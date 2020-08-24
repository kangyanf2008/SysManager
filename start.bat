set PROGRAM_HOME=%~dp0
set JAVA_HOME=%~dp0jdk1.8.0
set classPath=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\dt.jar;
SET PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin  /M
java -jar %~dp0SM.jar
::java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9999,server=y,suspend=y -jar %~dp0SM.jar
