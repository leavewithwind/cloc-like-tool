#!/bin/bash
echo "Installing cloc tool..."

# 创建用户bin目录和程序专用子目录（如果不存在）
mkdir -p ~/bin/cloc-like-tool

# 复制JAR文件到专用目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "Copying files to ~/bin/cloc-like-tool..."
cp "$SCRIPT_DIR/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" ~/bin/cloc-like-tool/

# 创建启动脚本到bin目录，指向子目录中的JAR文件
echo "Creating launcher in ~/bin..."
cat > ~/bin/cloc << EOF
#!/bin/bash
java -jar "\$HOME/bin/cloc-like-tool/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "\$@"
EOF
chmod +x ~/bin/cloc

# 添加到PATH（如果尚未添加）
if [[ ":$PATH:" != *":$HOME/bin:"* ]]; then
    if [ -f ~/.bashrc ]; then
        echo 'export PATH="$HOME/bin:$PATH"' >> ~/.bashrc
        echo "Added to .bashrc"
    fi
    
    if [ -f ~/.zshrc ]; then
        echo 'export PATH="$HOME/bin:$PATH"' >> ~/.zshrc
        echo "Added to .zshrc"
    fi
    
    echo "Please run 'source ~/.bashrc' or reopen terminal to update PATH"
else
    echo "~/bin directory is already in PATH"
fi

echo
echo "======================================================"
echo "Installation complete!"
echo "======================================================"
echo
echo "IMPORTANT: You need to restart your terminal or run:"
echo "  source ~/.bashrc"
echo "for the PATH changes to take effect."
echo 