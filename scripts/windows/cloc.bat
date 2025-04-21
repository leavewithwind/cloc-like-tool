@echo off
set SCRIPT_DIR=%~dp0
set ROOT_DIR=%SCRIPT_DIR%..\..
java -jar "%ROOT_DIR%\target\cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" %* 