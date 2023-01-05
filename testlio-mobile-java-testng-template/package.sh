#!/bin/sh
SUITE_NAME=basic
[ ! -z "$1" ] && SUITE_NAME=$1
echo "$SUITE_NAME suite selected"

cp src/test/resources/suites/$SUITE_NAME.xml src/test/resources/testng.xml
mvn package -DskipTests -Dsuite.name=$SUITE_NAME
rm src/test/resources/testng.xml