#!/bin/bash

#path to java
JAVA=java #relative or absolute path

#nginx configuration
NGINX=nginx #relative or absolute path
NGINX_PREFIX=./nginx
NGINX_CONFIG=conf/nginx_https.conf

#logs configuration
LOGS_PATH=./log/

#ssl configuration
SSL_KEY_STORE=./.keystore
SSL_KEY_STORE_PASSWORD=changeit
SSL_KEY_PASSWORD=changeit
SSL_KEY_ALIAS=tomcat

#authentication service configuration
AUTHENTICATION_SERVICE_PORT=8041
AUTHENTICATION_SERVICE_JAR=./services/authentication/target/service-authentication-1.0-SNAPSHOT.jar

#tasks service configuration
TASKS_SERVICE_PORT=8042
TASKS_SERVICE_JAR=./services/tasks/target/service-tasks-1.0-SNAPSHOT.jar

#front server configuration
WEB_PORT=8443
WEB_SERVICE_JAR=./web/target/web-tasks-1.0-SNAPSHOT.jar
NGINX_URL="https://localhost:8043/api/v1"




#run nginx server
${NGINX} -p ${NGINX_PREFIX} -c ${NGINX_CONFIG}

#run authentication service
xterm -e "${JAVA} -jar ${AUTHENTICATION_SERVICE_JAR} --server.port=${AUTHENTICATION_SERVICE_PORT} --server.ssl.key-store=${SSL_KEY_STORE} --server.ssl.key-store-password=${SSL_KEY_STORE_PASSWORD} --server.ssl.key-password=${SSL_KEY_PASSWORD} --server.ssl.key-alias=${SSL_KEY_ALIAS} --logging.path=${LOGS_PATH}" &

#run tasks service
xterm -e "${JAVA} -jar ${TASKS_SERVICE_JAR} --server.port=${TASKS_SERVICE_PORT} --server.ssl.key-store=${SSL_KEY_STORE} --server.ssl.key-store-password=${SSL_KEY_STORE_PASSWORD} --server.ssl.key-password=${SSL_KEY_PASSWORD} --server.ssl.key-alias=${SSL_KEY_ALIAS} --logging.path=${LOGS_PATH}" &

#run front server
xterm -e "${JAVA} -jar ${WEB_SERVICE_JAR} --server.port=${WEB_PORT} --api.base.url=${NGINX_URL} --server.ssl.key-store=${SSL_KEY_STORE} --server.ssl.key-store-password=${SSL_KEY_STORE_PASSWORD} --server.ssl.key-password=${SSL_KEY_PASSWORD} --server.ssl.key-alias=${SSL_KEY_ALIAS} --logging.path=${LOGS_PATH}" &

