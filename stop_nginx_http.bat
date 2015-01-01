@echo off

setlocal

rem nginx configuration
set NGINX=nginx.exe
set NGINX_PREFIX=./nginx
set NGINX_CONFIG=conf/nginx_http.conf




@echo on

rem run nginx server
%NGINX% -p %NGINX_PREFIX% -c %NGINX_CONFIG% -s stop

@echo off

endlocal