#!/usr/bin/env sh

# -------------------------------------------------------
# Minimal Gradle wrapper shell script (Linux / macOS)
# -------------------------------------------------------
APP_BASE_NAME=$(basename "$0")
APP_HOME=$(cd "$(dirname "$0")" && pwd)

# Locate Java
if [ -n "$JAVA_HOME" ] ; then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA=java
fi

# Execute Gradle wrapper JAR
exec "$JAVA" -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
    org.gradle.wrapper.GradleWrapperMain "$@"
