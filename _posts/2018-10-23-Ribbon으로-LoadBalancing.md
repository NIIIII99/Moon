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
    listOfServers: localhost:8888,localhost:8090 #라우팅하고자 하는 server list
...    
```

