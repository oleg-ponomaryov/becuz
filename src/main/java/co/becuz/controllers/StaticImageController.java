package co.becuz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import co.becuz.configuration.ConfigurationSettings;
import co.becuz.dto.response.StaticImageResponse;
import co.becuz.services.PhotoService;

@Controller
public class StaticImageController {

	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private ConfigurationSettings config;

	@RequestMapping(value = "/startup/images", method = RequestMethod.GET)
	public @ResponseBody StaticImageResponse images() {

		String s3Bucket = config.getProperty("S3_UPLOAD_BUCKET");
		String startup_prefix = config.getProperty("STARTUP_PREFIX");
		String logos_prefix = config.getProperty("LOGOS_PREFIX");
		String slides_prefix = config.getProperty("SLIDES_PREFIX");
		
		StaticImageResponse resp = new StaticImageResponse(); 

		resp.logos =  photoService.getStaticImages(s3Bucket, logos_prefix);
		resp.advertiseAnimation = photoService.getStaticImages(s3Bucket, slides_prefix);
		resp.getStartedButton = photoService.getStaticImages(s3Bucket, startup_prefix).get(0);
		resp.backgroundColor = config.getProperty("backgroundColor");
		resp.headlineNote = config.getProperty("headlineNote");
		
		return resp;
	}
}
