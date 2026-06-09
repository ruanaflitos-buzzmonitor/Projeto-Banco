@echo off
setlocal
pushd "%~dp0"

if "%TOMCAT_HOME%"=="" if exist "apache-tomcat-9.0.118-windows-x64\apache-tomcat-9.0.118" set "TOMCAT_HOME=%CD%\apache-tomcat-9.0.118-windows-x64\apache-tomcat-9.0.118"
if "%TOMCAT_HOME%"=="" set "TOMCAT_HOME=C:\tomcat"

if "%JAVAC_EXE%"=="" if not "%JAVA_HOME%"=="" if exist "%JAVA_HOME%\bin\javac.exe" set "JAVAC_EXE=%JAVA_HOME%\bin\javac.exe"
if "%JAVAC_EXE%"=="" if exist "%USERPROFILE%\.jdks\openjdk-26.0.1\bin\javac.exe" set "JAVAC_EXE=%USERPROFILE%\.jdks\openjdk-26.0.1\bin\javac.exe"
if "%JAVAC_EXE%"=="" set "JAVAC_EXE=javac"

set CP=lib\postgresql-42.6.0.jar;lib\gson-2.10.1.jar;lib\servlet-api.jar;%TOMCAT_HOME%\lib\servlet-api.jar

echo [1/3] Compilando...
if exist bin (
  attrib -R bin\* /S /D >nul 2>nul
  rmdir /s /q bin
)
if exist bin (
  echo ERRO: nao foi possivel limpar a pasta bin.
  if "%1" neq "--no-pause" pause
  popd
  exit /b 1
)
mkdir bin
"%JAVAC_EXE%" -encoding UTF-8 -d bin -cp "%CP%" ^
  src\com\ucsal\clinica\model\*.java ^
  src\com\ucsal\clinica\util\*.java ^
  src\com\ucsal\clinica\dao\*.java ^
  src\com\ucsal\clinica\servlet\*.java
if errorlevel 1 (
  echo ERRO de compilacao.
  if "%1" neq "--no-pause" pause
  popd
  exit /b 1
)

echo [2/3] Montando o webapp em deploy\ClinicaMedica ...
if exist deploy (
  attrib -R deploy\* /S /D >nul 2>nul
  rmdir /s /q deploy
)
if exist deploy (
  echo ERRO: nao foi possivel limpar a pasta deploy.
  if "%1" neq "--no-pause" pause
  popd
  exit /b 1
)
mkdir deploy\ClinicaMedica\WEB-INF\classes
mkdir deploy\ClinicaMedica\WEB-INF\lib
xcopy /e /i /y bin\* deploy\ClinicaMedica\WEB-INF\classes\ >nul
if errorlevel 1 goto erro_deploy
copy /y web\WEB-INF\web.xml deploy\ClinicaMedica\WEB-INF\ >nul
if errorlevel 1 goto erro_deploy
copy /y lib\postgresql-42.6.0.jar deploy\ClinicaMedica\WEB-INF\lib\ >nul
if errorlevel 1 goto erro_deploy
copy /y lib\gson-2.10.1.jar deploy\ClinicaMedica\WEB-INF\lib\ >nul
if errorlevel 1 goto erro_deploy
copy /y web\index.html deploy\ClinicaMedica\ >nul
if errorlevel 1 goto erro_deploy
copy /y web\style.css  deploy\ClinicaMedica\ >nul
if errorlevel 1 goto erro_deploy
copy /y web\script.js  deploy\ClinicaMedica\ >nul
if errorlevel 1 goto erro_deploy

echo [3/3] Pronto!
echo Copie a pasta deploy\ClinicaMedica para %TOMCAT_HOME%\webapps\ e inicie o Tomcat.
echo Depois abra: http://localhost:8080/ClinicaMedica/
if "%1" neq "--no-pause" pause
popd
exit /b 0

:erro_deploy
echo ERRO ao montar o deploy.
if "%1" neq "--no-pause" pause
popd
exit /b 1
