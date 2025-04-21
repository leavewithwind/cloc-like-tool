#!/bin/bash
echo "正在安装cloc工具..."

# 创建用户bin目录（如果不存在）
mkdir -p ~/bin

# 复制脚本和JAR文件
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cp "$SCRIPT_DIR/cloc.sh" ~/bin/cloc
cp "$SCRIPT_DIR/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" ~/bin/
chmod +x ~/bin/cloc

# 添加到PATH（如果尚未添加）
if [[ ":$PATH:" != *":$HOME/bin:"* ]]; then
    if [ -f ~/.bashrc ]; then
        echo 'export PATH="$HOME/bin:$PATH"' >> ~/.bashrc
        echo "已添加到 .bashrc"
    fi
    
    if [ -f ~/.zshrc ]; then
        echo 'export PATH="$HOME/bin:$PATH"' >> ~/.zshrc
        echo "已添加到 .zshrc"
    fi
    
    echo "请运行 'source ~/.bashrc' 或重新打开终端以更新PATH"
else
    echo "~/bin 目录已在PATH中"
fi

echo "安装完成！请重新打开终端或执行 'source ~/.bashrc'，然后使用'cloc'命令运行程序。" 