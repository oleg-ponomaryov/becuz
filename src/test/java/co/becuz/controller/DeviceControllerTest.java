package co.becuz.controller;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
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
import co.becuz.domain.Device;
import co.becuz.domain.Frame;
import co.becuz.domain.Photo;
import co.becuz.domain.User;
import co.becuz.domain.enums.DeviceType;
import co.becuz.domain.enums.Role;
import co.becuz.dto.PhotoDTO;
import co.becuz.dto.PhotoUploadRequestDTO;
import co.becuz.repositories.DeviceRepository;
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
public class DeviceControllerTest {

    @Autowired
    UserService service;

    @Autowired
    DeviceRepository deviceRepository;
    
    private static User ndemo;

    @Value("${local.server.port}")
    int port;

    private static boolean setUpIsDone = false;
    
    @Before
    public void setUp() {

    	if (setUpIsDone) {
    		return;
    	}

        RestAssured.port = port;
        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));
        setUpIsDone = true;
    }

    @After
    public void tearDown() {
    	deviceRepository.deleteAll();
    }
    
    @Test
    public void createDevice() throws JSONException{
    	
    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo100");
        u.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo100@quantlance.com");
        u.setPhotoUrl("http://");
        service.save(u);

        Device d = new Device();
        d.setDescription("My descr");
        d.setDeviceType(DeviceType.IOS);
        d.setToken("12121212");
        d.setUser(u);
        
    	Gson gson = new GsonBuilder().create();
    	System.out.println("---------------->>"+gson.toJson(d));
    	
        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(d).when().post("/devices");
        resp.prettyPrint();
        
        assertEquals(deviceRepository.findAllByUser(u).size(),1);
        assertEquals(deviceRepository.findAllByUser(u).iterator().next().getToken(),"12121212");
        
       return;
    }
    
    @Test
    public void getDevices() {
    	
        User demo = service.getUserById("abc-edcv");
    	
        Device d = new Device();
        d.setDescription("My descr33");
        d.setDeviceType(DeviceType.IOS);
        d.setToken("121212123333");
        d.setUser(demo);
        deviceRepository.save(d);

        assertEquals(deviceRepository.findAllByUser(demo).size(),1);
        
        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/devices");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("token[0]", Matchers.is("121212123333"));
        return;
    }
}
