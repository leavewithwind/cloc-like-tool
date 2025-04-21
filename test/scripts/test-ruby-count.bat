@echo off
setlocal enabledelayedexpansion

REM 获取脚本目录和项目根目录
set "SCRIPT_DIR=%~dp0"
set "ROOT_DIR=%SCRIPT_DIR%..\..\"
cd "%ROOT_DIR%"

REM 创建测试资源目录（如果不存在）
if not exist "test\resources" mkdir "test\resources" 2>nul

REM 确保源代码编译好
echo 编译项目...
cd "%ROOT_DIR%"
call mvn clean package -DskipTests

REM 测试新创建的Ruby测试文件
set "TEST_FILE=%ROOT_DIR%test\resources\ruby-test-cases.rb"

echo --------------------------------------------------
echo 测试文件: %TEST_FILE%
echo --------------------------------------------------

echo 使用我的工具统计:
java -jar "%ROOT_DIR%target\cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "%TEST_FILE%"

echo --------------------------------------------------
echo 使用cloc工具统计:
cloc "%TEST_FILE%"

echo --------------------------------------------------
echo 对比总结:
echo --------------------------------------------------

REM 获取我们工具的结果
for /f "tokens=*" %%a in ('java -jar "%ROOT_DIR%target\cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "%TEST_FILE%" ^| findstr "Ruby"') do (
    set OUR_LINE=%%a
)

REM 解析结果
for /f "tokens=2,3,4,5" %%a in ("!OUR_LINE!") do (
    set OUR_FILES=%%a
    set OUR_BLANKS=%%b
    set OUR_COMMENTS=%%c
    set OUR_CODE=%%d
)

REM 获取cloc工具的结果
for /f "tokens=*" %%a in ('cloc "%TEST_FILE%" ^| findstr "Ruby"') do (
    set CLOC_LINE=%%a
)

REM 解析结果
for /f "tokens=2,3,4,5" %%a in ("!CLOC_LINE!") do (
    set CLOC_FILES=%%a
    set CLOC_BLANKS=%%b
    set CLOC_COMMENTS=%%c
    set CLOC_CODE=%%d
)

echo 我程序的结果 (文件数 空行数 注释行数 代码行数): !OUR_FILES! !OUR_BLANKS! !OUR_COMMENTS! !OUR_CODE!
echo cloc结果 (文件数 空行数 注释行数 代码行数): !CLOC_FILES! !CLOC_BLANKS! !CLOC_COMMENTS! !CLOC_CODE!

REM 分析结果是否一致
if "!OUR_COMMENTS!"=="!CLOC_COMMENTS!" if "!OUR_CODE!"=="!CLOC_CODE!" (
    echo 结果一致！注释行数和代码行数都匹配。
) else (
    echo 结果不一致！
    echo 注释行数差异: 我的工具=!OUR_COMMENTS!, cloc=!CLOC_COMMENTS!
    echo 代码行数差异: 我的工具=!OUR_CODE!, cloc=!CLOC_CODE!
)

endlocal 