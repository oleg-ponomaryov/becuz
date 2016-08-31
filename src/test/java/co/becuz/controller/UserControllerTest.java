package co.becuz.controller;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import co.becuz.SpringBootWebApplication;
import co.becuz.domain.Collection;
import co.becuz.domain.Frame;
import co.becuz.domain.Photo;
import co.becuz.domain.User;
import co.becuz.domain.enums.FrameOwner;
import co.becuz.domain.enums.Role;
import co.becuz.repositories.CollectionRepository;
import co.becuz.repositories.FrameRepository;
import co.becuz.repositories.PhotoRepository;
import co.becuz.repositories.UserRepository;
import co.becuz.services.EncryptionService;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class UserControllerTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EncryptionService encryptionService;
    
    @Autowired
    private FrameRepository frameRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private PhotoRepository photoRepository;
    
    User john;
    User oleg;
    User mary;
    User demo;

    @Value("${local.server.port}")
    private int port;

    @Value("${app.bundle.id}")
    private String appId;
    
    private static boolean setUpIsDone = false;

	private static Logger LOGGER = LoggerFactory
			.getLogger(UserControllerTest.class);

    
    @Before
    public void setUp() {
    	if (setUpIsDone) {
    		return;
    	}
        demo = repository.findOne("abc-edcv");
    	
        john = new User();
        john.setRole(Role.ADMIN);
        john.setUsername("john");
        john.setPasswordHash(new BCryptPasswordEncoder().encode("123"));
        john.setEmail("john@john.com");
        john.setPhotoUrl("http://");
        
        oleg = new User();
        oleg.setRole(Role.USER);
        oleg.setUsername("oleg");
        oleg.setPasswordHash(new BCryptPasswordEncoder().encode("123"));
        oleg.setEmail("oleg@oleg.com");
        oleg.setPhotoUrl("http://");
        
        mary = new User();
        mary.setRole(Role.USER);
        mary.setUsername("mary");
        mary.setPasswordHash(new BCryptPasswordEncoder().encode("123"));
        mary.setEmail("mary@mary.com");
        mary.setPhotoUrl("http://");

        repository.save(Arrays.asList(john, oleg, mary));

        RestAssured.port = port;
        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));
        setUpIsDone = true;
    }

    @Test
    public void createSelfUser() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    	String iv = "1234567890123456";
    	
    	String dto = "{\"user\":{\"email\":\""+encryptionService.encrypt("oleg@quantlance.com", iv.getBytes("UTF-8"))+"\",\"password\":\""+encryptionService.encrypt("demo", iv.getBytes("UTF-8"))+"\"},\"appId\":\""+encryptionService.encrypt(appId, iv.getBytes("UTF-8"))+"\",\"iv\":\"1234567890123456\"}";

    	LOGGER.info(dto);
    	
        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(dto).when().post("/user/create");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK);

        
    	dto = "{\"user\":{\"email\":\""+encryptionService.encrypt("oleg@quantlance.com", iv.getBytes("UTF-8"))+"\",\"password\":\""+encryptionService.encrypt("demo", iv.getBytes("UTF-8"))+"\"},\"appId\":\""+encryptionService.encrypt(appId, iv.getBytes("UTF-8"))+"\",\"iv\":\"1234567890123456\"}";

    	LOGGER.info(dto);
    	
        resp = RestAssured.given().contentType("application/json\r\n").with().body(dto).when().post("/user/create");
        resp.prettyPrint();

        resp
        .then()
        .statusCode(HttpStatus.SC_CONFLICT);
        
    	return;
    }
    
    @Test
    public void createUser() {
        String j = "{\"email\":\"jane@jane.com\",\"role\":\"USER\",\"password\":\"123\",\"username\":\"jane\",\"photoUrl\":\"http://\"}";
        assertEquals(repository.findAllByEmail("jane@jane.com").size(),0);

        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(j).when().post("/users");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("username", Matchers.is("jane"))
        .body("email", Matchers.is("jane@jane.com"));

        assertEquals(repository.findAllByEmail("jane@jane.com").size(),1);
        
        return;
    }
    
    @Test
    public void getAllUser() {
        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/users");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK).
        and().body("email", hasItems("demo@quantlance.com", "greatjohn@john.com","mary@mary.com","oleg@oleg.com"));
        return;
    }

    @Test
    public void deleteUser() {

        assertEquals(repository.findAllByEmail("oleg@oleg.com").size(),1);
        User oleg = repository.findAllByEmail("oleg@oleg.com").iterator().next();
    	
        RestAssured.expect().statusCode( 200 )
    	.when().delete( "/users/" + oleg.getId());

        assertEquals(repository.findAllByEmail("oleg@oleg.com").size(),0);
        return;
    }

    
    @Test
    public void updateUser() {

        assertEquals(repository.findAllByEmail("greatjohn@john.com").size(),0);
    	
        assertEquals(repository.findAllByEmail("john@john.com").size(),1);
        User john = repository.findAllByEmail("john@john.com").iterator().next();
        john.setUsername("greatjohn");
        john.setEmail("greatjohn@john.com");

        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(john).when().put("/users/"+john.getId());
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("username", Matchers.is("greatjohn"))
        .body("email", Matchers.is("greatjohn@john.com"));

        assertEquals(repository.findAllByEmail("greatjohn@john.com").size(),1);
        
        return;
    }
    
    @Test
    public void getUser() {
        assertEquals(repository.findAllByEmail("mary@mary.com").size(), 1);
        User mary = repository.findAllByEmail("mary@mary.com").iterator().next();

        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/users/"+mary.getId());
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("username", Matchers.is("mary"))
        .body("email", Matchers.is("mary@mary.com"));

        return;
    }

    @Test
    public void getUserByEmail() throws UnsupportedEncodingException {
        assertEquals(repository.findAllByEmail("mary@mary.com").size(), 1);
        User mary = repository.findAllByEmail("mary@mary.com").iterator().next();
        assertNotNull(mary);
        
        RestAssured.authentication = RestAssured.form("oleg@oleg.com", "123", new FormAuthConfig("/login", "email", "password"));

        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/user/email/"+URLEncoder.encode(mary.getEmail(), "UTF-8"));
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("username", Matchers.is("mary"))
        .body("email", Matchers.is("mary@mary.com"));

        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));

        return;
    }

    
    @Test
    public void swapUsers() {
    	
        RestAssured.authentication = RestAssured.form("oleg@oleg.com", "123", new FormAuthConfig("/login", "email", "password"));
    	
        assertEquals(repository.findAllByEmail("oleg@oleg.com").size(),1);

       	Frame frame = new Frame();
    	frame.setUrl("http://becuz.net");
    	frame = frameRepository.save(frame);
        
        Collection collection = new Collection();
        collection.setFrame(frame);
        collection.setHeadline("My headline");
        collection.setOwnerType(FrameOwner.OWNER);
        collection.setUser(demo);

        Collection collection1 = new Collection();
        collection1.setFrame(frame);
        collection1.setHeadline("My headline1");
        collection1.setOwnerType(FrameOwner.OWNER);
        collection1.setUser(demo);

        collectionRepository.save(Arrays.asList(collection, collection1));
        
    	Photo p0 = new Photo();
    	p0.setBucket("becuz0");
    	p0.setCaption("My caption0");
    	p0.setMd5Digest("eeeeee0");
    	p0.setOriginalKey("/upload/file");
    	p0.setOwner(demo);
    	p0.setUploadedDate(new Date());
    	photoRepository.save(p0);

    	Photo p1 = new Photo();
    	p1.setBucket("becuz0");
    	p1.setCaption("My caption1");
    	p1.setMd5Digest("eeeeee1");
    	p1.setOriginalKey("/upload/file");
    	p1.setOwner(demo);
    	p1.setUploadedDate(new Date());
    	photoRepository.save(p1);

    	Photo p2 = new Photo();
    	p2.setBucket("becuz0");
    	p2.setCaption("My caption2");
    	p2.setMd5Digest("eeeeee2");
    	p2.setOriginalKey("/upload/file");
    	p2.setOwner(oleg);
    	p2.setUploadedDate(new Date());
    	photoRepository.save(p2);

    	
        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(oleg.getId()).when().post("/user/swap");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);

    	
        resp = RestAssured.given().contentType("application/json\r\n").with().body(demo.getId()).when().post("/user/swap");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("status", Matchers.is("success"))
        .body("message", Matchers.anything("Assigned 2 photos"))
        .body("message", Matchers.anything("assigned 2 collections"));

        assertEquals(collectionRepository.findOne(collection.getId()).getUser(),oleg);
        assertEquals(collectionRepository.findOne(collection1.getId()).getUser(),oleg);

        assertEquals(photoRepository.findOne(p0.getId()).getOwner(),oleg);
        assertEquals(photoRepository.findOne(p1.getId()).getOwner(),oleg);
        
        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));

        return;
    }
    
    @Test
    public void getTime() {
        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/time");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK);

        return;
    }
}