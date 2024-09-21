#!/bin/bash

# 设置根目录

cd $(dirname "$0")
cd ..
CRTDIR=$(pwd)

# 设置JAR文件路径
JAR_PATH=./riseger-core.jar

# 设置Java虚拟机参数
JAVA_OPTS="-Xmx512m -Xms256m"

# 执行启动命令
$JAVA_HOME/bin/java $JAVA_OPTS -jar $JAR_PATH $CRTDIR
