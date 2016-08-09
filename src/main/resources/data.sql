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
  null,null,'false',0,0,'https://s3-us-west-1.amazonaws.com/becuz/images/frames/zep.png',
  0,0,null,'http://frames.becuz.co/framesvc/frames/zep/',
  'https://s3-us-west-1.amazonaws.com/becuz/images/photo_windows/zep_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f06'
    );

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f0a7','2016-07-06 04:46:56.733',null,
  null,null,'false',0,0,'https://s3-us-west-1.amazonaws.com/becuz/images/frames/bus.png',
  0,0,null,'http://frames.becuz.co/framesvc/frames/bus/',
  'https://s3-us-west-1.amazonaws.com/becuz/images/photo_windows/bus_window.png'
WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f0a7'
    );

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  SELECT '4684db9b-c723-4fac-93bc-a9948019f0a8','2016-07-06 04:46:56.733',null,
  null,null,'true',0,0,
  'https://s3-us-west-1.amazonaws.com/becuz/images/frames/goldframe.png',0,0,null,
  'http://frames.becuz.co/framesvc/frames/goldframe/',
  'https://s3-us-west-1.amazonaws.com/becuz/images/photo_windows/golden_window.png'
  WHERE
    NOT EXISTS (
        SELECT id FROM frames WHERE id = '4684db9b-c723-4fac-93bc-a9948019f0a8'
    );

    update frames set preview_url = 'http://s3-us-west-1.amazonaws.com/becuz-frames/preview-frames/goldframe.png',
    						  url ='http://s3-us-west-1.amazonaws.com/becuz-frames/frames/golden/index.html',
    					window_url ='http://s3-us-west-1.amazonaws.com/becuz-frames/photo-windows/golden_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a8';					
    				

    update frames set preview_url = 'http://s3-us-west-1.amazonaws.com/becuz-frames/preview-frames/bus.png',
    						  url ='http://s3-us-west-1.amazonaws.com/becuz-frames/frames/bus/index.html',
    					window_url ='http://s3-us-west-1.amazonaws.com/becuz-frames/photo-windows/bus_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a7';					

    update frames set preview_url = 'http://s3-us-west-1.amazonaws.com/becuz-frames/preview-frames/zep.png',
    						  url ='http://s3-us-west-1.amazonaws.com/becuz-frames/frames/zep/index.html',
    					window_url ='http://s3-us-west-1.amazonaws.com/becuz-frames/photo-windows/zep_window.png'
    where id = '4684db9b-c723-4fac-93bc-a9948019f0a7';					
