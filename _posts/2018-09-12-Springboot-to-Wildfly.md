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
pom.xml 파일 변경후 위의 세가지 단계를 거쳐 서버를 올리니..이번에는 무슨 서블릿 오류가 난다며 또 올라가지 않았다.
기타 계속 발생한 오류는 다음단계 절정 에서 다루겠다...ㅠㅠ


## 절정
1. Servlet 오류 - springboot가 sevlet 컨테이너 안에서 동작하려면 아래 클래스를 상속받아서 오버라이드 해주어야 한다고 한다. 자세한건 아래 링크를 참고....

```java
public class MyProjectApplication extends SpringBootServletInitializer{
    ...
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyProjectApplication.class);
    }
    ...
}
```
https://medium.com/@SlackBeck/spring-boot-%EC%9B%B9-%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98%EC%9D%84-war%EB%A1%9C-%EB%B0%B0%ED%8F%AC%ED%95%A0-%EB%95%8C-%EC%99%9C-springbootservletinitializer%EB%A5%BC-%EC%83%81%EC%86%8D%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94%EA%B1%B8%EA%B9%8C-a07b6fdfbbde
2. Windows 환경에서 서블릿이 강제 종료됨
이번에는 오류 없이 잘 올라가는데 호출이 안되서 로그를 확인해보니 이상하게도 OS Signal로 인해 서버를 죽인다고 자꾸 내 서블릿을 죽이는 것이었다!!!
(로그는 standalone\log 폴더에서 확인 가능하다.)
회사 PC에서 테스트삼아 진행한 일이었기에 서버환경이 윈도우였다...
보통 서버환경은 linux 로 진행되니까 아마 나같은 일을 겪을 일은 별로 없을 것이다. 윈도우의 cmd 창을 **"관리자모드"** 로 실행하면 위의 문제는 해결할 수 있다. 그래도 여전히 404가 호출되었다..
3. context-root 수정
로그를 확인하던 중 http://127.0.0.1:9990 로 들어가면 wildfly 관리자모드가 가능하다는 정보가 있어 bin\add_user 를 통해 사용자를 등록하고 관리자 모드로 확인해보았다. 그랬더니...context_root가 내 프로젝트명인 my-project-1.0.0-SNAPSHOT 으로 잡혀져 있었다.(!!)
이걸 수정하려면 아래 파일을 webapp\WEB_INF\jboss_web.xml 로 생성하여 넣어주면 된다.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
        <context-root>/</context-root>
</jboss-web>
```

## 결말
이제 거의 다왔다! 위의 세단계를 거쳐 새 war파일을 배포해보니....이번에는 정상적으로 도는걸 확인할수 있었다.
최종적으로 수정해야 하는 부분은 세군데이다.
1. main 클래스 - SpringBootServletInitializer 상속받아서 아래 함수 override
```java
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyProjectApplication.class);
    }
```
2. pom.xml 파일 - war 패키지, tomcat대신 서블릿 넣고, maven으로 빌드
```xml
...
        <packaging>war</packaging>
...
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>			
		</dependency>
		<dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <scope>provided</scope>
        </dependency>
...
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
3. webapp\WEB_INF\jboss_web.xml 파일 추가
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
        <context-root>/</context-root>
</jboss-web>
```
