#!/bin/bash

[ -z "$JAVA_HOME" ] && fatal "JAVA_HOME must be set!"

LIB="${LIB:=./lib}"
DIST="${DIST:=./build/dist/jar}"
CP="$LIB/*:$DIST/*:."

"$JAVA_HOME/bin/java" -cp "$CP" com.quora.challenge.command.DuctPathCounter "$@"

function fatal () {
    echo "$@"
    exit 1
}

