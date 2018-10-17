---
layout: post
title: "Springboot log file로 보기"
date: 2018-09-17
excerpt: "Console 아닌 File로 로그를 관리할거야"
tags: [logback-spring, logback, springboot]
comments: true
---


# Springboot log file로 보기

* springboot는 특별한 설정이 없는한 console로 로그를 확인할 수 있다. 그런데 log를 관리해야하는 경우가 발생한다면? 그때가 바로 이 포스트를 봐야할때!
## 특별한 설정없이 file로 만들기
* 기본 포맷을 그대로 콘솔이 아닌 file로 log를 표시할때는 application.yml 파일만 수정하면 된다.
```yml
logging:
  file: ./logs/data.log
```
* 쉽고 간단하지만 그런데 이렇게 하면 콘솔에서 더이상 로그를 확인할 수 없게 된다....

## logback-spring.xml을 이용하여 console과 file에서 모두 log 확인하기
* springboot는 common-logging을 사용하고 있다고 가이드문서에는 명시되어 있지만 그 내부를 보면 logback을 사용하고 있다.
log 관련 설정은 logback-sping.xml로 명시하는게 좋다고 가이드문서에서 권하고 있으니, 하라는대로 하는게 좋겠다.(logback.xml은 스프링부트가 뜨기도 전에 읽어가서 제대로 설정이 먹히지 않을수 있다. )
1. resources/logback-spring.xml 작성
```xml
<include resource="org/springframework/boot/logging/logback/base.xml" />
	<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>./logs/servicelog.log</file> <!-- 로그 경로/로그 파일명 설정 -->
		<encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
			<charset>utf-8</charset>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level --- [%thread]
				%logger{35} : %msg %n</pattern>
		</encoder>
		<!-- 일자별로 로그파일 적용하기 -->
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern> servicelog__%d{yyyy-MM-dd}_%i.log </fileNamePattern>
			<maxHistory>30</maxHistory>
			<timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>100MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
		</rollingPolicy>
	</appender>
```
