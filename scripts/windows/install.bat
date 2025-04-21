@echo off
rem Simple installation script for cloc tool
echo Installing cloc tool...

rem Set directories
set TOOL_DIR=%USERPROFILE%\bin\cloc-like-tool
set BIN_DIR=%USERPROFILE%\bin
set JAR_NAME=cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar

rem Create directories
echo Creating directories...
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"
if not exist "%TOOL_DIR%" mkdir "%TOOL_DIR%"

rem Get script directory
set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%..\..

rem Look for JAR in multiple locations
echo Looking for JAR file...
set JAR_PATH=

rem Check script directory for JAR
if exist "%SCRIPT_DIR%%JAR_NAME%" (
    set JAR_PATH=%SCRIPT_DIR%%JAR_NAME%
    echo Found JAR in script directory
    goto CopyJar
)

rem Check current directory for JAR
if exist "%JAR_NAME%" (
    set JAR_PATH=%JAR_NAME%
    echo Found JAR in current directory
    goto CopyJar
)

rem Check project root for JAR
if exist "%PROJECT_ROOT%\%JAR_NAME%" (
    set JAR_PATH=%PROJECT_ROOT%\%JAR_NAME%
    echo Found JAR in project root directory
    goto CopyJar
)

rem Check project root target directory for JAR
if exist "%PROJECT_ROOT%\target\%JAR_NAME%" (
    set JAR_PATH=%PROJECT_ROOT%\target\%JAR_NAME%
    echo Found JAR in project target directory
    goto CopyJar
)

rem JAR not found
echo ERROR: JAR file not found
echo Searched locations:
echo - %SCRIPT_DIR%%JAR_NAME%
echo - %CD%\%JAR_NAME%
echo - %PROJECT_ROOT%\%JAR_NAME%
echo - %PROJECT_ROOT%\target\%JAR_NAME%
echo.
echo Please ensure the JAR file exists or build the project first.
goto End

:CopyJar
rem Copy JAR to installation directory
echo Copying JAR from: %JAR_PATH%
copy "%JAR_PATH%" "%TOOL_DIR%\"
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to copy JAR file
    goto End
)

:CreateLauncher
rem Create launcher script
echo Creating launcher script...
(
    echo @echo off
    echo java -jar "%%USERPROFILE%%\bin\cloc-like-tool\%JAR_NAME%" %%*
) > "%BIN_DIR%\cloc.bat"

rem Add to PATH
echo Adding to PATH...
powershell -Command ^
    "$path = [Environment]::GetEnvironmentVariable('PATH', 'User'); ^
    if ($path -notlike '*%%USERPROFILE%%\bin*') { ^
        [Environment]::SetEnvironmentVariable('PATH', ^
        [Environment]::GetEnvironmentVariable('PATH', 'User') + ';%%USERPROFILE%%\bin', ^
        'User') ^
    }"

echo.
echo Installation complete!
echo Please restart your command prompt before using the cloc command.
echo Example usage: cloc -l c++ your_source_code_directory

:End
pause 