package co.becuz.util;

import java.util.UUID;

import co.becuz.configuration.ConfigurationSettings;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.services.CommonService;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;

/**
 *
 * @author oleg.ponomaryov
 *
 */
public class PhotoUploadFormSigner extends S3FormSigner {
	private String s3Bucket;
	private String objectKey;
	private String keyPrefix;
	private String successActionRedirect;
	private String encodedPolicy;
	private String signature;
	private String uuid;
	private CurrentUser user;
	private AWSCredentialsProvider credsProvider;
	private CommonService commonService;

	public PhotoUploadFormSigner(String s3Bucket, String keyPrefix, CurrentUser user,
			ConfigurationSettings config, String successActionRedirect, CommonService commonService) {
		this.s3Bucket = s3Bucket;
		this.keyPrefix = keyPrefix;
		this.successActionRedirect = successActionRedirect;
		this.user = user;
		this.credsProvider = config.getAWSCredentialsProvider();
		this.uuid = UUID.randomUUID().toString();
		String policy = super.generateUploadPolicy(s3Bucket, keyPrefix,
				credsProvider, successActionRedirect);
		String[] policyAndSig = super.signRequest(credsProvider, policy);
		// Create object key
		generatePhotoObjectKey();
		// Create policy
		this.encodedPolicy = policyAndSig[0];
		this.signature = policyAndSig[1];
		this.commonService = commonService;
	}

	/**
	 * Generate a unique object key for this upload
	 */
	private void generatePhotoObjectKey() {
		this.objectKey = this.keyPrefix + "/original/" + user.getId();
	}

	public AWSCredentialsProvider getCredsProvider() {
		return credsProvider;
	}

	public String getS3Bucket() {
		return s3Bucket;
	}

	public String getS3BucketUrl() {
		return commonService.getS3BucketUrl();
	}
	
	public void setS3BucketUrl(String s3BucketUrl) {
		this.s3Bucket = s3BucketUrl;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public Boolean getIsToken() {
		return credsProvider.getCredentials() instanceof BasicSessionCredentials;
	}

	public String getSuccessActionRedirect() {
		return successActionRedirect;
	}

	public void setSuccessActionRedirect(String successActionRedirect) {
		this.successActionRedirect = successActionRedirect;
	}

	public String getEncodedPolicy() {
		return encodedPolicy;
	}

	public void setPolicy(String policy) {
		this.encodedPolicy = policy;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
