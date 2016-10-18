package co.becuz.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.becuz.configuration.ConfigurationSettings;
import co.becuz.dto.PhotoDTO;
import co.becuz.services.PhotoService;

@Controller
public class StaticImageController {

	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private ConfigurationSettings config;
	
	@RequestMapping(value = "/stockphotos", method = RequestMethod.GET)
	public @ResponseBody List<PhotoDTO> getStockphotos() {

		String s3Bucket = config.getProperty("S3_UPLOAD_BUCKET");
		String stockphotos_prefix = config.getProperty("STOCKPHOTOS_PREFIX");

		List<PhotoDTO> resp =  photoService.getStaticImages(s3Bucket, stockphotos_prefix);
		
		return resp;
	}
}
