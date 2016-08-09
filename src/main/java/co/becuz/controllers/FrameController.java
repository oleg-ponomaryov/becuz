package co.becuz.controllers;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.becuz.domain.Frame;
import co.becuz.services.FrameService;

@Controller
public class FrameController {

    private final FrameService frameService;
    private static final Logger log = LoggerFactory
			.getLogger(FrameController.class);
    
    @Autowired
    public FrameController(FrameService frameService) {
        this.frameService = frameService;
    }
    
    /*** For large number there  will be paging ***/
    @RequestMapping(value = "/frames/all", method=RequestMethod.GET)
    public @ResponseBody Collection<Frame> getAllFrames(Model model) {
        return frameService.getAllFrames();
    }
    
    @RequestMapping(value = "/frames",method=RequestMethod.POST)
    public @ResponseBody Frame create(@RequestBody Frame frame) {
      return frameService.save(frame);
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="/frames/{id}")
    public @ResponseBody void delete(@PathVariable String id) {
    	frameService.delete(id);
    }
    
    @RequestMapping(method=RequestMethod.PUT, value="/frames/{id}")
    public @ResponseBody Frame update(@PathVariable String id, @RequestBody Frame frame) {
      return frameService.update(frame);
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/frames/{id}")
    public @ResponseBody Frame getFrame(@PathVariable String id) {
    	
    	Frame frame = frameService.getFrameById(id);
    	if (frame != null) {
    		return frame;
    	}
    	else {
    		throw new NoSuchElementException(String.format("Frame=%s not found", id));
    	}
    }
}