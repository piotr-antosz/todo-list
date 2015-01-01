## TODO list
TODO list web app

## Building from Source
App uses a [Maven][]-based build system. In the instructions
below, `mvn` is invoked from the root of the source tree.

### Prerequisites

[Git][], [JDK 7 or later][JDK download] and [Maven 3][Maven download].

Be sure that your `JAVA_HOME` environment variable points to the `jdk` folder
extracted from the JDK download.

### Check out sources
`git clone git@github.com:piotr-antosz/todo-list.git`

### Build
`mvn clean install`

### Run integration tests
`mvn failsafe:integration-test`
> **Note:** integration tests are skipped during `mvn test`


## Running app
In the instructions below all scripts are invoked from the root of the source tree.

### Prerequisites

[JRE 7 or later][JDK download] and [Nginx][Nginx] with 3 additional modules ([HttpLuaModule][], [CJSON][], [HttpHeadersMoreModule][]).

Be sure that your `PATH` environment variable points to the `nginx` or `nginx.exe` executable.

##### Example nginx installation on Windows 7

* download and extract [nginx 1.7.9.1 Gryphon.zip][nginx 1.7.9.1 Gryphon] (it already contains HttpLuaModule and HttpHeadersMoreModule)
* dwonload [lua files][], extract and copy `cjson.dll` to nginx installation dir (just place it next to `nginx.exe`)
* update `Path` environment variable to access `nginx.exe` from anywhere 

##### Example nginx installation on Ubuntu 14.04

`sudo apt-get install libreadline-dev libpcre3-dev libssl-dev perl`  

`wget http://openresty.org/download/ngx_openresty-1.7.2.1.tar.gz` 
  
`tar xzvf ngx_openresty-1.7.2.1.tar.gz`

`cd ngx_openresty-1.7.2.1/`

`./configure`
> **Note:** you can configure with `./configure --prefix="the folder you want to install"`, default is `/usr/local/openresty` 

`make`

`sudo make install`

`export PATH=/usr/local/openresty:$PATH`

### Start app

In the instructions below all scripts are invoked from the root of the source tree.

> **Note:** default configuration assumes that some ports are not being used by system. For http scenario they are `8001`, `8002`, `8003` and `8080`. For https scenario they are `8041`, `8042`, `8043` and `8443`.

Run `./run_http.sh` or `./run_http.bat` for http communication. If you want to use https, start `./run_https.sh` or `./run_https.bat`

> **Note:** because of self signed SSL certificate, some browsers have problems with making ajax calls to not verified https endpoints. Latest Chrome is working fine after accepting untrusted certificate during first web page opening. Firefox needs additionally to accept nginx's certificate by going to [https://localhost:8043](https://localhost:8043)  

### Browser

Open [http://localhost:8080](http://localhost:8080) for http or [https://localhost:8443](https://localhost:8443) for https.

### Stop app

In the instructions below all scripts are invoked from the root of the source tree.

Close all opened console windows and run `./stop_nginx_http.sh` or `./stop_nginx_http.bat` for http communication. For https there are `./stop_nginx_https.sh` or `./stop_nginx_https.bat`.

[Maven]: http://maven.apache.org
[Git]: http://help.github.com/set-up-git-redirect
[JDK download]: http://www.oracle.com/technetwork/java/javase/downloads
[Maven download]: http://maven.apache.org/download.cgi
[Nginx]: http://wiki.nginx.org/Install
[HttpLuaModule]: http://wiki.nginx.org/Install
[CJSON]: http://www.kyne.com.au/~mark/software/lua-cjson-manual.html
[HttpHeadersMoreModule]: http://wiki.nginx.org/HttpHeadersMoreModule#Installation
[nginx 1.7.9.1 Gryphon]: http://nginx-win.ecsds.eu/download/nginx%201.7.9.1%20Gryphon.zip
[lua files]: https://lua-files.googlecode.com/archive/34fb452053a6abe752ec0e2ba4e268c832050a7f.zip