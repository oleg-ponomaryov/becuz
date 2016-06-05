package co.becuz.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.becuz.configuration.ConfigurationSettings;
import co.becuz.domain.Photo;
import co.becuz.dto.PhotoDTO;
import co.becuz.dto.response.StaticImage;
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
	private ConfigurationSettings config;
	
    @Override
    public Iterable<Photo> listAllPhotos() {
        return photoRepository.findAll();
    }

	@Override
	public Iterable<Photo> listAllPhotosForUser(String Id) {
        return photoRepository.findByOwner(userRepository.getOne(Id));
	}

	@Override
	public List<StaticImage> getStaticImages(String bucket, String prefix) {
		
		List<StaticImage> ret = new ArrayList<>();
		String baseUrl = commonService.getS3BucketUrl();
		String metadataKey = config.getProperty("initialMetadataKey");
		
		final ListObjectsRequest req = new ListObjectsRequest().withBucketName(bucket).withPrefix(prefix);
		final ObjectListing objectListing = s3Client.listObjects(req);
		for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			if (objectSummary.getKey().endsWith("/")) {
				continue;
			}
			
			StaticImage st = new StaticImage();
			st.setUrl(baseUrl+objectSummary.getKey());
			st.setInitial(false);
			st.setMd5(objectSummary.getETag());
			
			LOG.debug("Key:{}", objectSummary.getKey());
			
			ObjectMetadata objectMetadata = s3Client.getObjectMetadata(bucket, objectSummary.getKey());
            Map<String, String> userMetadataMap = objectMetadata.getUserMetadata();
            
            if (userMetadataMap!=null && userMetadataMap.get(metadataKey) != null && userMetadataMap.get(metadataKey).equals("true")) {
            	st.setInitial(true);
            }
			
			ret.add(st);
		}
		return ret;
	}
	
    
	@Override
	public Photo save(String bucket, String photoKey) throws ParseException {

		LOG.debug("Saving for bucket:{} and key:{}:",bucket, photoKey);
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
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		    }
		
		photo.setOwner(userService.getUserById(userMetadata.get("owner")));
		photo.setTitle(userMetadata.get("title"));
		//photo.setCreatedDate(new SimpleDateFormat("MM/dd/yyyy")
		//		.parse(userMetadata.get("createddate")));
		photo.setOriginalKey(photoKey);
		photo.setBucket(userMetadata.get("bucket"));
		photo.setMd5Digest(metadata.getETag());
		photo.setUploadedDate(new Date());

		photoRepository.save(photo);
		return photo;
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
		dto.setInageId(photo.getId());
		dto.setExpiringUrl(s3Client
				.generatePresignedUrl(generatePresignedUrlRequest));
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
}
