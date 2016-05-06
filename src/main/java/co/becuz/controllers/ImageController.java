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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.becuz.configuration.ConfigurationSettings;
import co.becuz.domain.CurrentUser;
import co.becuz.domain.Image;
import co.becuz.domain.Role;
import co.becuz.dto.ImageDTO;
import co.becuz.repositories.ImageRepository;
import co.becuz.services.CommonService;
import co.becuz.services.ImageService;
import co.becuz.util.ImageUploadFormSigner;

import com.amazonaws.services.s3.AmazonS3;

@Controller
public class ImageController {

	@Autowired
	private ImageService imageService;

	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ConfigurationSettings config;

	@Autowired
	private AmazonS3 s3Client;
	
	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/images", method = RequestMethod.GET)
	public String images(ModelMap model,@ModelAttribute CurrentUser currentUser) {
		if (currentUser.getRole()==Role.ADMIN) {
			model.addAttribute("images", imageService.listAllImages());
		}
		else {
			model.addAttribute("images", imageService.listAllImagesForUser(currentUser.getId()));
		}
		return "images";
	}

	@RequestMapping(value = "/image/{imageId}", method = RequestMethod.GET)
	public String imageGet(ModelMap model, @PathVariable String imageId,
			@RequestParam(value = "delete", required = false) String delete, @ModelAttribute CurrentUser currentUser) {
		Image image = imageRepository.getOne(imageId);
		ImageDTO dto = new ImageDTO();
		if (delete != null) {
			imageRepository.delete(image);
			return images(model, currentUser);
		} else {
			dto = imageService.generateExpiringUrl(image, 500000);
			model.addAttribute("image", image);
			model.addAttribute("dto", dto);
			model.addAttribute("templateName", "image_edit");
			return "image_edit";
		}
	}
	
	@RequestMapping(value = "/image/{imageId}", method = RequestMethod.POST)
	public String imageEdit(@ModelAttribute Image image,
			@PathVariable String imageId, BindingResult result,
			RedirectAttributes attr, HttpSession session) {
		imageRepository.save(image);
		return "redirect:/";
	}

	@RequestMapping(value = "/image/upload", method = RequestMethod.GET)
	//@ResponseBody ImageUploadFormSigner
	public String  imageUpload(ModelMap model, HttpServletRequest request,
			@ModelAttribute CurrentUser currentUser) {
		// Image redirect URL
		String redirectUrl = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/image/ingest";
		// Prepare S3 form upload
		ImageUploadFormSigner formSigner = new ImageUploadFormSigner(
				config.getProperty("S3_UPLOAD_BUCKET"),
				config.getProperty("S3_UPLOAD_PREFIX"), currentUser, config,
				redirectUrl, commonService);
		
		model.addAttribute("formSigner", formSigner);
		model.addAttribute("templateName", "image_upload");
		return "image_upload";
		//return formSigner;
	}

	@RequestMapping(value = "/image/ingest", method = RequestMethod.GET)
	public String imageIngest(ModelMap model,
			@RequestParam(value = "bucket") String bucket,
			@RequestParam(value = "key") String imageKey) throws ParseException {
		imageService.save(bucket, imageKey);
		// Kick off preview encoding
		return "redirect:/";
	}
}
