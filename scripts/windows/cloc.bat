@echo off

:: 检查JAR文件是否在安装位置存在
set JAR_PATH=%USERPROFILE%\bin\cloc-like-tool\cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar
if not exist "%JAR_PATH%" (
    echo ERROR: Could not find JAR file at: %JAR_PATH%
    echo Please run the install script first to properly set up the cloc tool.
    exit /b 1
)

:: 运行程序
java -jar "%JAR_PATH%" %* 