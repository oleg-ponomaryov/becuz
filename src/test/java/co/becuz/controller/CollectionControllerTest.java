package co.becuz.controller;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import co.becuz.domain.CollectionPhotos;
import co.becuz.domain.Frame;
import co.becuz.domain.Photo;
import co.becuz.domain.User;
import co.becuz.domain.enums.CollectionStatus;
import co.becuz.domain.enums.FrameOwner;
import co.becuz.domain.enums.Role;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.PhotoDTO;
import co.becuz.repositories.CollectionPhotosRepository;
import co.becuz.repositories.CollectionRepository;
import co.becuz.repositories.FrameRepository;
import co.becuz.repositories.PhotoRepository;
import co.becuz.repositories.UserRepository;
import co.becuz.services.PhotoService;
import co.becuz.services.UserService;

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
    PhotoRepository photoRepository;

    @Autowired
    PhotoService photoService;
    
    @Autowired
    UserRepository repository;

    @Autowired
    UserService userService;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    FrameRepository frameRepository;

    @Autowired
    CollectionPhotosRepository collectionPhRepository;
    
    private Collection collection;
    private User demo;

    private Frame frame;
    
    @Value("${local.server.port}")
    int port;
    private static boolean setUpIsDone = false;
    
    @Before
    public void setUp() {
    	if (setUpIsDone) {
    		return;
    	}
        demo = repository.findOne("abc-edcv");
    	
       	frame = new Frame();
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

    
    @Test
    public void findInvitedTo() {
    	User u_demo = new User();
    	u_demo.setRole(Role.ADMIN);
    	u_demo.setUsername("ndemo1001");
    	u_demo.setPasswordHash("$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
    	u_demo.setEmail("ndemo1001@quantlance.com");
    	u_demo.setPhotoUrl("http://");
        userService.save(u_demo);

    	//1. Have user1 created a collection1
    	//2. Have user1 created a photo and put to collection1
    	//3. Have user1 created a collection2
    	//4. Have user1 created a photo and put to collection2
    	//3. Have user2 created a collection3 
    	//4. Have user2 created a photo and put to collection3
    	//5. Have user2 put a photo to collection1
    	//6. Have user2 put a photo to collection2
    	//7. Call findInvitedTo with user2, you should get collection1 and collection2
    	
    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo101");
        u.setPasswordHash("$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo101@quantlance.com");
        u.setPhotoUrl("http://");
        userService.save(u);

        RestAssured.port = port;
        RestAssured.authentication = RestAssured.form("ndemo101@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));
        
        Collection coll = new Collection();
        coll.setFrame(frame);
        coll.setHeadline("Collection 0");
        coll.setOwnerType(FrameOwner.OWNER);
        coll.setUser(u_demo);
        coll.setStatus(CollectionStatus.SHARED);
        collectionRepository.save(Arrays.asList(coll));

        Collection coll1 = new Collection();
        coll1.setFrame(frame);
        coll1.setHeadline("Collection 1");
        coll1.setOwnerType(FrameOwner.OWNER);
        coll1.setUser(u_demo);
        coll1.setStatus(CollectionStatus.SHARED);
        collectionRepository.save(Arrays.asList(coll1));
        
    	Photo p0 = new Photo();
    	p0.setBucket("coll");
    	p0.setCaption("My coll");
    	p0.setMd5Digest("coll1");
    	p0.setOriginalKey("/upload/file");
    	p0.setOwner(u);
    	p0.setUploadedDate(new Date());
    	photoRepository.save(p0);

    	Photo p1 = new Photo();
    	p1.setBucket("coll1");
    	p1.setCaption("My coll1");
    	p1.setMd5Digest("coll11");
    	p1.setOriginalKey("/upload/file");
    	p1.setOwner(u);
    	p1.setUploadedDate(new Date());
    	photoRepository.save(p1);
    	
    	CollectionPhotos ph = new CollectionPhotos();
    	ph.setCollection(coll);
    	ph.setPhoto(p0);
    	collectionPhRepository.save(ph);

    	CollectionPhotos ph1 = new CollectionPhotos();
    	ph1.setCollection(coll1);
    	ph1.setPhoto(p1);
    	collectionPhRepository.save(ph1);
    	
        Collection coll2 = new Collection();
        coll2.setFrame(frame);
        coll2.setHeadline("Collection 2");
        coll2.setOwnerType(FrameOwner.OWNER);
        coll2.setUser(u);
        collectionRepository.save(Arrays.asList(coll2));

    	Photo p2 = new Photo();
    	p2.setBucket("coll4");
    	p2.setCaption("My coll4");
    	p2.setMd5Digest("coll14");
    	p2.setOriginalKey("/upload/file");
    	p2.setOwner(u);
    	p2.setUploadedDate(new Date());
    	photoRepository.save(p2);

    	Photo p3 = new Photo();
    	p3.setBucket("coll3");
    	p3.setCaption("My coll3");
    	p3.setMd5Digest("coll13");
    	p3.setOriginalKey("/upload/file");
    	p3.setOwner(u);
    	p3.setUploadedDate(new Date());
    	photoRepository.save(p3);
    	
    	CollectionPhotos ph2 = new CollectionPhotos();
    	ph2.setCollection(coll2);
    	ph2.setPhoto(p2);
    	collectionPhRepository.save(ph2);
        
    	CollectionPhotos ph3 = new CollectionPhotos();
    	ph3.setCollection(coll);
    	ph3.setPhoto(p3);
    	collectionPhRepository.save(ph3);
        
    	CollectionPhotos ph4 = new CollectionPhotos();
    	ph4.setCollection(coll);
    	ph4.setPhoto(p2);
    	collectionPhRepository.save(ph4);

    	CollectionPhotos ph5 = new CollectionPhotos();
    	ph5.setCollection(coll1);
    	ph5.setPhoto(p2);
    	collectionPhRepository.save(ph5);
    	
    	java.util.Collection<Collection> allu =collectionRepository.findAllByUserIdAndStatusOrderByHeadline(u.getId(), CollectionStatus.DRAFT);
    	assertEquals(allu.size(),1);
    	assertTrue(allu.contains(coll2));
    	
    	java.util.Collection<Collection> alld =collectionRepository.findAllByUserIdAndStatusOrderByHeadline(u_demo.getId(), CollectionStatus.SHARED);
    	assertEquals(alld.size(),2);
    	assertTrue(alld.contains(coll));
    	assertTrue(alld.contains(coll1));
    	
    	List<Collection> mycoll = collectionRepository.findInvitedTo(u);
    	assertEquals(mycoll.size(),2);
    	assertTrue(mycoll.contains(coll));
    	assertTrue(mycoll.contains(coll1));

        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/collections/statistics");
        resp.prettyPrint();

        resp
        .then()
        .statusCode(HttpStatus.SC_OK);

        collectionPhRepository.delete(ph);
        collectionPhRepository.delete(ph1);
        collectionPhRepository.delete(ph2);
        collectionPhRepository.delete(ph3);
        collectionPhRepository.delete(ph4);
        collectionPhRepository.delete(ph5);

        
        collectionRepository.delete(Arrays.asList(new Collection[] {coll, coll1, coll2}));
        
        photoRepository.delete(p0);
        photoRepository.delete(p1);
        photoRepository.delete(p2);
        photoRepository.delete(p3);
        

        RestAssured.port = port;
        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));
        
    	return;
    }

    
    @Test
    public void updateCollection() {

    	demo = repository.findOne("abc-edcv");
    	
        assertEquals(collectionRepository.findAllByUserId(demo.getId()).size(),1);
        
        collection = collectionRepository.findAllByUserId(demo.getId()).iterator().next();
    	
        Collection col  = collectionRepository.findOne(collection.getId());
        col.setHeadline("My new headline");
        col.setStatus(CollectionStatus.SHARED);

        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(col).when().put("/collections/"+col.getId());
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("headline", Matchers.is("My new headline"))
        .body("status", Matchers.is(CollectionStatus.SHARED.name()));
        //.body("email", Matchers.is("greatjohn@john.com"));
        return;
    }
}
