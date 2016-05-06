package co.becuz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.becuz.configuration.ConfigurationSettings;

@Service
public class CommonService {

	@Autowired
	private ConfigurationSettings config;
	
	public String getS3BucketUrl() {
		String region = config.getProperty("AWS_REGION");
		String s3Bucket = config.getProperty("S3_UPLOAD_BUCKET");

		if (region.equals("us-east-1")) {
			region = "external-1";
		}
		String prefix = "s3-" + region;
		return "https://" + s3Bucket + "." + prefix + ".amazonaws.com/";
	}
}
