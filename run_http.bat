@echo off

setlocal

rem path to java, relative or absolute path
set JAVA=java

rem nginx configuration
set NGINX=nginx.exe
set NGINX_PREFIX=./nginx
set NGINX_CONFIG=conf/nginx_http.conf

rem logs configuration
set LOGS_PATH=./log/

rem authentication service configuration
set AUTHENTICATION_SERVICE_PORT=8001
set AUTHENTICATION_SERVICE_JAR=./services/authentication/target/service-authentication-1.0-SNAPSHOT.jar

rem tasks service configuration
set TASKS_SERVICE_PORT=8002
set TASKS_SERVICE_JAR=./services/tasks/target/service-tasks-1.0-SNAPSHOT.jar

rem front server configuration
set WEB_PORT=8080
set WEB_SERVICE_JAR=./web/target/web-tasks-1.0-SNAPSHOT.jar
set NGINX_URL=http://localhost:8003/api/v1




@echo on

rem run nginx server
rem start %NGINX% -p %NGINX_PREFIX% -c %NGINX_CONFIG%

rem run authentication service
start %JAVA% -jar %AUTHENTICATION_SERVICE_JAR% --server.port=%AUTHENTICATION_SERVICE_PORT% --logging.path=%LOGS_PATH%

rem run tasks service
start %JAVA% -jar %TASKS_SERVICE_JAR% --server.port=%TASKS_SERVICE_PORT% --logging.path=%LOGS_PATH%

rem run front server
start %JAVA% -jar %WEB_SERVICE_JAR% --server.port=%WEB_PORT% --api.base.url=%NGINX_URL% --logging.path=%LOGS_PATH%

@echo off

endlocal