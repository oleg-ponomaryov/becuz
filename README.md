Pre-requisites: install maven 3.x

How to run:
mvn spring-boot:run

How to build:
mvn clean install

About the project
-------------------

1. Spring boot CRUD web project with Spring security
2. Initial login: demo@quantlance.com with password:demo
3. Role based security implemented
4. Two roles (ADMIN and USER) are implemented
5. CRUD for Users and Images
6. CSRF based communication is enabled between UI forms and server

Start Postgres
------------------

pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log start


Curl for login
------------------
 curl -i -H "Content-type: application/x-www-form-urlencoded" -X POST http://localhost:8080/login -d "email=demo@quantlance.com&password=demo"
 
 curl --cookie cookie -i -H "Content-type: application/x-www-form-urlencoded" -X POST http://localhost:8080/login -d "email=demo@quantlance.com&password=demo&_csrf=d3daab9b-a480-4d18-81cf-181c81ec2db8"
 
 To get  _csrf
 
 curl --cookie-jar cookie -L http://localhost:8080/login  | grep csrf
 
 
 AWS Credentials
 ------------------
export AWS_ACCESS_KEY=<your-aws-access-key-id>
export AWS_SECRET_KEY=<your-aws-secret-key> 
 
 Initial Images
 ------------------
For initial images (first in series) set S3 object's metadata to:

 x-amz-meta-primary:true
 
 