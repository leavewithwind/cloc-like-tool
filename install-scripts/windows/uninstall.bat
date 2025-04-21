@echo off
echo Uninstalling cloc tool...

:: 删除安装的文件
if exist "%USERPROFILE%\bin\cloc-like-tool" (
    echo Removing cloc tool files...
    rmdir /S /Q "%USERPROFILE%\bin\cloc-like-tool"
    echo Removed installed files.
) else (
    echo cloc tool files not found in %USERPROFILE%\bin\cloc-like-tool
)

:: 删除启动脚本
if exist "%USERPROFILE%\bin\cloc.bat" (
    echo Removing launcher script...
    del "%USERPROFILE%\bin\cloc.bat"
    echo Removed launcher script.
) else (
    echo Launcher script not found in %USERPROFILE%\bin
)

:: 检查bin目录是否为空，如果是，则删除
dir /a "%USERPROFILE%\bin\*" >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo bin directory is empty, removing it...
    rmdir "%USERPROFILE%\bin"
    echo Removed bin directory.
)

:: 提示用户已从PATH中删除的信息
echo.
echo ======================================================
echo Uninstallation complete!
echo ======================================================
echo.
echo NOTE: The script has removed all installed files.
echo However, it did not modify your PATH environment variable.
echo.
echo If you no longer need the %USERPROFILE%\bin directory in your PATH,
echo please manually edit your environment variables to remove it:
echo.
echo 1. Right-click on This PC/My Computer and select Properties
echo 2. Click on Advanced system settings
echo 3. Click on Environment Variables
echo 4. Under User variables, edit the PATH variable
echo 5. Remove "%USERPROFILE%\bin" entry
echo.
pause 