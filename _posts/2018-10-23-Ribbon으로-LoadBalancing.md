---
layout: post
title: "Ribbon으로 Load Balancing처리(without Eureka)"
date: 2018-10-23
excerpt: "유레카 없이 Ribbon만으로 LB처리"
tags: [ribbon, LoadBalance, LB]
comments: true
---

## Ribbon이란?
* 공식 가이드 문서에 따르면, Ribbon은 HTTP와 TCP를 사용하는 클라이언트를 제어할 수 있는 client-side load balancer이다.
보통 유레카에 LB로 내장되어 있어 유레카 사용시 자연스럽게 적용된다. 이번 포스트에서는 유레카 없이 Ribbon만 적용하는 방안에 대해 살펴보겠다.

## Ribbon 기본 설정
### Ribbon dependcy 추가
* 아래와 같이 Ribbon dependency를 추가한다.
```xml
    ...
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
		</dependency>
    ...
	<dependencyManagement>
		<dependencies>    
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Finchley.SR1</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>		    
      ...
```

### application.yml 수정
* 라우팅 하고자 하는 주소를 application.yml에 원하는 client server 이름으로 적는다.(여기에서는 store로 적었다.)
```yml
...
store: #원하는 client server 명
  ribbon:
    listOfServers: localhost:8888,localhost:8090 #라우팅하고자 하는 server list
...    
```
### @LoadBalanced를 통한 RestTemplate으로 호출
* 클라이언트 서버를 호출하는 부분을 아래와 같이 수정한다. 
```java
	@LoadBalanced
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	@Autowired
	LoadBalancerClient loadBalancer;
    
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/hi")
	public void hi() {
		String greeting = this.restTemplate.getForObject("http://store/", String.class); //클라이언트 서버 이름
		return;
	}
```
### 결과 확인
수정 후 확인해보면 라운드로빈 방식으로 서버 목록에 있는 서버들을 라우팅 한다는걸 알 수 있다.
하지만 서버들 중 하나가 내려가있는 상황에도 서버의 상태와 상관없이 라운드 로빈으로 라우팅 하게 된다. 다음에는 정상적인 서버에만 라우팅 할 수 있도록 Ribbon의 Configuration을 변경해보겠다.

## Ribbon의 Configuration Class 생성을 통해 Configuration 변경
### Ribbon configuration class 생성
```java
public class StoreConfiguration {
	@Bean
	public IPing ribbonPing(IClientConfig config) {
		return new PingUrl();
//		return new PingUrl(false,"/actuator/health"); //원하는 ping url이 있는 경우
	}

	@Bean
	public IRule ribbonRule(IClientConfig config) {
		return new AvailabilityFilteringRule();
	}
}
```
### @RibbonClient 추가
* @RibbonClient 를 추가하여 특정 클라이언트 서버에 대한 configuration class를 설정하거나 @RibbonClients를 추가하여 default configuration class를 설정한다.
```java
@SpringBootApplication
@RibbonClients(defaultConfiguration = StoreConfiguration.class)
//@RibbonClient(name="store", configuration = StoreConfiguration.class)
public class DemoApplication {
	public static void main(String[] args){        
        SpringApplication.run(DemoApplication.class, args);
    }	
}
```
### 결과 확인
* 위와 같이 설정 후 다시 확인해보면 올라가지 않은 서버에 대해서는 라우팅 하지 않는걸 확인할 수 있다.
* configuration class를 생성하지 않고 application.yml에 properties를 추가하여 configuration을 변경할 수도 있다.
```yml
store:
  ribbon:
    listOfServers: localhost:8888,localhost:8090
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.AvailabilityFilteringRule #IRule
    NFLoadBalancerPingClassName: com.netflix.loadbalancer.PingUrl #IPing
```

* 참고 사이트
https://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#spring-cloud-ribbon
https://github.com/Netflix/ribbon/wiki/Working-with-load-balancers
https://thepracticaldeveloper.com/2017/06/28/how-to-fix-eureka-taking-too-long-to-deregister-instances/
