@echo off

setlocal

rem path to java
set JAVA=java

rem authentication service configuration
set AUTHENTICATION_SERVICE_PORT=8001
set AUTHENTICATION_SERVICE_JAR=./services/authentication/target/service-authentication-1.0-SNAPSHOT.jar

rem tasks service configuration
set TASKS_SERVICE_PORT=8002
set TASKS_SERVICE_JAR=./services/tasks/target/service-tasks-1.0-SNAPSHOT.jar

rem front server configuration
set WEB_PORT=80
set WEB_SERVICE_JAR=./web/target/web-tasks-1.0-SNAPSHOT.jar
set NGINX_URL=http://localhost:8080/api/v1

@echo on

rem run authentication service
start %JAVA% -jar %AUTHENTICATION_SERVICE_JAR% --server.port=%AUTHENTICATION_SERVICE_PORT%

rem run tasks service
start %JAVA% -jar %TASKS_SERVICE_JAR% --server.port=%TASKS_SERVICE_PORT%

rem run front server
start %JAVA% -jar %WEB_SERVICE_JAR% --server.port=%WEB_PORT% --api.base.url=%NGINX_URL%

@echo off

endlocal