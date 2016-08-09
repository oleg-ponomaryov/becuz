package co.becuz.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Date;

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
import co.becuz.domain.CollectionPhotos;
import co.becuz.domain.Frame;
import co.becuz.domain.Photo;
import co.becuz.domain.User;
import co.becuz.domain.enums.Role;
import co.becuz.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebApplication.class)
@TestPropertySource(locations="classpath:application-test.properties")
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class DataIntegrityTest {
	
    @Autowired
    UserService service;

    @Autowired
    FrameRepository frameRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    CollectionPhotosRepository collectionPhRepository;
    
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
        ndemo.setUsername("ndemo200");
        ndemo.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        ndemo.setEmail("ndemo200@quantlance.com");
        ndemo.setPhotoUrl("http://");
        
        service.save(ndemo);
        photoRepository.deleteAll();
        collectionRepository.deleteAll();
        
        setUpIsDone = true;
    }

    @Test
    public void testPhotosIntegrity() {
    
    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo1");
        u.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo1@quantlance.com");
        u.setPhotoUrl("http://");
        service.save(u);

    	Photo p0 = new Photo();
    	p0.setBucket("becuz0");
    	p0.setCaption("My caption0");
    	p0.setMd5Digest("eeeeee1");
    	p0.setOriginalKey("/upload/file");
    	p0.setOwner(u);
    	p0.setUploadedDate(new Date());
    	photoRepository.save(p0);

    	Photo p1 = new Photo();
    	p1.setBucket("becuz2");
    	p1.setCaption("My caption2");
    	p1.setMd5Digest("eeeeee2");
    	p1.setOriginalKey("/upload/file");
    	p1.setOwner(u);
    	p1.setUploadedDate(new Date());
    	photoRepository.save(p1);
    	
    	Photo p = new Photo();
    	p.setBucket("becuz1");
    	p.setCaption("My caption1");
    	p.setMd5Digest("eeeeee3");
    	p.setOriginalKey("/upload/file1");
    	p.setOwner(ndemo);
    	p.setUploadedDate(new Date());
    	photoRepository.save(p);
    	
    	assertEquals(3, photoRepository.findAll().size());
    	service.delete(ndemo.getId());
    	assertEquals(2, photoRepository.findAll().size());
    	Collection<Photo> photos =  photoRepository.findAll();
    	for (Photo ph : photos) {
    		if (! ph.getId().equals(p0.getId()) && ! ph.getId().equals(p1.getId())) {
    			fail("Unexpected Id");
    		}
    	}
    }
    
    @Test
    public void testCollectionsIntegrity() {
    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo300");
        u.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo300@quantlance.com");
        u.setPhotoUrl("http://");
        service.save(u);
    	
    	Frame frame = new Frame();
    	frame.setUrl("http://becuz.net");
    	frame = frameRepository.save(frame);

    	Frame frame1 = new Frame();
    	frame1.setUrl("http://becuz.net");
    	frame1 = frameRepository.save(frame1);
    	
    	co.becuz.domain.Collection c = new co.becuz.domain.Collection();
    	c.setFrame(frame);
    	c.setHeadline("My headline");
    	c.setUser(u);
    	collectionRepository.save(c);
    	
    	co.becuz.domain.Collection c1 = new co.becuz.domain.Collection();
    	c1.setFrame(frame1);
    	c1.setHeadline("My headline1");
    	c1.setUser(u);
    	collectionRepository.save(c1);

    	co.becuz.domain.Collection c2 = new co.becuz.domain.Collection();
    	c2.setFrame(frame);
    	c2.setHeadline("My headline2");
    	c2.setUser(u);
    	collectionRepository.save(c2);

    	assertEquals(3, collectionRepository.findAll().size());
    	frameRepository.delete(frame.getId());
    	assertEquals(1, collectionRepository.findAll().size());
    	
    	assertEquals(c1.getId(), collectionRepository.findAll().get(0).getId());
    	service.delete(u.getId());
    	assertEquals(0, collectionRepository.findAll().size());
    }
    
    @Test
    public void testCollectionsPhotosIntegrity() {

    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo300");
        u.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo300@quantlance.com");
        u.setPhotoUrl("http://");
        service.save(u);
    	
    	Frame frame = new Frame();
    	frame.setUrl("http://becuz.net");
    	frame = frameRepository.save(frame);

    	co.becuz.domain.Collection c = new co.becuz.domain.Collection();
    	c.setFrame(frame);
    	c.setHeadline("My headline");
    	c.setUser(u);
    	collectionRepository.save(c);
    	
    	co.becuz.domain.Collection c1 = new co.becuz.domain.Collection();
    	c1.setFrame(frame);
    	c1.setHeadline("My headline1");
    	c1.setUser(u);
    	collectionRepository.save(c1);

    	Photo p0 = new Photo();
    	p0.setBucket("becuz0");
    	p0.setCaption("My caption100");
    	p0.setMd5Digest("eeeeee100");
    	p0.setOriginalKey("/upload/file");
    	p0.setOwner(u);
    	p0.setUploadedDate(new Date());
    	photoRepository.save(p0);

    	Photo p1 = new Photo();
    	p1.setBucket("becuz2");
    	p1.setCaption("My caption200");
    	p1.setMd5Digest("eeeeee200");
    	p1.setOriginalKey("/upload/file");
    	p1.setOwner(u);
    	p1.setUploadedDate(new Date());
    	photoRepository.save(p1);

    	Photo p2 = new Photo();
    	p2.setBucket("becuz0");
    	p2.setCaption("My caption1002");
    	p2.setMd5Digest("eeeeee1002");
    	p2.setOriginalKey("/upload/file");
    	p2.setOwner(u);
    	p2.setUploadedDate(new Date());
    	photoRepository.save(p2);

    	Photo p3 = new Photo();
    	p3.setBucket("becuz3");
    	p3.setCaption("My caption2003");
    	p3.setMd5Digest("eeeeee2003");
    	p3.setOriginalKey("/upload/file");
    	p3.setOwner(u);
    	p3.setUploadedDate(new Date());
    	photoRepository.save(p3);

    	
    	
    	CollectionPhotos ph = new CollectionPhotos();
    	ph.setCollection(c);
    	ph.setPhoto(p0);
    	collectionPhRepository.save(ph);
    	
    	CollectionPhotos ph1 = new CollectionPhotos();
    	ph1.setCollection(c1);
    	ph1.setPhoto(p2);
    	collectionPhRepository.save(ph1);

    	CollectionPhotos ph2 = new CollectionPhotos();
    	ph2.setCollection(c);
    	ph2.setPhoto(p1);
    	collectionPhRepository.save(ph2);
    	
    	CollectionPhotos ph3 = new CollectionPhotos();
    	ph3.setCollection(c1);
    	ph3.setPhoto(p3);
    	collectionPhRepository.save(ph3);
    	
    	assertEquals(4, collectionPhRepository.findAll().size());
    	collectionRepository.delete(c.getId());
    	assertEquals(2, collectionPhRepository.findAll().size());
    	Collection<CollectionPhotos> photos =  collectionPhRepository.findAll();
    	for (CollectionPhotos cph : photos) {
    		if (! cph.getId().equals(ph3.getId()) && ! cph.getId().equals(ph1.getId())) {
    			fail("Unexpected Id");
    		}
    	}
    	
    	assertNull(photoRepository.findOne(p0.getId()));
    	photoRepository.delete(p3.getId());
    	assertEquals(1, collectionPhRepository.findAll().size());
    	assertEquals(ph1.getId(), collectionPhRepository.findAll().get(0).getId());
    	service.delete(u.getId());
    	assertEquals(0, collectionPhRepository.findAll().size());
    }
    

    @Test
    public void testDeleteCollectionIntegrity() {

    	User u = new User();
        u.setRole(Role.ADMIN);
        u.setUsername("ndemo3001");
        u.setPasswordHash("n$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C");
        u.setEmail("ndemo3001@quantlance.com");
        u.setPhotoUrl("http://");
        service.save(u);
    	
    	Frame frame = new Frame();
    	frame.setUrl("http://becuz.net");
    	frame = frameRepository.save(frame);

    	co.becuz.domain.Collection c = new co.becuz.domain.Collection();
    	c.setFrame(frame);
    	c.setHeadline("My headline");
    	c.setUser(u);
    	collectionRepository.save(c);

    	Photo p0 = new Photo();
    	p0.setBucket("becuz0");
    	p0.setCaption("My caption100");
    	p0.setMd5Digest("eeeeee100");
    	p0.setOriginalKey("/upload/file");
    	p0.setOwner(u);
    	p0.setUploadedDate(new Date());
    	photoRepository.save(p0);

    	Photo p1 = new Photo();
    	p1.setBucket("becuz2");
    	p1.setCaption("My caption200");
    	p1.setMd5Digest("eeeeee200");
    	p1.setOriginalKey("/upload/file");
    	p1.setOwner(u);
    	p1.setUploadedDate(new Date());
    	photoRepository.save(p1);
    	
    	CollectionPhotos ph = new CollectionPhotos();
    	ph.setCollection(c);
    	ph.setPhoto(p0);
    	collectionPhRepository.save(ph);
    	

    	CollectionPhotos ph2 = new CollectionPhotos();
    	ph2.setCollection(c);
    	ph2.setPhoto(p1);
    	collectionPhRepository.save(ph2);

    	assertEquals(2, photoRepository.findAll().size());
    	assertEquals(2, collectionPhRepository.findAll().size());
    	collectionRepository.delete(c.getId());
    	assertEquals(0, collectionPhRepository.findAll().size());
    	assertEquals(0, photoRepository.findAll().size());

    }
}
