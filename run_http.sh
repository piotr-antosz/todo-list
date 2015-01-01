#!/bin/bash

#path to java
JAVA=java #relative or absolute path

#nginx configuration
NGINX=nginx #relative or absolute path
NGINX_PREFIX=./nginx
NGINX_CONFIG=conf/nginx_http.conf

#authentication service configuration
AUTHENTICATION_SERVICE_PORT=8001
AUTHENTICATION_SERVICE_JAR=./services/authentication/target/service-authentication-1.0-SNAPSHOT.jar

#tasks service configuration
TASKS_SERVICE_PORT=8002
TASKS_SERVICE_JAR=./services/tasks/target/service-tasks-1.0-SNAPSHOT.jar

#front server configuration
WEB_PORT=8080
WEB_SERVICE_JAR=./web/target/web-tasks-1.0-SNAPSHOT.jar
NGINX_URL="http://localhost:8003/api/v1"




#run nginx server
${NGINX} -p ${NGINX_PREFIX} -c ${NGINX_CONFIG}

#run authentication service
xterm -e "${JAVA} -jar ${AUTHENTICATION_SERVICE_JAR} --server.port=${AUTHENTICATION_SERVICE_PORT}" &

#run tasks service
xterm -e "${JAVA} -jar ${TASKS_SERVICE_JAR} --server.port=${TASKS_SERVICE_PORT}" &

#run front server
xterm -e "${JAVA} -jar ${WEB_SERVICE_JAR} --server.port=${WEB_PORT} --api.base.url=${NGINX_URL}" &

