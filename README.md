# beercat

This application manages a beer catalogue from different manufacturers (providers). Beer consumers
can look up the beer catalogue in order to inspire future purchases.

# Getting Started

Command to build project/image 

	mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=beercat/latest -Dexcludedevtools=true

Command to build project/image remote development

	mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=beercat/latest -Dexcludedevtools=false

Command to run project 

	docker run -d -p 8080:8080 -t beercat/latest
	
Swagger API URL
	
	http://localhost:8080/swagger-ui/index.html

H2 Database Console

	http://localhost:8080/h2-console

# technical decisions

1)There are two ways to pack the project, with and without devtools. With devtools is exclusively to development environment. This library can be used to hot reload the project inside the container. This feature can accelerate the development phase.
2)I am using springdoc-openapi to generate the swagger api documentation
3)The library lombok is used to reduce boilerplate code.
4)The h2 is used as memory database. It is light, easy to use and simplifies the development.
5)If the beer types changes a lot, it is better use a database table. This approach will give some flexibility. But I chose an enum to simplify the development and I think that is difficult to create new type of beer.
6)In this project, I used the spring-boot to build the docker image. It can be accomplished also with dockerfile.
7)Some improvements could be developed: create a new layer to deal with business rules and transactions; insert initial images in database; create more tests; use beans validations.
8)The images could be stored in Blob or File storage.



### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#using-boot-devtools)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Declarative REST calls with Spring Cloud OpenFeign sample](https://github.com/spring-cloud-samples/feign-eureka)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-security)
