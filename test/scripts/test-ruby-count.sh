#!/bin/bash

# 获取脚本目录和项目根目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

mkdir -p "$ROOT_DIR/test/resources" 2>/dev/null

# 确保源代码编译好
echo "编译项目..."
cd "$ROOT_DIR"
mvn clean package -DskipTests

# 测试新创建的Ruby测试文件
TEST_FILE="$ROOT_DIR/test/resources/ruby-test-cases.rb"

echo "--------------------------------------------------"
echo "测试文件: $TEST_FILE"
echo "--------------------------------------------------"

echo "使用我的工具统计:"
java -jar "$ROOT_DIR/target/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "$TEST_FILE"

echo "--------------------------------------------------"
echo "使用cloc工具统计:"
cloc "$TEST_FILE"

echo "--------------------------------------------------"
echo "对比总结:"
echo "--------------------------------------------------"
OUR_RESULT=$(java -jar "$ROOT_DIR/target/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "$TEST_FILE" | grep "Ruby" | awk '{print $2, $3, $4, $5}')
CLOC_RESULT=$(cloc "$TEST_FILE" | grep "Ruby" | awk '{print $2, $3, $4, $5}')

echo "我程序的结果 (文件数 空行数 注释行数 代码行数): $OUR_RESULT"
echo "cloc结果 (文件数 空行数 注释行数 代码行数): $CLOC_RESULT"

# 分析结果是否一致
OUR_COMMENTS=$(echo "$OUR_RESULT" | awk '{print $3}')
CLOC_COMMENTS=$(echo "$CLOC_RESULT" | awk '{print $3}')

OUR_CODE=$(echo "$OUR_RESULT" | awk '{print $4}')
CLOC_CODE=$(echo "$CLOC_RESULT" | awk '{print $4}')

if [ "$OUR_COMMENTS" == "$CLOC_COMMENTS" ] && [ "$OUR_CODE" == "$CLOC_CODE" ]; then
    echo "结果一致！注释行数和代码行数都匹配。"
else
    echo "结果不一致！"
    echo "注释行数差异: 我的工具=${OUR_COMMENTS}, cloc=${CLOC_COMMENTS}"
    echo "代码行数差异: 我的工具=${OUR_CODE}, cloc=${CLOC_CODE}"
fi 