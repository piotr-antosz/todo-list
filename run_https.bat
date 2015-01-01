@echo off

setlocal

rem path to java
set JAVA=java

rem nginx configuration
set NGINX=nginx
set NGINX_PREFIX=./nginx
set NGINX_CONFIG=conf/nginx_https.conf

rem ssl configuration
set SSL_KEY_STORE=./.keystore
set SSL_KEY_STORE_PASSWORD=changeit
set SSL_KEY_PASSWORD=changeit
set SSL_KEY_ALIAS=tomcat

rem authentication service configuration
set AUTHENTICATION_SERVICE_PORT=8001
set AUTHENTICATION_SERVICE_JAR=./services/authentication/target/service-authentication-1.0-SNAPSHOT.jar

rem tasks service configuration
set TASKS_SERVICE_PORT=8002
set TASKS_SERVICE_JAR=./services/tasks/target/service-tasks-1.0-SNAPSHOT.jar

rem front server configuration
set WEB_PORT=8443
set WEB_SERVICE_JAR=./web/target/web-tasks-1.0-SNAPSHOT.jar
set NGINX_URL=https://localhost:8043/api/v1




@echo on

rem run nginx server
%NGINX% -p %NGINX_PREFIX% -c %NGINX_CONFIG%

rem run authentication service
start %JAVA% -jar %AUTHENTICATION_SERVICE_JAR% --server.port=%AUTHENTICATION_SERVICE_PORT% --server.ssl.key-store=%SSL_KEY_STORE% --server.ssl.key-store-password=%SSL_KEY_STORE_PASSWORD% --server.ssl.key-password=%SSL_KEY_PASSWORD% --server.ssl.key-alias=%SSL_KEY_ALIAS%

rem run tasks service
start %JAVA% -jar %TASKS_SERVICE_JAR% --server.port=%TASKS_SERVICE_PORT% --server.ssl.key-store=%SSL_KEY_STORE% --server.ssl.key-store-password=%SSL_KEY_STORE_PASSWORD% --server.ssl.key-password=%SSL_KEY_PASSWORD% --server.ssl.key-alias=%SSL_KEY_ALIAS%

rem run front server
start %JAVA% -jar %WEB_SERVICE_JAR% --server.port=%WEB_PORT% --server.ssl.key-store=%SSL_KEY_STORE% --server.ssl.key-store-password=%SSL_KEY_STORE_PASSWORD% --server.ssl.key-password=%SSL_KEY_PASSWORD% --server.ssl.key-alias=%SSL_KEY_ALIAS% --api.base.url=%NGINX_URL%

@echo off

endlocal