#!/usr/bin/env sh
# Minimal Gradle wrapper launcher for GitHub Actions

# Directory of this script (project root)
DIR="$(cd "$(dirname "$0")" && pwd)"

# Java executable (Actions will install Java for us)
JAVA_EXE=java

# Classpath points to the wrapper JAR
CLASSPATH="$DIR/gradle/wrapper/gradle-wrapper.jar"

# Main class of Gradle wrapper
MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain

exec "$JAVA_EXE" -classpath "$CLASSPATH" "$MAIN_CLASS" "$@"
