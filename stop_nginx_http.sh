#!/bin/bash

#nginx configuration
NGINX=nginx #relative or absolute path
NGINX_PREFIX=./nginx
NGINX_CONFIG=conf/nginx_http.conf




#run nginx server
${NGINX} -p ${NGINX_PREFIX} -c ${NGINX_CONFIG} -s stop
