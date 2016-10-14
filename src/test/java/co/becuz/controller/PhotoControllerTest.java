package co.becuz.controller;

import java.sql.Date;

import org.apache.http.HttpStatus;
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
import co.becuz.domain.Photo;
import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.dto.PhotoDTO;
import co.becuz.dto.PhotoUploadRequestDTO;
import co.becuz.repositories.FrameRepository;
import co.becuz.repositories.PhotoRepository;
import co.becuz.services.PhotoService;
import co.becuz.services.UserService;

import com.amazonaws.util.json.JSONException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class PhotoControllerTest {

    @Autowired
    UserService service;

    @Autowired
    FrameRepository frameRepository;

    @Autowired
    PhotoService photoService;

    @Autowired
    PhotoRepository photoRepository;
    
    private static User ndemo;

    @Value("${local.server.port}")
    int port;

    private static boolean setUpIsDone = false;
    
    @Before
    public void setUp() {

    	if (setUpIsDone) {
    		return;
    	}

        ndemo = new User();
        ndemo.setRole(Role.ADMIN);
        ndemo.setUsername("ndemo");
        ndemo.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        ndemo.setEmail("ndemo@quantlance.com");
        ndemo.setPhotoUrl("http://");
    	
        service.save(ndemo);
    	
        RestAssured.port = port;
        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));
        setUpIsDone = true;
    }

    @Test
    public void uploadPhoto() throws JSONException{
    	PhotoUploadRequestDTO dto = new PhotoUploadRequestDTO();
    	
    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo100");
        u.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo100@quantlance.com");
        u.setPhotoUrl("http://");
        service.save(u);
    	
    	Frame frame = new Frame();
    	frame.setUrl("http://becuz.net");
    	frame = frameRepository.save(frame);
    	
    	Collection collection = new Collection();
    	collection.setFrame(frame);
    	collection.setHeadline("My headline");
    	collection.setUser(u);
    	
    	dto.setCollection(collection);
    	
    	Gson gson = new GsonBuilder().create();
    	System.out.println("---------------->>"+gson.toJson(dto));
    	
        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(dto).when().post("/photo/upload");
        resp.prettyPrint();
        
       return;
    }
    
    @Test
    public void updatePhoto() {

    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo100");
        u.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo100001@quantlance.com");
        u.setPhotoUrl("http://");
        service.save(u);
    	
    	Photo ph = new Photo();
    	ph.setBucket("bucket");
    	ph.setCaption("My caption");
    	ph.setDescription("My Descr");
    	ph.setMd5Digest("12345");
    	ph.setOwner(u);
    	ph.setOriginalKey("/upload/file");
    	ph.setUploadedDate(new Date(20000L));
    	photoService.save(ph);
    	
    	PhotoDTO phd = new PhotoDTO();
    	phd.setCaption("My new caption");
    	phd.setMd5Digest("54321");
    	phd.setOwner(ndemo);
    	phd.setId(ph.getId());
    	
        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(phd).when().put("/photo/"+phd.getId());
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("caption", Matchers.is("My new caption"))
        .body("md5Digest", Matchers.is("54321"));
        return;
    }
}
