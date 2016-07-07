package co.becuz.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.becuz.domain.Collection;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.services.CollectionService;

@Controller
public class CollectionController {

    private final CollectionService collectionService;
    private static final Logger log = LoggerFactory
			.getLogger(CollectionController.class);
    
    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }
    
    @RequestMapping(value = "/collections", method=RequestMethod.GET)
    public @ResponseBody java.util.Collection<Collection> getAllCollectionsByUser(@ModelAttribute CurrentUser currentUser) {
        return collectionService.getAllCollectionsByUserId(currentUser.getId());
    }
    
    @RequestMapping(value = "/collections",method=RequestMethod.POST)
    public @ResponseBody Collection create(@RequestBody Collection collection) {
      return collectionService.save(collection);
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="/collections/{id}")
    public @ResponseBody void delete(@PathVariable String id) {
    	collectionService.delete(id);
    }
    
    @RequestMapping(method=RequestMethod.PUT, value="/collections/{id}")
    public @ResponseBody Collection update(@PathVariable String id, @RequestBody Collection collection) {
      return collectionService.update(collection);
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/collections/{id}")
    public @ResponseBody Collection getCollection(@PathVariable String id) {
    	
    	Collection collection = collectionService.getCollectionById(id);
    	if (collection != null) {
    		return collection;
    	}
    	else {
    		throw new NoSuchElementException(String.format("Collection=%s not found", id));
    	}
    }
}