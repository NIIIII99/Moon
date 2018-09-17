---
layout: post
title: "Spring boot Admin 적용기(삽질기)"
date: 2018-09-17
excerpt: "springboot application 을 위한 최고의 monitoring UI"
tags: [spring-boot-admin, monitoring, springboot]
comments: true
---

## Springboot Admin이란?
https://github.com/codecentric/spring-boot-admin

* springboot는 /actuator Endpoint를 제공하여 프로그램의 모니터링을 가능하게 함.
* 근데 이 정보는 json 형태로 가독성이 너무나도 떨어짐.
* 그런 점을 보완하기 위해 나온게 Springboot Admin UI! 일종의 대시보드 기능으로 보면 됨

### 주요 기능
* actuator에서 제공하는 정보는 다 볼 수 있음
* Thread dump, log file 등등

### 그외 기능
* endpoint를 사용자가 원하는 곳에 추가할 수 있음
(어노테이션 추가로 어떤 method든지 모니터링 가능)
* 로그 레벨을 변경하여 재기동 없이 바로 확인 가능
* /actuator 대신 다른 base URL 사용가능 (포트도 변경 가능)

### 적용하려면
#### spring-boot-admin-server 설정
1. pom.xml 작성
* spring.start.io 에서 빈 springboot app 생성. 후에 아래 dependency를 추가한다. 거기서 spring-boot-admin-server와 web를 추가 후 만들면 dependecy version이 잘 안맞는지 오류가 난다.... 꼭 나중에 따로 추가해주자.
```xml
		<dependency>
		    <groupId>de.codecentric</groupId>
		    <artifactId>spring-boot-admin-starter-server</artifactId>
		    <version>2.0.2</version>
		</dependency>
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-web</artifactId>
		</dependency>
```
2. application.yml 작성
```
server:
  port: 8081 # application과 겹치지 않는 port로..
```
3. Application.java 작성
```java
@SpringBootApplication
@EnableAdminServer // EnableAdminServer 어노테이션으로 admin server 동작
public class SpringAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAdminApplication.class, args);
	}
}
```

#### spring-boot-admin-client 설정 
