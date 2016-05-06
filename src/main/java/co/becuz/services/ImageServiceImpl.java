package co.becuz.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.becuz.configuration.ConfigurationSettings;
import co.becuz.domain.Image;
import co.becuz.dto.ImageDTO;
import co.becuz.dto.response.StaticImage;
import co.becuz.repositories.ImageRepository;
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
public class ImageServiceImpl implements ImageService {

	private static final Logger LOG = LoggerFactory
			.getLogger(ImageServiceImpl.class);
	
	@Autowired
	protected AmazonS3 s3Client;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ConfigurationSettings config;
	
    @Override
    public Iterable<Image> listAllImages() {
        return imageRepository.findAll();
    }

	@Override
	public Iterable<Image> listAllImagesForUser(String Id) {
        return imageRepository.findByOwner(userRepository.getOne(Id));
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
	public Image save(String bucket, String imageKey) throws ParseException {

		LOG.debug("Saving for bucket:{} and key:{}:",bucket, imageKey);
		GetObjectMetadataRequest metadataReq = new GetObjectMetadataRequest(
				bucket, imageKey);
		ObjectMetadata metadata = s3Client.getObjectMetadata(metadataReq);
		
		Map<String, String> userMetadata = metadata.getUserMetadata();
		
		Image image  = imageRepository.findByMd5Digest(metadata.getETag());
		
		if (image == null) {
			image = new Image();
		}	

		image.setDescription(userMetadata.get("description"));

		Iterator it = userMetadata.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		    }
		
		image.setOwner(userService.getUserById(userMetadata.get("owner")));
		image.setTitle(userMetadata.get("title"));
		//image.setCreatedDate(new SimpleDateFormat("MM/dd/yyyy")
		//		.parse(userMetadata.get("createddate")));
		image.setOriginalKey(imageKey);
		image.setBucket(userMetadata.get("bucket"));
		image.setMd5Digest(metadata.getETag());
		image.setUploadedDate(new Date());

		imageRepository.save(image);
		return image;
	}

	@Override
	public ImageDTO generateExpiringUrl(Image image, long expirationInMillis) {
		Date expiration = new java.util.Date();
		long msec = expiration.getTime();
		msec += expirationInMillis;
		expiration.setTime(msec);
		// Expiring URL for original image
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				image.getBucket(), image.getOriginalKey());
		
		generatePresignedUrlRequest.setMethod(HttpMethod.GET);
		generatePresignedUrlRequest.setExpiration(expiration);
		ImageDTO dto = new ImageDTO();
		dto.setInageId(image.getId());
		dto.setExpiringUrl(s3Client
				.generatePresignedUrl(generatePresignedUrlRequest));
		// Expiring URL for thumbnail image
		if (image.getThumbnailKey() != null) {
			generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
					image.getBucket(), image.getThumbnailKey());
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			generatePresignedUrlRequest.setExpiration(expiration);
			dto.setExpiringThumbnailUrl(s3Client
					.generatePresignedUrl(generatePresignedUrlRequest));
		}
		return dto;
	}

	@Override
	public List<ImageDTO> generateExpiringUrls(List<Image> images,
			long expirationInMillis) {
		List<ImageDTO> newImages = new ArrayList<ImageDTO>();
		if (null != images) {
			for (Image image : images) {
				newImages.add(generateExpiringUrl(image, expirationInMillis));
			}
		}
		return newImages;
	}

}
