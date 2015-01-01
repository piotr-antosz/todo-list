#!/bin/bash

#nginx configuration
NGINX=nginx #relative or absolute path
NGINX_PREFIX=./nginx
NGINX_CONFIG=conf/nginx_https.conf




#run nginx server
${NGINX} -p ${NGINX_PREFIX} -c ${NGINX_CONFIG} -s stop
