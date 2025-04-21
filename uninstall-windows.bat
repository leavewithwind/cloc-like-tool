@echo off
echo Uninstalling cloc tool...

:: 删除已安装的文件和目录
echo Removing installed files...
if exist "%USERPROFILE%\bin\cloc.bat" del "%USERPROFILE%\bin\cloc.bat"
if exist "%USERPROFILE%\bin\cloc-like-tool" (
    rmdir /s /q "%USERPROFILE%\bin\cloc-like-tool"
    echo Removed directory: %USERPROFILE%\bin\cloc-like-tool
)

:: 检查bin目录是否为空
echo Checking if %USERPROFILE%\bin contains other files...
dir /b /a "%USERPROFILE%\bin\*.*" > "%TEMP%\bin_contents.txt" 2>nul
set IS_EMPTY=1
for /f %%F in ('type "%TEMP%\bin_contents.txt" 2^>nul') do set IS_EMPTY=0
del "%TEMP%\bin_contents.txt" 2>nul

:: 根据目录是否为空决定是否从PATH中移除
if %IS_EMPTY%==1 (
    echo %USERPROFILE%\bin is empty, removing from PATH...
    :: 使用PowerShell移除PATH中的目录（处理PATH过长的情况）
    echo Removing %USERPROFILE%\bin from PATH using PowerShell method...
    powershell -Command "$currentPath = [Environment]::GetEnvironmentVariable('PATH', 'User'); $binPath = [regex]::Escape($env:USERPROFILE + '\bin'); $newPath = ($currentPath -split ';' | Where-Object { $_ -notmatch \"^$binPath$\" }) -join ';'; [Environment]::SetEnvironmentVariable('PATH', $newPath, 'User'); if ($currentPath -ne $newPath) { Write-Host 'Successfully removed %USERPROFILE%\bin from PATH' } else { Write-Host '%USERPROFILE%\bin not found in PATH or already removed' }"

    :: 如果PowerShell方法失败，则尝试VBS方法（针对某些没有PowerShell的旧版Windows）
    if %ERRORLEVEL% NEQ 0 (
        echo PowerShell method failed, trying VBScript method...
        
        :: 创建临时VBS脚本来修改PATH
        echo Set oWS = WScript.CreateObject("WScript.Shell") > "%TEMP%\temppath_uninstall.vbs"
        echo sOldUserPath = oWS.ExpandEnvironmentStrings("%%PATH%%") >> "%TEMP%\temppath_uninstall.vbs"
        echo sUserPath = sOldUserPath >> "%TEMP%\temppath_uninstall.vbs"
        echo sPathToRemove = "%USERPROFILE:\=\\%\\bin;" >> "%TEMP%\temppath_uninstall.vbs"
        echo If InStr(1, sUserPath, sPathToRemove, 1) > 0 Then >> "%TEMP%\temppath_uninstall.vbs"
        echo   sUserPath = Replace(sUserPath, sPathToRemove, "") >> "%TEMP%\temppath_uninstall.vbs"
        echo   oWS.Environment("USER").Item("PATH") = sUserPath >> "%TEMP%\temppath_uninstall.vbs"
        echo   oWS.RegWrite "HKCU\Environment\PATH", sUserPath, "REG_EXPAND_SZ" >> "%TEMP%\temppath_uninstall.vbs"
        echo   WScript.Echo "Removed from PATH: %USERPROFILE%\bin" >> "%TEMP%\temppath_uninstall.vbs"
        echo Else >> "%TEMP%\temppath_uninstall.vbs"
        echo   sPathToRemove = ";%USERPROFILE:\=\\%\\bin" >> "%TEMP%\temppath_uninstall.vbs"
        echo   If InStr(1, sUserPath, sPathToRemove, 1) > 0 Then >> "%TEMP%\temppath_uninstall.vbs"
        echo     sUserPath = Replace(sUserPath, sPathToRemove, "") >> "%TEMP%\temppath_uninstall.vbs"
        echo     oWS.Environment("USER").Item("PATH") = sUserPath >> "%TEMP%\temppath_uninstall.vbs"
        echo     oWS.RegWrite "HKCU\Environment\PATH", sUserPath, "REG_EXPAND_SZ" >> "%TEMP%\temppath_uninstall.vbs"
        echo     WScript.Echo "Removed from PATH: %USERPROFILE%\bin" >> "%TEMP%\temppath_uninstall.vbs"
        echo   Else >> "%TEMP%\temppath_uninstall.vbs"
        echo     WScript.Echo "%USERPROFILE%\bin not found in PATH or already removed" >> "%TEMP%\temppath_uninstall.vbs"
        echo   End If >> "%TEMP%\temppath_uninstall.vbs"
        echo End If >> "%TEMP%\temppath_uninstall.vbs"

        :: 执行临时脚本
        cscript //nologo "%TEMP%\temppath_uninstall.vbs"
        del "%TEMP%\temppath_uninstall.vbs"
    )

    :: 检查bin目录是否为空，如果是则询问是否删除
    echo Checking if %USERPROFILE%\bin is empty...
    dir /b "%USERPROFILE%\bin\*.*" > nul 2>&1
    if errorlevel 1 (
        echo %USERPROFILE%\bin is empty.
        choice /c YN /m "Do you want to remove the empty bin directory"
        if errorlevel 1 if not errorlevel 2 (
            rmdir "%USERPROFILE%\bin"
            echo Removed empty directory: %USERPROFILE%\bin
        )
    )
) else (
    echo %USERPROFILE%\bin contains other files or directories.
    echo Keeping %USERPROFILE%\bin in PATH for other applications.
)

:: 通知环境变量已更改
echo Notifying Windows of environment changes...
powershell -Command "(New-Object -ComObject Shell.Application).Windows() | ForEach-Object { $_.Refresh() }"

echo.
echo ======================================================
echo Uninstallation complete!
echo ======================================================
echo.
echo IMPORTANT: You need to restart all command prompts for
echo the PATH changes to take effect.
echo.
pause 