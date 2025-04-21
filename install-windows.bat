@echo off
echo 正在安装cloc工具...

:: 创建用户bin目录（如果不存在）
if not exist "%USERPROFILE%\bin" mkdir "%USERPROFILE%\bin"

:: 复制脚本和JAR文件
copy "%~dp0cloc.bat" "%USERPROFILE%\bin\cloc.bat"
copy "%~dp0cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "%USERPROFILE%\bin\"

:: 添加到PATH（如果尚未添加）
echo 正在检查PATH环境变量...
set PATH_TO_ADD=%USERPROFILE%\bin
echo %PATH% | findstr /C:"%PATH_TO_ADD%" > nul
if errorlevel 1 (
    setx PATH "%PATH%;%PATH_TO_ADD%"
    echo 已将%PATH_TO_ADD%添加到PATH环境变量
) else (
    echo %PATH_TO_ADD%已在PATH环境变量中
)

echo 安装完成！请重新打开命令提示符，然后使用"cloc"命令运行程序。
pause 