package co.becuz.services;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.becuz.domain.Frame;
import co.becuz.repositories.FrameRepository;

@Service
public class FrameServiceImpl implements FrameService {


    @Autowired
    private FrameRepository frameRepository;

    @Override
    public Frame getFrameById(String id) {
        return frameRepository.findOne(id);
    }

    @Override
    public Collection<Frame> getAllFrames() {
        return frameRepository.findAll(new Sort("priority"));
    }
    
    @Override
    public void delete(String id) {
        frameRepository.delete(id);
    }

    @Override
    public Frame save(Frame frame) {
        return frameRepository.save(frame);
    }
    
    @Override
    public Frame update(Frame frame) {
    	Frame fr = getFrameById(frame.getId());
    	if (fr == null) {
    		throw new NoSuchElementException(String.format("Frame=%s not found", frame.getId()));
    	}
    	
    	if (frame.getDescription() != null)
    		fr.setDescription(frame.getDescription());
    	
    	if (frame.getDisplayType()!=null)
    		fr.setDisplayType(frame.getDisplayType());
    		
    	fr.setPriority(frame.getPriority());
    		
    	if (frame.getStatus()!=null)
    		fr.setStatus(frame.getStatus());
    	
    	if (frame.getUrl()!=null)
    		fr.setUrl(frame.getUrl());
    	
        return frameRepository.save(fr);
    }
}
