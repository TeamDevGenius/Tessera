#!/usr/bin/env sh
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
DEFAULT_JVM_OPTS='-Xmx64m -Xms64m'
APP_HOME="`pwd -P`"
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
exec "$JAVA_HOME/bin/java" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
  "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain "$@"