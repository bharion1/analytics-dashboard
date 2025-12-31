#!/bin/bash

# Script para executar o projeto Analytics Dashboard

# Tentar encontrar o JDK instalado
if [ -d "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" ]; then
    export JAVA_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
elif [ -d "/Library/Java/JavaVirtualMachines" ]; then
    # Tentar encontrar qualquer JDK instalado
    JDK_PATH=$(ls -d /Library/Java/JavaVirtualMachines/*/Contents/Home 2>/dev/null | head -1)
    if [ -n "$JDK_PATH" ]; then
        export JAVA_HOME="$JDK_PATH"
    fi
fi

# Se ainda não encontrou, tentar usar o java_home
if [ -z "$JAVA_HOME" ] || [ ! -d "$JAVA_HOME" ]; then
    JAVA_HOME_CMD=$(/usr/libexec/java_home -v 17 2>/dev/null)
    if [ -n "$JAVA_HOME_CMD" ]; then
        export JAVA_HOME="$JAVA_HOME_CMD"
    fi
fi

export PATH="$JAVA_HOME/bin:$PATH"

echo "Usando JAVA_HOME: $JAVA_HOME"
java -version

# Executar o projeto
./mvnw spring-boot:run

