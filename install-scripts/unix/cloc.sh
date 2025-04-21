#!/bin/bash

# 检查JAR文件是否在安装位置存在
JAR_PATH="$HOME/bin/cloc-like-tool/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar"
if [ ! -f "$JAR_PATH" ]; then
    echo "ERROR: Could not find JAR file at: $JAR_PATH"
    echo "Please run the install script first to properly set up the cloc tool."
    exit 1
fi

# 运行程序
java -jar "$JAR_PATH" "$@"