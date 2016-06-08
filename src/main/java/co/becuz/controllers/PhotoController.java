package co.becuz.controllers;

import java.text.ParseException;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.becuz.configuration.ConfigurationSettings;
import co.becuz.domain.Collection;
import co.becuz.domain.Photo;
import co.becuz.domain.enums.Role;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.PhotoDTO;
import co.becuz.repositories.PhotoRepository;
import co.becuz.services.CommonService;
import co.becuz.services.PhotoService;
import co.becuz.util.PhotoUploadFormSigner;

import com.amazonaws.services.s3.AmazonS3;

@Controller
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
	private ConfigurationSettings config;

	@Autowired
	private AmazonS3 s3Client;
	
	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/photos", method = RequestMethod.GET)
	public String photos(ModelMap model,@ModelAttribute CurrentUser currentUser) {
		if (currentUser.getRole()==Role.ADMIN) {
			model.addAttribute("photos", photoService.listAllPhotos());
		}
		else {
			model.addAttribute("photos", photoService.listAllPhotosForUser(currentUser.getId()));
		}
		return "photos";
	}

	@RequestMapping(value = "/photo/{photoId}", method = RequestMethod.GET)
	public String imageGet(ModelMap model, @PathVariable String photoId,
			@RequestParam(value = "delete", required = false) String delete, @ModelAttribute CurrentUser currentUser) {
		Photo photo = photoRepository.getOne(photoId);
		PhotoDTO dto = new PhotoDTO();
		if (delete != null) {
			photoRepository.delete(photo);
			return photos(model, currentUser);
		} else {
			dto = photoService.generateExpiringUrl(photo, 500000);
			model.addAttribute("photo", photo);
			model.addAttribute("dto", dto);
			model.addAttribute("templateName", "photo_edit");
			return "photo_edit";
		}
	}
	
	@RequestMapping(value = "/photo/{photoId}", method = RequestMethod.POST)
	public String imageEdit(@ModelAttribute Photo photo,
			@PathVariable String photoId, BindingResult result,
			RedirectAttributes attr, HttpSession session) {
		photoRepository.save(photo);
		return "redirect:/";
	}

	@RequestMapping(value = "/photo/add", method = RequestMethod.GET)
	public String  imageAdd(ModelMap model, HttpServletRequest request,
			@ModelAttribute CurrentUser currentUser) {
		// Photo redirect URL
		String redirectUrl = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/photo/ingest";
		// Prepare S3 form upload
		PhotoUploadFormSigner formSigner = new PhotoUploadFormSigner(
				config.getProperty("S3_UPLOAD_BUCKET"),
				config.getProperty("S3_UPLOAD_PREFIX"), currentUser, config,
				redirectUrl, commonService);
		
		model.addAttribute("formSigner", formSigner);
		model.addAttribute("templateName", "photo_upload");
		return "photo_upload";
	}

/*
	@RequestMapping(value = "/photo/upload", method = RequestMethod.GET)
	public @ResponseBody PhotoUploadFormSigner imageUpload(HttpServletRequest request,
			@ModelAttribute CurrentUser currentUser) {
		// Photo redirect URL
		String redirectUrl = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/photo/ingest";
		// Prepare S3 form upload
		PhotoUploadFormSigner formSigner = new PhotoUploadFormSigner(
				config.getProperty("S3_UPLOAD_BUCKET"),
				config.getProperty("S3_UPLOAD_PREFIX"), currentUser, config,
				redirectUrl, commonService);
		
		return formSigner;
	}
*/

	@RequestMapping(value = "/photo/upload/{collectionId}", method = RequestMethod.POST)
	public @ResponseBody PhotoUploadFormSigner imageUpload(HttpServletRequest request,
			@ModelAttribute CurrentUser currentUser, @PathVariable String collectionId, @RequestBody Collection collection) {
		// Photo redirect URL
		String redirectUrl = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/photo/ingest";
		// Prepare S3 form upload

		PhotoUploadFormSigner formSigner = new PhotoUploadFormSigner(
				config.getProperty("S3_UPLOAD_BUCKET"),
				config.getProperty("S3_UPLOAD_PREFIX"), currentUser, config,
				redirectUrl, commonService);
		
		return formSigner;
	}
	
	@RequestMapping(value = "/photo/ingest", method = RequestMethod.GET)
	public String imageIngest(ModelMap model,
			@RequestParam(value = "bucket") String bucket,
			@RequestParam(value = "key") String photoKey) throws ParseException {
		photoService.save(bucket, photoKey);
		// Kick off preview encoding
		return "redirect:/";
	}
}
