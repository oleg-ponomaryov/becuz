INSERT INTO users (id, email, password_hash, role, created)
VALUES ('abc-edcv','demo@quantlance.com', '$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C', 'ADMIN', CURRENT_TIMESTAMP );

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  values ('4684db9b-c723-4fac-93bc-a9948019f06','2016-07-06 04:46:56.733',null,
  null,null,'false',0,0,'http://s3-us-west-1.amazonaws.com/becuz-frames/preview-frames/zep.png',
  0,0,null,'http://s3-us-west-1.amazonaws.com/becuz-frames/frames/zep/index.html',
  'http://s3-us-west-1.amazonaws.com/becuz-frames/photo-windows/zep_window.png');

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  values ('4684db9b-c723-4fac-93bc-a9948019f0a7','2016-07-06 04:46:56.733',null,
  null,null,'false',0,0,'http://s3-us-west-1.amazonaws.com/becuz-frames/preview-frames/bus.png',
  0,0,null,'http://s3-us-west-1.amazonaws.com/becuz-frames/frames/bus/index.html,
  'http://s3-us-west-1.amazonaws.com/becuz-frames/photo-windows/bus_window.png');

Insert into frames (id,created,child_id,description,display_type,isdefault,photo_height,photo_width,
  preview_url,priority,status,updated,url,window_url) 
  values ('4684db9b-c723-4fac-93bc-a9948019f0a8','2016-07-06 04:46:56.733',null,
  null,null,'true',0,0,
  'http://s3-us-west-1.amazonaws.com/becuz-frames/preview-frames/goldframe.png',0,0,null,
  'http://s3-us-west-1.amazonaws.com/becuz-frames/frames/golden/index.html',
  'http://s3-us-west-1.amazonaws.com/becuz-frames/photo-windows/golden_window.png');
