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

## security 없이 적용하려면? 
### spring-boot-admin-server 설정
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

### spring-boot-admin-client 설정 
1. pom.xml 작성
```xml
		<dependency>
		    <groupId>de.codecentric</groupId>
		    <artifactId>spring-boot-admin-starter-client</artifactId>
		    <version>2.0.2</version>
		</dependency>	
```
2. application.yml 수정
```yml
spring:
  boot:
    admin:
      client:
        url: "http://localhost:8081" 
management:
  endpoints:
    web:
      exposure:
         include: "*"
  endpoint:
    health:
      show-details: ALWAYS           
```        

## security 로 적용하려면? 
### spring-boot-admin-server 설정
1. pom.xml 수정 : spring-boot-starter-security dependency 추가
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>	
```
2. SecurityConfig.java 파일 추가 : WebSecurityConfigurerAdapter 를 상속받아서 login, logout 페이지로 접속할수 있게함.
```java
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String adminContextPath;
    public SecurityConfig(AdminServerProperties adminServerProperties) {
    	
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        @formatter:off
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");

        http.authorizeRequests()
                .antMatchers(adminContextPath + "/assets/**").permitAll()
                .antMatchers(adminContextPath + "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                .logout().logoutUrl(adminContextPath + "/logout").and()
                .httpBasic().and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  
                .ignoringAntMatchers(
                        adminContextPath + "/instances",   
                        adminContextPath + "/actuator/**"  
                   ); 
    }
  
}
```
3. application.yml 수정 : 관리자 이름과 비밀번호 설정
```yml
spring:
  security:
    user:
      name: "user"
      password: "password"    
```
### spring-boot-admin-client 설정
1. pom.xml 수정 : spring-boot-starter-security dependency 추가
```xml
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```
2. application.yml 수정 : 관리자 이름과 비밀번호 spirng-boot-admin 과 동일하게 설정
```yml
spring: 
  security:
    user:
      name: "user" # spirng-boot-admin 과 동일해야 함.
      password: "password" # spirng-boot-admin 과 동일해야 함.         
  boot:
    admin:
      client:
        url: "http://localhost:8081" # spirng-boot-admin
        username: ${spring.security.user.name}
        password: ${spring.security.user.password}                   
        instance:
          metadata:
            user.name: ${spring.security.user.name}
            user.password: ${spring.security.user.password} 
```


## spring-boot-admin 활용하기
### logfile 확인하기
* 로그를 console이 아닌 file로 출력하도록 설정(여기에정리) 후 경로만 client쪽 application.yml에 명시하면 자동으로 admin server화면에서 logfile 탭이 생성되면서 logefile을 확인할 수 있다.
```yml
logging:
  file: ./logs/data.log
```

### http trace 확인하기
* http trace 탭 클릭시 정상적으로 동작하지 않는다면, 아래 설정을 client의 application.yml 안에 해주면 된다. 원인은 /actuator/httptrace의  timestamp가 spring boot admin 에서 생각하는 대로 넘어가지 않아서 발생하는 현상일 가능성이 매우 높다. 아래 설정으로 timestamp 형식을 수정해주면 정상적으로 동작한다.
```yml
spring:
  jackson:
    serialization:            
      write-dates-as-timestamps: true
      write-date-timestamps-as-nanoseconds: false   
```
* 이설정이 동작하지 않는 경우 혹시 소스내에 @EnableWebMvc나 WebMVCConfigurerSupport 를 implements한 부분이 있는지 확인 해보길 바란다. 저런 아이들은 스프링부트의 자동설정된 configuration을 자기네가 제어하기 때문에 내가 아무리 설정해도 먹히지 않는다.

### git commit info 확인하기
* client쪽에 설정하면 admin server의 details 탭에서 git 관련 정보(commit 버전, user, git 주소, branch 명, artifact 버전)를 볼 수 있다.
1. client의 pom.xml 에 plugin 추가
```xml
<build>
	<plugins>
		<plugin>
			<groupId>pl.project13.maven</groupId>
			<artifactId>git-commit-id-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```
2. client의 application.yml 수정
```yml
management: 
  info:
    git:
      mode: full # 혹은 simple
```
