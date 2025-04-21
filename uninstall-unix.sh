#!/bin/bash
echo "Uninstalling cloc tool..."

# 删除已安装的文件和目录
echo "Removing installed files..."
rm -f ~/bin/cloc
if [ -d ~/bin/cloc-like-tool ]; then
    rm -rf ~/bin/cloc-like-tool
    echo "Removed directory: ~/bin/cloc-like-tool"
fi

# 检查bin目录是否还包含其他文件或目录
echo "Checking if ~/bin contains other files or directories..."
if [ -n "$(ls -A ~/bin 2>/dev/null)" ]; then
    echo "~/bin contains other files or directories."
    echo "Keeping ~/bin in PATH for other applications."
else
    echo "~/bin is empty, removing from PATH..."
    
    # 从配置文件中移除PATH条目
    echo "Removing PATH entries from shell configuration files..."

    # 处理.bashrc文件
    if [ -f ~/.bashrc ]; then
        grep -v 'export PATH="$HOME/bin:$PATH"' ~/.bashrc > ~/.bashrc.temp
        mv ~/.bashrc.temp ~/.bashrc
        echo "Updated .bashrc file"
    fi

    # 处理.zshrc文件
    if [ -f ~/.zshrc ]; then
        grep -v 'export PATH="$HOME/bin:$PATH"' ~/.zshrc > ~/.zshrc.temp
        mv ~/.zshrc.temp ~/.zshrc
        echo "Updated .zshrc file"
    fi

    # 检查bin目录是否为空，如果是则询问是否删除
    echo "Checking if ~/bin is empty..."
    if [ -z "$(ls -A ~/bin 2>/dev/null)" ]; then
        echo "~/bin directory is empty."
        read -p "Do you want to remove the empty bin directory? (y/n): " answer
        if [[ "$answer" == "y" || "$answer" == "Y" ]]; then
            rmdir ~/bin
            echo "Removed empty directory: ~/bin"
        fi
    fi
fi

echo
echo "======================================================"
echo "Uninstallation complete!"
echo "======================================================"
echo
echo "IMPORTANT: You need to restart your terminal or run:"
echo "  source ~/.bashrc"
echo "for the PATH changes to take effect."
echo 