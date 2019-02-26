# ag-service
A RESTful service for Apartment Guide application.  

Project is built in Spring Framework utilizing Spring Boot and Spring Data Rest.  

Current version (still a work in progress) is running on AWS EC2 and MySQL database on AWS RDS.

## API calls
Currently, it supports query by `zipCode`,`bedNum`,`maxPrice`,`name` either alone or in combine:  

`http://3.88.103.11:80/ag-api/apartments/search?zipCode=95134`  
`http://3.88.103.11:80/ag-api/apartments/search?name=Avalon&maxPrice=3000`

