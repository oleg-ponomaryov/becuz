package co.becuz.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.becuz.configuration.ConfigurationSettings;
import co.becuz.domain.Collection;
import co.becuz.domain.CollectionPhotos;
import co.becuz.domain.Photo;
import co.becuz.domain.User;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.PhotoDTO;
import co.becuz.dto.response.PhotoDeleteResponse;
import co.becuz.dto.response.PhotoSaveResponse;
import co.becuz.repositories.PhotoRepository;
import co.becuz.repositories.UserRepository;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class PhotoServiceImpl implements PhotoService {

	private static final Logger LOG = LoggerFactory
			.getLogger(PhotoServiceImpl.class);
	
	@Autowired
	protected AmazonS3 s3Client;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private CollectionPhotosService collectionPhotosService;
	
	@Autowired
	private ConfigurationSettings config;
	
    @Override
    public List<Photo> listAllPhotos() {
        return photoRepository.findAll();
    }

	@Override
	public java.util.Collection<Photo> listAllPhotosForUser(String id) {
        return photoRepository.findAllByOwnerId(id);
	}

	@Override
	public List<PhotoDTO> getStaticImages(String bucket, String prefix) {
		
		List<PhotoDTO> ret = new ArrayList<>();
		String baseUrl = commonService.getS3BucketUrl();
		String captionMmetadataKey = config.getProperty("captionMmetadataKey");
		String userMmetadataKey = config.getProperty("userMmetadataKey");
		
		final ListObjectsRequest req = new ListObjectsRequest().withBucketName(bucket).withPrefix(prefix);
		final ObjectListing objectListing = s3Client.listObjects(req);
		List<S3ObjectSummary> s3list = objectListing.getObjectSummaries();
		shuffleList(s3list);
		for(S3ObjectSummary objectSummary : s3list) {
			if (objectSummary.getKey().endsWith("/")) {
				continue;
			}
			
			PhotoDTO st = new PhotoDTO();
			try {
				st.setExpiringUrl(new URL(baseUrl+objectSummary.getKey()));
			} catch (MalformedURLException e) {
				LOG.error("Could not parse URL:"+baseUrl+objectSummary.getKey());
			}
			st.setMd5Digest(objectSummary.getETag());
			
			ObjectMetadata objectMetadata = s3Client.getObjectMetadata(bucket, objectSummary.getKey());
            Map<String, String> userMetadataMap = objectMetadata.getUserMetadata();
            
            //for (Entry<String, String> e : userMetadataMap.entrySet()) {
    		//	LOG.debug(e.getKey()+"->"+e.getValue());
            //}
            
			st.setCaption(userMetadataMap.get(captionMmetadataKey));
			if (userMetadataMap.get(userMmetadataKey)!=null) {
				String[] uarr = userMetadataMap.get(userMmetadataKey).split(" ");
				User u = new User();
				u.setFirstname(uarr[0]);
				u.setLastname(uarr[1]);
				st.setOwner(u);
			}
			
			ret.add(st);
		}
		return ret;
	}
	
    
	@Override
	@Transactional
	public PhotoSaveResponse save(String bucket, String photoKey, String collectionId, CurrentUser user) throws ParseException {
		LOG.debug("Saving for bucket:{} and key:{}:",bucket, photoKey);
		PhotoSaveResponse resp = new PhotoSaveResponse();
		
		Collection collection = collectionService.getCollectionById(collectionId);
		if (collection==null) {
			resp.setStatus("ERROR");
			resp.setMessage(String.format("Collection with id %s not found",collectionId));
		}
		
		GetObjectMetadataRequest metadataReq = new GetObjectMetadataRequest(
				bucket, photoKey);
		ObjectMetadata metadata = s3Client.getObjectMetadata(metadataReq);
		Map<String, String> userMetadata = metadata.getUserMetadata();
		Photo photo  = photoRepository.findByMd5Digest(metadata.getETag());
		
		if (photo == null) {
			photo = new Photo();
		}	

		photo.setDescription(userMetadata.get("description"));
		Iterator it = userMetadata.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        LOG.info(pair.getKey() + " = " + pair.getValue());
		    }
		
		User us = userService.getUserById(user.getUser().getId());
		photo.setOwner(us);
		photo.setCaption(userMetadata.get("title"));
		photo.setOriginalKey(photoKey);
		photo.setBucket(userMetadata.get("bucket"));
		photo.setMd5Digest(metadata.getETag());
		photo.setUploadedDate(new Date());

		Photo p = photoRepository.save(photo);
		CollectionPhotos col = collectionPhotosService.getCollectionPhotosByPhotoAndCollection(photo, collection);
		
		if ( col != null) {
			resp.setStatus("WARNING");
			resp.setMessage(String.format("Reference already exists for Collection %s and Photo %s",collection.getId(), photo.getId()));
		}
		else {
			col = new CollectionPhotos();
			col.setCollection(collection);
			col.setPhoto(photo);
			collectionPhotosService.save(col);
			resp.setStatus("SUCCESS");
		}

		resp.setCollectionPhotos(collectionPhotosService.getPhotosWithUrls(collection));
		
		return resp;
	}

	@Override
	@Transactional
	public PhotoDeleteResponse delete(java.util.Collection<PhotoDTO> photos, CurrentUser user) {
		PhotoDeleteResponse resp = new PhotoDeleteResponse();
		
		Iterator<PhotoDTO> ph_it = photos.iterator();
	    while (ph_it.hasNext()) {
	    	String id = ph_it.next().getId();
	    	if (StringUtils.isEmpty(id)) {
	    		continue;
	    	}
	        Photo ph = photoRepository.findOne(id);
	        if (ph==null) {
	        	continue;
	        }
	        boolean can_delete = false;
	        if (ph.getOwner().equals(user.getUser())) {
	        	can_delete = true;
	        }
	        else {
		        for (CollectionPhotos c : collectionPhotosService.getAllCollectionPhotosByPhoto(ph)) {
		        	if (c.getCollection().getUser().equals(user.getUser())) {
			        	can_delete = true;
			        	break;	
		        	}
		        }
	        }
	        if (can_delete) {
	        	try {
	        		photoRepository.delete(ph);
	    			resp.getMessages().add(String.format("Deleted photo with id %s",ph.getId()));
	        	}
	        	catch (Exception e) {
	    			resp.setStatus("ERROR");
	    			resp.getMessages().add(String.format("Could not delete Photo/S3 object %s,%s",ph.getId(),ph.getOriginalKey()));
	        	}
	        }
	    }
		
	    if (resp.getStatus()==null) {
	    	resp.setStatus("SUCCESS");
	    }
		return resp;
	}
	
	@Override
	public PhotoDTO generateExpiringUrl(Photo photo, long expirationInMillis) {
		Date expiration = new java.util.Date();
		long msec = expiration.getTime();
		msec += expirationInMillis;
		expiration.setTime(msec);
		// Expiring URL for original photo
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				photo.getBucket(), photo.getOriginalKey());
		
		generatePresignedUrlRequest.setMethod(HttpMethod.GET);
		generatePresignedUrlRequest.setExpiration(expiration);
		PhotoDTO dto = new PhotoDTO();
		dto.setId(photo.getId());
		dto.setMd5Digest(photo.getMd5Digest());
		dto.setCaption(photo.getCaption());
		dto.setOwner(photo.getOwner());
		URL url = s3Client
				.generatePresignedUrl(generatePresignedUrlRequest);
		dto.setExpiringUrl(url);
		LOG.info("Generated URL->"+url);
		// Expiring URL for thumbnail photo
		if (photo.getThumbnailKey() != null) {
			generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
					photo.getBucket(), photo.getThumbnailKey());
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			generatePresignedUrlRequest.setExpiration(expiration);
			dto.setExpiringThumbnailUrl(s3Client
					.generatePresignedUrl(generatePresignedUrlRequest));
		}
		return dto;
	}

	@Override
	public List<PhotoDTO> generateExpiringUrls(List<Photo> photos,
			long expirationInMillis) {
		List<PhotoDTO> newPhotos = new ArrayList<PhotoDTO>();
		if (null != photos) {
			for (Photo photo : photos) {
				newPhotos.add(generateExpiringUrl(photo, expirationInMillis));
			}
		}
		return newPhotos;
	}

	@Override
	public Photo save(Photo photo) {
	       return photoRepository.save(photo);
	}

	@Override
	public Photo update(PhotoDTO photo) {
		if (photo==null || photo.getId()==null) {
    		throw new NoSuchElementException("Photo not found");
		}
    	Photo ph = photoRepository.findOne(photo.getId());
    	if (ph == null) {
    		throw new NoSuchElementException(String.format("Photo=%s not found", photo.getId()));
    	}

    	if (photo.getCaption()!=null)
    		ph.setCaption(photo.getCaption());
    	
    	if (photo.getMd5Digest()!=null)
    		ph.setMd5Digest(photo.getMd5Digest());

    	if (photo.getOwner()!=null)
    		ph.setOwner(photo.getOwner());
		
	       return photoRepository.save(ph);
	}
	
	private void shuffleList(List<S3ObjectSummary> list)
	  {
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = list.size() - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      S3ObjectSummary a = list.get(index);
	      list.set(index, list.get(i));
	      list.set(i, a);
	    }
	  }
}
