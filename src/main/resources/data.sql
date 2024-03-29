INSERT INTO users
    (id, email, password_hash, role, created)
SELECT 'abc-edcv','demo@quantlance.com', '$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C', 'ADMIN', CURRENT_TIMESTAMP
WHERE
    NOT EXISTS (
        SELECT id FROM users WHERE id = 'abc-edcv'
    );

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f06','2016-07-06 04:46:56.733',null,  
  null,null,'false',0,0,'http://my.becuz.co/preview-frames/zep.jpg',
  6,0,null,'http://my.becuz.co/frames/zep/index.html',
  'http://my.becuz.co/photo-windows/zep_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f06'
    );

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f0a7','2016-07-06 04:46:56.733',null,
  null,null,'false',0,0,'http://my.becuz.co/preview-frames/bus.jpg',
  4,0,null,'http://my.becuz.co/frames/bus/index.html',
  'http://my.becuz.co/photo-windows/bus_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f0a7'
    );

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f0a8','2016-07-06 04:46:56.733',null,
  null,null,'true',0,0,
  'http://my.becuz.co/preview-frames/goldframe.jpg',1,0,null,
  'http://my.becuz.co/frames/goldframe/index.html',
  'http://my.becuz.co/photo-windows/golden_window.png'
  WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f0a8'
    );
    
Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f09','2016-07-06 04:46:56.733',null,  
  null,null,'false',0,0,'http://my.becuz.co/preview-frames/sub.jpg',
  3,0,null,'http://my.becuz.co/frames/sub/index.html',
  'http://my.becuz.co/photo-windows/sub_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f09'
    );
    
Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f0a','2016-07-06 04:46:56.733',null,  
  null,null,'false',0,0,'http://my.becuz.co/preview-frames/instant.jpg',
  7,0,null,'http://my.becuz.co/frames/instant/index.html',
  'http://my.becuz.co/photo-windows/instant_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f0a'
    );
    
Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f1b','2016-07-06 04:46:56.733',null,  
  null,null,'false',0,0,'http://my.becuz.co/preview-frames/clouds.jpg',
  2,0,null,'http://my.becuz.co/frames/clouds/index.html',
  'http://my.becuz.co/photo-windows/clouds_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f1b'
    );
    
Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f2b','2016-07-06 04:46:56.733',null,  
  null,null,'false',0,0,'http://my.becuz.co/preview-frames/vintagetv.jpg',
  5,0,null,'http://my.becuz.co/frames/vintagetv/index.html',
  'http://my.becuz.co/photo-windows/vintagetv_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f2b'
    );
    
    update frames set preview_url = 'http://my.becuz.co/preview-frames/vintagetv.jpg',
    						  url ='http://my.becuz.co/frames/vintagetv/index.html',
    					window_url ='http://my.becuz.co/photo-windows/vintagetv_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f2b';
    
    update frames set preview_url = 'http://my.becuz.co/preview-frames/clouds.jpg',
    						  url ='http://my.becuz.co/frames/clouds/index.html',
    					window_url ='http://my.becuz.co/photo-windows/clouds_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f1b';
    
    
    update frames set preview_url = 'http://my.becuz.co/preview-frames/instant.jpg',
    						  url ='http://my.becuz.co/frames/instant/index.html',
    					window_url ='http://my.becuz.co/photo-windows/instant_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a';

	update frames set preview_url = 'http://my.becuz.co/preview-frames/sub.jpg',
    						  url ='http://my.becuz.co/frames/sub/index.html',
    					window_url ='http://my.becuz.co/photo-windows/sub_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f09';

    update frames set preview_url = 'http://my.becuz.co/preview-frames/goldframe.jpg',
    						  url ='http://my.becuz.co/frames/golden/index.html',
    					window_url ='http://my.becuz.co/photo-windows/golden_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a8';					
    				

    update frames set preview_url = 'http://my.becuz.co/preview-frames/bus.jpg',
    						  url ='http://my.becuz.co/frames/bus/index.html',
    					window_url ='http://my.becuz.co/photo-windows/bus_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a7';					

    update frames set preview_url = 'http://my.becuz.co/preview-frames/zep.jpg',
    						  url ='http://my.becuz.co/frames/zep/index.html',
    					window_url ='http://my.becuz.co/photo-windows/zep_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f06';					

    update frames set priority = 5
    where id = '4684db9b-c723-4fac-93bc-a9948019f2b';

    update frames set priority = 2
    where id = '4684db9b-c723-4fac-93bc-a9948019f1b';

    update frames set priority = 7
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a';

    update frames set priority = 3
    where id = '4684db9b-c723-4fac-93bc-a9948019f09';

    update frames set priority = 1
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a8';

    update frames set priority = 4
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a7';

    update frames set priority = 6
    where id = '4684db9b-c723-4fac-93bc-a9948019f06';
