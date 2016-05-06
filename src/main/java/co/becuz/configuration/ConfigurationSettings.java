package co.becuz.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSCredentialsProvider;

/**
 * The ConfigurationSettings class is a singleton class that retrieves the
 * settings from the aMediaManager.properties file or from the EC2 Metadata URL
 * or Elastic Beanstalk Environment Metadata.
 */
@Configuration
public class ConfigurationSettings {
	private static final Logger LOG = LoggerFactory
			.getLogger(ConfigurationSettings.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private AWSCredentialsProvider credsProvider;

	/**
	 * This method returns the AWS credentials object.
	 *
	 * @return AWS credentials taken from the properties and user-data.
	 */
	public AWSCredentialsProvider getAWSCredentialsProvider() {
		return credsProvider;
	}

	/**
	 * Accessor for the various properties in the configuration.
	 *
	 * @param propertyName
	 *            the name of the property key. The static strings on this class
	 *            can also be used.
	 * @return the value of the property.
	 */
	public String getProperty(String property_name) {
		return env.getProperty(property_name);
	}
}