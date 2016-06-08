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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import co.becuz.SpringBootWebApplication;
import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.repositories.UserRepository;

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
    UserRepository repository;

    User john;
    User oleg;
    User mary;
    User demo;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        demo = new User();
        demo.setRole(Role.ADMIN);
        demo.setUsername("demo");
        demo.setPasswordHash("$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        demo.setEmail("demo@quantlance.com");
        demo.setPhotoUrl("http://");
    	
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

        repository.deleteAll();
        repository.save(Arrays.asList(john, oleg, mary, demo));

        RestAssured.port = port;
        RestAssured.authentication = RestAssured.form("demo@quantlance.com", "demo", new FormAuthConfig("/login", "email", "password"));
    }

    @Test
    public void createUser() {
    	User jane = new User();
        jane.setRole(Role.USER);
        jane.setUsername("jane");
        jane.setPasswordHash("123");
        jane.setEmail("jane@jane.com");
        jane.setPhotoUrl("http://");

        assertEquals(repository.findAllByEmail("jane@jane.com").size(),0);

        Response resp = RestAssured.given().contentType("application/json\r\n").with().body(jane).when().post("/users");
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
        and().body("email", hasItems("demo@quantlance.com", "john@john.com","mary@mary.com","oleg@oleg.com"));
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
    public void getTime() {
        Response resp = RestAssured.given().contentType("application/json\r\n").with().when().get("/time");
        resp.prettyPrint();
        
        resp
        .then()
        .statusCode(HttpStatus.SC_OK);

        return;
    }
}