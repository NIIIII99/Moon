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

만약 Logback을 사용하는 서비스라면 혹시 Springboot 기반 서비스라면 아래 appender 설정을 추가하는 것만으로도 ES 에 원하는 형태로 로그를 적재할 수 있다.
