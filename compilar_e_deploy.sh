#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

: "${TOMCAT_HOME:=/opt/tomcat}"
JAVAC="${JAVAC_EXE:-javac}"

case "$(uname -s 2>/dev/null)" in
  MINGW*|MSYS*|CYGWIN*) CP="lib/*;${TOMCAT_HOME}/lib/*" ;;
  *) CP="lib/*:${TOMCAT_HOME}/lib/*" ;;
esac

echo "[1/3] Compilando..."
rm -rf bin && mkdir -p bin
"$JAVAC" -encoding UTF-8 -d bin -classpath "$CP" \
  src/com/ucsal/clinica/model/*.java \
  src/com/ucsal/clinica/util/*.java \
  src/com/ucsal/clinica/dao/*.java \
  src/com/ucsal/clinica/servlet/*.java

echo "[2/3] Montando o webapp em deploy/ClinicaMedica ..."
rm -rf deploy
mkdir -p deploy/ClinicaMedica/WEB-INF/classes deploy/ClinicaMedica/WEB-INF/lib
cp -r bin/* deploy/ClinicaMedica/WEB-INF/classes/
cp web/WEB-INF/web.xml deploy/ClinicaMedica/WEB-INF/
cp lib/postgresql-42.6.0.jar lib/gson-2.10.1.jar deploy/ClinicaMedica/WEB-INF/lib/
cp web/index.html web/style.css web/script.js deploy/ClinicaMedica/

echo "[3/3] Pronto!"
echo "Copie a pasta deploy/ClinicaMedica para ${TOMCAT_HOME}/webapps/ e inicie o Tomcat."
echo "Depois abra: http://localhost:8080/ClinicaMedica/"
