package co.becuz.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FaceBookController {

	
	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	private Facebook facebook;
	
	@Autowired
	private Environment environment;
	
	@RequestMapping(value = "/facebook/success", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> connected(Model model) {

		if (facebook==null || !facebook.isAuthorized() ) {
			throw new RuntimeException("Facebook is null or not Authorized");
		}
		
		UserOperations s = facebook.userOperations();
		Map<String, Object> ret_map = new HashMap<String, Object>();
		ret_map.put("profile", facebook.userOperations().getUserProfile());
		ret_map.put("albums", facebook.mediaOperations().getAlbums());
		return ret_map;
	}

	@RequestMapping(value = "/facebook/album/{albumId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> showAlbum(@PathVariable("albumId") String albumId, Model model) {

		Map<String, Object> ret_map = new HashMap<String, Object>();
		ret_map.put("album", facebook.mediaOperations().getAlbum(albumId));
		ret_map.put("photos", facebook.mediaOperations().getPhotos(albumId));

		return ret_map;
	}
}
