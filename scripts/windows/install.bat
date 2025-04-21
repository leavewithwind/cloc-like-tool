@echo off
echo Installing cloc tool...

:: 获取脚本目录和项目根目录
set SCRIPT_DIR=%~dp0
set ROOT_DIR=%SCRIPT_DIR%..\..

:: 创建用户bin目录和程序专用子目录（如果不存在）
if not exist "%USERPROFILE%\bin" mkdir "%USERPROFILE%\bin"
if not exist "%USERPROFILE%\bin\cloc-like-tool" mkdir "%USERPROFILE%\bin\cloc-like-tool"

:: 复制脚本和JAR文件到专用目录
echo Copying files to %USERPROFILE%\bin\cloc-like-tool...
copy "%ROOT_DIR%\target\cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "%USERPROFILE%\bin\cloc-like-tool\"

:: 创建启动脚本到bin目录，指向子目录中的JAR文件
echo Creating launcher in %USERPROFILE%\bin...
echo @echo off > "%USERPROFILE%\bin\cloc.bat"
echo java -jar "%%USERPROFILE%%\bin\cloc-like-tool\cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" %%* >> "%USERPROFILE%\bin\cloc.bat"

:: 使用PowerShell添加到PATH - 避免PATH过长问题
echo Adding %USERPROFILE%\bin to PATH using PowerShell method...
powershell -Command "$currentPath = [Environment]::GetEnvironmentVariable('PATH', 'User'); $binPath = $env:USERPROFILE + '\bin'; if ($currentPath -split ';' | Where-Object { $_ -eq $binPath }) { Write-Host '%USERPROFILE%\bin is already in PATH' } else { $newPath = $binPath + ';' + $currentPath; [Environment]::SetEnvironmentVariable('PATH', $newPath, 'User'); Write-Host 'Successfully added %USERPROFILE%\bin to PATH' }"

:: 如果PowerShell方法失败，则尝试VBS方法
if %ERRORLEVEL% NEQ 0 (
    echo PowerShell method failed, trying VBScript method...
    
    :: 创建临时VBS脚本来修改PATH而不受字符限制
    set PATH_TO_ADD=%USERPROFILE%\bin
    echo Set oWS = WScript.CreateObject("WScript.Shell") > "%TEMP%\temppath.vbs"
    echo sUserPath = oWS.ExpandEnvironmentStrings("%%PATH%%") >> "%TEMP%\temppath.vbs"
    echo If InStr(1, ";" ^& sUserPath ^& ";", ";" ^& "%PATH_TO_ADD:\=\\%" ^& ";", 1) = 0 Then >> "%TEMP%\temppath.vbs"
    echo   sUserPath = "%PATH_TO_ADD%" ^& ";" ^& sUserPath >> "%TEMP%\temppath.vbs"
    echo   oWS.Environment("USER").Item("PATH") = sUserPath >> "%TEMP%\temppath.vbs"
    echo   oWS.RegWrite "HKCU\Environment\PATH", sUserPath, "REG_EXPAND_SZ" >> "%TEMP%\temppath.vbs"
    echo   WScript.Echo "Added to PATH: %PATH_TO_ADD%" >> "%TEMP%\temppath.vbs"
    echo Else >> "%TEMP%\temppath.vbs"
    echo   WScript.Echo "%PATH_TO_ADD% is already in PATH" >> "%TEMP%\temppath.vbs"
    echo End If >> "%TEMP%\temppath.vbs"

    :: 执行临时脚本
    cscript //nologo "%TEMP%\temppath.vbs"
    del "%TEMP%\temppath.vbs"
)

:: 通知环境变量已更改
echo Notifying Windows of environment changes...
powershell -Command "(New-Object -ComObject Shell.Application).Windows() | ForEach-Object { $_.Refresh() }"

echo.
echo ======================================================
echo Installation complete! 
echo ======================================================
echo.
echo IMPORTANT: You need to restart all command prompts for
echo the PATH changes to take effect.
echo.
pause 