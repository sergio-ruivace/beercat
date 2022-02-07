# beercat

This application manages a beer catalogue from different manufacturers (providers). Beer consumers
can look up the beer catalogue in order to inspire future purchases.

# Getting Started

Command to build project/image 

	mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=beercat/latest -Dexcludedevtools=true

Command to build project/image remote dev

	mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=beercat/latest -Dexcludedevtools=false

Command to run project 

	docker run -d -p 8080:8080 -t beercat/latest
	
Swagger API URL
	
	http://localhost:8080/swagger-ui/index.html


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#using-boot-devtools)