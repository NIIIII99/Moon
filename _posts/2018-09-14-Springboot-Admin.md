
## Springboot Admin이란?
https://github.com/codecentric/spring-boot-admin

* springboot는 /actuator Endpoint를 제공하여 프로그램의 모니터링을 가능하게 함.
* 근데 이 정보는 json 형태로 가독성이 너무나도 떨어짐.
* 그런 점을 보완하기 위해 나온게 Springboot Admin UI! 일종의 대시보드 기능으로 보면 됨

### 주요 기능
* actuator에서 제공하는 정보는 다 볼 수 있음
* Thread dump, log file 등등

## 그외 기능
* endpoint를 사용자가 원하는 곳에 추가할 수 있음
(어노테이션 추가로 어떤 method든지 모니터링 가능)
* 로그 레벨을 변경하여 재기동 없이 바로 확인 가능
* /actuator 대신 다른 base URL 사용가능 (포트도 변경 가능)
