set JAVA_HOME=%~dp0jdk1.8.0
set classPath=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\dt.jar;
SET PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin  /M
java -jar %~dp0SM.jar 
