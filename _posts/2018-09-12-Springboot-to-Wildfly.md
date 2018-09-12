---
layout: post
title: "Springboot 기반 application을 tomcat이 아닌 wildfly에 올려보기"
date: 2018-09-12
excerpt: "열심히 했지만 업무에 쓰이지 않았다"
tags: [wildfly, tomcat, springboot]
comments: true
---

## Springboot 기반 application을 tomcat이 아닌 wildfly에 올려보기 

*주의 : 본글은 아주 많은 MSG가 포함되어 있으며 개발을 제외한 모든 이야기는 허구일 수도 있다는 사실을 밝힙니다. 저도 회사생활해야죠...*

### 발단
새로운 프로젝트에 투입된 첫날, 방치되었던 나에게 어떠한 소식이 전해졌다.
"우리 프로젝트는 springboot 기반인데...(embeded tomcat을 사용하는데) 이걸 wildfly에 올려야할것 같아."
왜때문에 widlfly? 중요하지 않다. 대다수의 개발 업무는 이유 없이 발생하게 된다.
tomcat을 빼고 undertow 서블릿을 적용해봤던 경험이 있었기에 아주 자신있게 말했다.
"아, 저 그거 할줄 알아요. 엄청 간단해요~"

## 전개
그 당시의 난 아주 단순하게 생각했었다.
'packging을 WAR로 변경하고 embeded tomcat 대신 JBOSS가 지원하는 undertow나.. servlet 기능 있는 것만 넣으면 금방 되겠지!'
어서 해서 메일을 보내고 방치에서 벗어나고 싶다고 생각한 의욕뿜뿜의 나는 어서 빨리 당장 톰캣을 제외하고 servlet을 넣었다.
package도 WAR로 변경하고.... wildfly도 다운받고...  
mvn package로 war 파일을 만들고!!
```sh
>> mvn clean install package
```
wildfly 서버의 standalone/deployments 폴더안에 war를 복사!!
```sh
>> cp target/my-project-1.0.0-SNAPSHOT.war %JBOSS_HOME%/standalone/deployments
```
서버야 올라가자!!!
```
>>%JBOSS_HOME%\bin\standalone
```

## 위기
그랬다. 되지 않았다.
어떠한 에러가 나면서 WEB.xml 을 찾을 수가 없댄다!
이럴때는 구글링(*이라고 쓰고 구글님이라고 읽는다.*)이지!! 여기저기 정보의 바다를 찾아다니니 maven 3.0이상부터는 web.xml이 없어도 정상동작해야할텐데 만약 정상동작 하지 않는 경우 pom.xml에 요런걸 추가해서 WEB.xml이 없어도 오류가 나지 않도록 해주랜다.
```xml
	    <plugins>
	      <plugin>
	        <groupId>org.apache.maven.plugins</groupId>
	        <artifactId>maven-war-plugin</artifactId>
	        <configuration>
	          <failOnMissingWebXml>false</failOnMissingWebXml>
	        </configuration>
	      </plugin>
	    </plugins>
```

