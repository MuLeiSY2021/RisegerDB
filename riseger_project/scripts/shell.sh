#!/bin/bash

# 设置Java运行时环境变量，根据你的实际情况修改JAVA_HOME的路径
CRTDIR=$(pwd)/models/shellCli

# 设置JAR文件路径，根据你的实际情况修改
JAR_PATH=./models/shellCli/ShellClient.jar

# 设置Java虚拟机参数，根据需要修改
JAVA_OPTS="-Xmx512m -Xms256m"

# 执行启动命令
$JAVA_HOME/bin/java $JAVA_OPTS -jar $JAR_PATH $CRTDIR
