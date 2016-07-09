INSERT INTO users (id, email, password_hash, role, created)
VALUES ('abc-edcv','demo@quantlance.com', '$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C', 'ADMIN', CURRENT_TIMESTAMP );

insert into frames (id, created, description, isDefault, preview_url, priority, status, url, photo_height, photo_width)
values('4684db9b-c723-4fac-93bc-a9948019f06', '2016-07-06 04:46:56.733',
'Jeff will provide description for this frame.', false,
'http://ec2-54-183-19-16.us-west-1.compute.amazonaws.com:8081/framesvc/images/preview/zep.png',
0, 0, 'http://ec2-54-183-19-16.us-west-1.compute.amazonaws.com:8081/framesvc/frames/zep/',
0, 0);

insert into frames (id, created, description, isDefault, preview_url, priority, status, url, photo_height, photo_width)
values('4684db9b-c723-4fac-93bc-a9948019f0a7', '2016-07-06 04:46:56.733',
'Jeff will provide description for this frame.', false,
'http://ec2-54-183-19-16.us-west-1.compute.amazonaws.com:8081/framesvc/images/preview/bus.png',
0, 0, 'http://ec2-54-183-19-16.us-west-1.compute.amazonaws.com:8081/framesvc/frames/bus/',
0, 0);

insert into frames (id, created, description, isDefault, preview_url, priority, status, url, photo_height, photo_width)
values('4684db9b-c723-4fac-93bc-a9948019f0a8', '2016-07-06 04:46:56.733',
'Jeff will provide description for this frame.', true,
'http://ec2-54-183-19-16.us-west-1.compute.amazonaws.com:8081/framesvc/images/preview/goldframe.png',
0, 0, 'http://ec2-54-183-19-16.us-west-1.compute.amazonaws.com:8081/framesvc/frames/goldframe/',
0, 0);
