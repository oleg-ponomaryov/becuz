package co.becuz.controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.becuz.domain.Device;
import co.becuz.domain.User;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.repositories.DeviceRepository;

@Controller
public class DeviceController {
	
	@Autowired
	private DeviceRepository deviceRepository;

	@RequestMapping(value = "/devices", method = RequestMethod.GET)
	public @ResponseBody java.util.Collection<Device> photos(@ModelAttribute CurrentUser currentUser) {
		java.util.Collection<Device> ret = deviceRepository.findAllByUser(currentUser.getUser());
		return ret;
	}
	
    @RequestMapping(value = "/devices",method=RequestMethod.POST)
    @Transactional
    public @ResponseBody Device create(@RequestBody Device device) {
      User user = device.getUser(); 
      String token = device.getToken();
    	
      if (user == null || token==null) {
    	  throw new NoSuchElementException(String.format("User and token are required"));
      }
      
      Device d = deviceRepository.findByUserAndToken(user, token);
      if (d!=null) {
    	  return d;
      }
      else {
    	  d = deviceRepository.save(device);
      }
      
      return d;
    }
}
