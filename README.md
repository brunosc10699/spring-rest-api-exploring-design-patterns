# Exploring Design Patterns

First of all, I wanna thanks to [MSc Venilton Falvo Jr](https://linkedin.com/in/falvojr), Tech Lead at DIO (Digital Innovation One) for his classes about Design Patterns. He's been contributed a lot to my growth in software development.

*If you also wanna have free classes with him and other DIO's IT Experts, hit this link: https://lnkd.in/e2f-aeNX*

This repository contains a REST API in which I explore the following design patterns:

	- Strategy
	- Singleton
	- Facade
	- Builder
	- DTO (Data Transfer Object)

## Product Registration

Here is the conceptual model:

![class-diagram.jpg](https://raw.githubusercontent.com/brunosc10699/spring-rest-api-exploring-design-patterns/main/.github/images/class-diagram.jpg)

Here, you see how the objects are instantiated in memory:

![objects-instantiated-in-memory.jpg](https://raw.githubusercontent.com/brunosc10699/spring-rest-api-exploring-design-patterns/main/.github/images/objects-instantiated-in-memory.jpg)

Gradle project dependencies:

```
   implementation 'org.springframework.boot:spring-boot-starter-actuator'
   implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
   implementation 'org.springframework.boot:spring-boot-starter-security'
   implementation 'org.springframework.boot:spring-boot-starter-validation'
   implementation 'org.springframework.boot:spring-boot-starter-web'
   implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
   implementation group: 'org.springdoc', name: 'springdoc-openapi-ui', version: '1.5.10'
   compileOnly 'org.projectlombok:lombok'
   runtimeOnly 'com.h2database:h2'
   runtimeOnly 'org.postgresql:postgresql'
   annotationProcessor 'org.projectlombok:lombok'
   testImplementation 'org.springframework.boot:spring-boot-starter-test'
   testImplementation 'org.springframework.security:spring-security-test'
   testImplementation group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: '5.7.2'
   testImplementation group: 'org.mockito', name: 'mockito-core', version: '3.11.0'
   testImplementation group: 'org.hamcrest', name: 'hamcrest-all', version: '1.3'
```

Link to the "Brazilian Home Appliances' Energy Consumption API" repository: https://github.com/brunosc10699/rest-api-spring-react-happliance

*Do you want to study software development for free? Hit this link: https://lnkd.in/e2f-aeNX*
