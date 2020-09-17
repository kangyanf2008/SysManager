@echo off

set CUR_DIR=%~dp0

if "%1" equ "install" goto install
if "%1" equ "start" goto start_docker
if "%1" equ "run" goto run_docker
if "%1" equ "restart" goto restart_docker
if "%1" equ "stop" goto stop_docker
if "%1" equ "delete" goto delete_docker
if "%1" equ "init" goto init
if "%1" equ "loadImage" goto load_images
if "%1" equ "init_data" goto init_data

echo end
exit /b

:install
	call :load_images
	call :pre_run
	call :run_docker
	call :init_data
goto:eof

:init
	call :pre_run
	call :run_docker
	call :init_data
goto:eof

:load_images
	for %%f in (%CUR_DIR%images\*.tar) do docker load -i %%f
goto:eof

:start_docker
    echo "start mysql"
    call %CUR_DIR%docker\mysql\docker.bat start
	set CUR_DIR=%~dp0

    echo "start nginx"
    call %CUR_DIR%docker\nginx\docker.bat start
	set CUR_DIR=%~dp0

    echo "start redis"
    call %CUR_DIR%docker\redis\docker.bat start
	set CUR_DIR=%~dp0
	
    echo "start mongo"
    call %CUR_DIR%docker\mongo\docker.bat start
	set CUR_DIR=%~dp0

    echo "start emqx"
    call %CUR_DIR%docker\emqx\docker.bat start
	set CUR_DIR=%~dp0
	
	echo "start terminal-web-tenant"
    call %CUR_DIR%docker\terminal-web-tenant\docker.bat start
	set CUR_DIR=%~dp0
	
	call %CUR_DIR%\cutIcon.bat CreateDesktopShort
	set CUR_DIR=%~dp0
	
::	echo "start terminal-web-admin"
::    call %CUR_DIR%docker\terminal-web-admin\docker.bat start
::	set CUR_DIR=%~dp0
goto :eof

:run_docker
    echo "run mysql"
    call %CUR_DIR%docker\mysql\docker.bat run
	set CUR_DIR=%~dp0

    echo "run nginx"
    call %CUR_DIR%docker\nginx\docker.bat run
	set CUR_DIR=%~dp0

    echo "run redis"
    call %CUR_DIR%docker\redis\docker.bat run
	set CUR_DIR=%~dp0
	
    echo "run mongo"
    call %CUR_DIR%docker\mongo\docker.bat run
	set CUR_DIR=%~dp0

    echo "run emqx"
    call %CUR_DIR%docker\emqx\docker.bat run
	set CUR_DIR=%~dp0
	
	echo "run terminal-web-tenant"
    call %CUR_DIR%docker\terminal-web-tenant\docker.bat run
	set CUR_DIR=%~dp0
	
	call %CUR_DIR%\cutIcon.bat CreateDesktopShort
	set CUR_DIR=%~dp0
	
::	echo "run terminal-web-admin"
::    call %CUR_DIR%docker\terminal-web-admin\docker.bat run
::	set CUR_DIR=%~dp0
goto :eof

:restart_docker
	::echo %PATH%
    echo "restart mysql"
    call %CUR_DIR%docker\mysql\docker.bat restart
	set CUR_DIR=%~dp0

    echo "restart nginx"
    call %CUR_DIR%docker\nginx\docker.bat restart
	set CUR_DIR=%~dp0
	
    echo "restart redis"
    call %CUR_DIR%docker\redis\docker.bat restart
	set CUR_DIR=%~dp0

    echo "restart mongo"
    call %CUR_DIR%docker\mongo\docker.bat restart
	set CUR_DIR=%~dp0
	
    echo "restart emqx"
    call %CUR_DIR%docker\emqx\docker.bat restart
	set CUR_DIR=%~dp0
	
	echo "restart terminal-web-tenant"
    call %CUR_DIR%docker\terminal-web-tenant\docker.bat restart
	set CUR_DIR=%~dp0
	
	call %CUR_DIR%\cutIcon.bat CreateDesktopShort
	set CUR_DIR=%~dp0
::	echo "restart terminal-web-admin"
::    call %CUR_DIR%docker\terminal-web-admin\docker.bat restart
::	set CUR_DIR=%~dp0
goto :eof

:stop_docker
	echo "stop terminal-web-tenant"
    call %CUR_DIR%docker\terminal-web-tenant\docker.bat stop
	set CUR_DIR=%~dp0
	
::	echo "stop terminal-web-admin"
::    call %CUR_DIR%docker\terminal-web-admin\docker.bat stop
::	set CUR_DIR=%~dp0
	
    echo "stop mysql"
    call %CUR_DIR%docker\mysql\docker.bat stop
	set CUR_DIR=%~dp0

    echo "stop nginx"
    call %CUR_DIR%docker\nginx\docker.bat stop
	set CUR_DIR=%~dp0

    echo "stop redis"
    call %CUR_DIR%docker\redis\docker.bat stop
	set CUR_DIR=%~dp0

    echo "stop mongo"
    call %CUR_DIR%docker\mongo\docker.bat stop
	set CUR_DIR=%~dp0

    echo "stop emqx"
    call %CUR_DIR%docker\emqx\docker.bat stop
	set CUR_DIR=%~dp0
goto :eof

:init_data
	::mongo volume create
	docker volume create --name mongo-single
	cd %CUR_DIR%
    ::timeout /T 20 /NOBREAK
	echo "mysql timeWait"
	set waitTime=20
	set oldDate=%date:~0,4%%date:~5,2%%date:~8,2%
	set oldTime=%time:~0,2%%time:~3,2%%time:~6,2%
	call :timeWait
	if "%ERRORLEVEL%"=="0" (echo "mysql timeWait success") else (echo "mysql timeWait fail")
	
    ::init mysql
	docker cp sql mysql:/tmp
    docker exec mysql //bin/bash -c "mysql -uroot -pcimevue@1234 < /tmp/sql/terminal_lite.sql"
goto :eof

:pre_run
	::mongo volume create
	echo "create volume mongo-single"
	docker volume create --name mongo-single
	if "%ERRORLEVEL%"=="0" (echo "create volume mongo-single success") else (echo "create volume mongo-single fail")
goto :eof

:delete_docker
	echo "delete terminal-web-tenant"
    call %CUR_DIR%docker\terminal-web-tenant\docker.bat delete
	set CUR_DIR=%~dp0
	
::	echo "delete terminal-web-admin"
::    call %CUR_DIR%docker\terminal-web-admin\docker.bat delete
::	set CUR_DIR=%~dp0
	
	echo "delete mysql"
    call %CUR_DIR%docker\mysql\docker.bat delete
	set CUR_DIR=%~dp0

    echo "delete nginx"
    call %CUR_DIR%docker\nginx\docker.bat delete
	set CUR_DIR=%~dp0

    echo "delete redis"
    call %CUR_DIR%docker\redis\docker.bat delete
	set CUR_DIR=%~dp0

    echo "delete mongo"
    call %CUR_DIR%docker\mongo\docker.bat delete
	set CUR_DIR=%~dp0

    echo "delete emqx"
    call %CUR_DIR%docker\emqx\docker.bat delete
	set CUR_DIR=%~dp0
	
	echo "delete volume mongo-single"
	docker volume rm -f mongo-single
	set CUR_DIR=%~dp0
goto:eof

:timeWait
set newDate=%date:~0,4%%date:~5,2%%date:~8,2%
set newTime=%time:~0,2%%time:~3,2%%time:~6,2% 
set /a tem=%oldTime% + %waitTime%
if %newDate% neq %oldDate% ( 
		echo "exit timeWait"
	) else (
		if %newTime% geq %tem% ( 
			echo "exit timeWait"
		) else ( goto timeWait)
	)
goto:eof



