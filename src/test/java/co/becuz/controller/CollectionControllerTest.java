package co.becuz.controller;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import co.becuz.SpringBootWebApplication;
import co.becuz.domain.Collection;
import co.becuz.domain.Frame;
import co.becuz.domain.User;
import co.becuz.domain.enums.FrameOwner;
import co.becuz.repositories.CollectionRepository;
import co.becuz.repositories.FrameRepository;
import co.becuz.repositories.UserRepository;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@WebAppConfiguration
@IntegrationTest("server.port:0")

public class CollectionControllerTest {

    @Autowired
    UserRepository repository;
	
    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    FrameRepository frameRepository;
    
    Collection collection;
    User demo;

    @Value("${local.server.port}")
    int port;
    private static boolean setUpIsDone = false;
    
    @Before
    public void setUp() {
    	if (setUpIsDone) {
    		return;
    	}
        demo = repository.findOne("abc-edcv");
    	
       	Frame frame = new Frame();
    	frame.setUrl("http://becuz.net");
    	frame = frameRepository.save(frame);
        
        collection = new Collection();
        collection.setFrame(frame);
        collection.setHeadline("My headline");
        collection.setOwnerType(FrameOwner.OWNER);
        collection.setUser(demo);

        //repository.deleteAll();
        collectionRepository.save(Arrays.asList(collection));

        RestAssured.port = port;
        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));
        setUpIsDone = true;
    }

    
    @Test
    public void getAllCollections() {
        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/collections");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .and().body("headline", hasItems("My headline"));
        return;
    }
/*
    @Test
    public void deleteUser() {

        assertEquals(repository.findAllByEmail("oleg@oleg.com").size(),1);
        User oleg = repository.findAllByEmail("oleg@oleg.com").iterator().next();
    	
        RestAssured.expect().statusCode( 200 )
    	.when().delete( "/users/" + oleg.getId());

        assertEquals(repository.findAllByEmail("oleg@oleg.com").size(),0);
        return;
    }

*/    
    @Test
    public void updateCollection() {

    	demo = repository.findOne("abc-edcv");
    	
        assertEquals(collectionRepository.findAllByUserId(demo.getId()).size(),1);
        
        collection = collectionRepository.findAllByUserId(demo.getId()).iterator().next();
    	
        Collection col  = collectionRepository.findOne(collection.getId());
        col.setHeadline("My new headline");

        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(col).when().put("/collections/"+col.getId());
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("headline", Matchers.is("My new headline"));
        //.body("email", Matchers.is("greatjohn@john.com"));

        //assertEquals(repository.findAllByEmail("greatjohn@john.com").size(),1);
        
        return;
    }
}