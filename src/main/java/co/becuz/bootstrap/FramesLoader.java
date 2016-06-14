package co.becuz.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import co.becuz.domain.Frame;
import co.becuz.domain.enums.DisplayType;
import co.becuz.repositories.FrameRepository;

@Component
public class FramesLoader implements ApplicationListener<ContextRefreshedEvent> {
 
    private FrameRepository frameRepository;
	private static Logger LOGGER = LoggerFactory
			.getLogger(FramesLoader.class);
    
    @Autowired
    public void setFrameRepository(FrameRepository frameRepository) {
        this.frameRepository = frameRepository;
    }
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
 
        Frame frame = new Frame();
        frame.setDisplayType(DisplayType.SLIDESHOW);
        frame.setUrl("http://becuz.net/zep");
        frameRepository.save(frame);
        
        LOGGER.info("Frame is loaded - id: " + frame.getId());
    }
}