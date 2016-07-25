package co.becuz.services;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    @Value("${app.bundle.id}")
	private String appId;

    @Value("${app.key}")
    private String appKey;
    
    private Cipher cipher;
    private SecretKeySpec secretKeySpec;
	
	@PostConstruct 
	public void init() {
    	if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appKey)) {
			throw new IllegalArgumentException("Security parameters are not defined");
    	}
     	
        try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new IllegalArgumentException("Could not create a cipher");
		}
        secretKeySpec = new SecretKeySpec(appKey.getBytes(),"AES");
    }
	
	public String encrypt(final String textData, final byte[] iv) {
        IvParameterSpec ivps = new IvParameterSpec(iv);
        try {
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
	        return (new String(Base64.getEncoder().encode(
	        cipher.doFinal(textData.getBytes("UTF-8")))));
        }
        catch(Exception e) {
        	throw new IllegalStateException("Error during encryption",e);
        }
    }
	
	public String decrypt(final String encryptedTextData, final byte[] iv)  {
		byte[] decrypedData = null;
        try {
			IvParameterSpec ivps = new IvParameterSpec(iv);
	        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps);
	        decrypedData = cipher.doFinal(Base64.getDecoder().decode(encryptedTextData.getBytes("UTF-8")));
        }
        catch (Exception e) {
        	throw new IllegalStateException("Error during decryption",e);
        }
        return (new String(decrypedData));
    }
	
	public String getAppId() {
		return appId;
	}
}
