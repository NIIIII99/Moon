---
layout: post
title: "Elastic Search Logback Appender 적용"
date: 2018-10-30
excerpt: "Logback에 appender를 추가하여 ES로 로그를 쌓아보자"
tags: [logback, elasticsearch, appender]
comments: true
---
# Elastic Search Logback Appender 적용
보통 elastic search는 ELK(Elasticsearch+Logstash+Kibana)나 EFK(Logstash 대신 FluentD) 스택으로 사용한다.
하지만 만약 대시보드를 사용자가 만들고(대시보드인 Kibana 필요없다), Logstash나 FluentD 와 같이 ES에 쌓기위한 용도의 무언가를 하나 더 띄우는게 부담스럽다면?
(관리포인트가 늘어나는 지점이 될수 있다.)

만약 Logback을 사용하는 서비스라면 혹은 Springboot 기반 서비스라면 아래 appender 설정을 추가하는 것만으로도 ES 에 원하는 형태로 로그를 적재할 수 있다.

## Elasticsearch remote appender 추가
1. pom.xml 수정 - logback-elasticsearch-appender dependency 추가
```xml
		<!-- Elasticsearch Remote Appender -->
		<dependency>
			<groupId>com.internetitem</groupId>
			<artifactId>logback-elasticsearch-appender</artifactId>
			<version>1.6</version>
		</dependency>	
```
2. logback-spring.xml 파일 수정 - appender를 추가하여 ES에 보내고, log 파일로 따로 관리한다.
```xml
    <!-- ES appender 추가 -->
	<appender name="ELASTIC" class="com.internetitem.logback.elasticsearch.ElasticsearchAppender">
		<!-- 필수값 -->
        <url>http://localhost:9200/_bulk</url> <!-- 적재하려는 ES 주소 -->
		<index>apim_log</index>
        <type>log</type>        
        <!-- 선택값 -->
        <!-- ES 내용을 콘솔이나 별도 로그파일로 보려고 하는 경우 반드시 logger name 지정해야 ES 적재 내용이 보임 -->
		<loggerName>com.myproject.filter</loggerName>
		<includeMdc>true</includeMdc> <!-- MDC 를 이용하여 적재하는 경우 (default false) -->
		<sleepTime>1000</sleepTime> <!-- sleepTime 후 비동기로 처리 (in ms, default 250) -->
		<errorsToStderr>false</errorsToStderr> <!-- 에러내용을 콘솔창에 표시 (default false) -->
		<logsToStderr>false</logsToStderr> <!-- 로그를 콘솔창에 표시 (default false) -->		
		<headers>
			<header>
				<name>Content-Type</name>
				<value>application/json</value>
			</header>
		</headers>
	</appender>
    ...
    <!-- ES로 보내는 내용을 파일로 관리 -->
	<appender name="ES_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
			<charset>utf-8</charset>
			<pattern>%msg</pattern>
		</encoder>
		<!-- 일자별로 로그파일 적용하기 -->
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${LOG_FILE}_ES_%d{yyyy-MM-dd}_%i.log</fileNamePattern>
			<maxHistory>30</maxHistory>
			<timeBasedFileNamingAndTriggeringPolicy 
                class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>100MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
		</rollingPolicy>		
	</appender>
    ...
    <!-- com.myproject.filter 에서 설정한 로그 내용만 ES로 보낸다. --> 
	<logger name="com.myproject.filter" level="info" additivity="false">			
		<appender-ref ref="ELASTIC" /> <!-- 앞에서 지정한 ES appender 로 보낸다. --> 
		<appender-ref ref="CONSOLE" /> <!-- 해당 내용을 console창에서 확인 -->
		<appender-ref ref="ES_FILE" /> <!-- 해당 내용을 file로 확인 -->
	</logger>	
```

## com.myproject.filter 에서 로그 설정
이 서비스에서는 servlet filter에서 어떤 http call이 발생하는지 request와 response 정보를 확인하는 로그를 설정한다.
- filter class를 작성
```java
package com.myproject.filter

@Slf4j
@WebFilter(urlPatterns = "/myproject/*") //해당 url일때만 filter 설정
public class Myfilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
				
		MDC.put("requestUri", uri);
		MDC.put("method", req.getMethod());		
		MDC.put("reqRes", " Request");        
		log.info("{}", "Request");

		chain.doFilter(request, response);
		
		// Response
		MDC.put("reqRes", " Response");
		MDC.put("status", res.getStatus()); // Req or Res
		log.info("{}", "Response");

		MDC.clear();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}
```

## Main application에서 Servlet filter 설정
```java
@SpringBootApplication
@ServletComponentScan // Servlet Filter
public class MyprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyprojectApplication.class, args);
	}
}
```
