#!/bin/bash
echo "Uninstalling cloc tool..."

# 删除安装的文件
if [ -d ~/bin/cloc-like-tool ]; then
    echo "Removing cloc tool files..."
    rm -rf ~/bin/cloc-like-tool
    echo "Removed installed files."
else
    echo "cloc tool files not found in ~/bin/cloc-like-tool"
fi

# 删除启动脚本
if [ -f ~/bin/cloc ]; then
    echo "Removing launcher script..."
    rm ~/bin/cloc
    echo "Removed launcher script."
else
    echo "Launcher script not found in ~/bin"
fi

# 提示用户手动清理PATH
echo
echo "======================================================"
echo "Uninstallation complete!"
echo "======================================================"
echo 
echo "NOTE: The script has removed all installed files."
echo "However, it did not modify your PATH environment variable."
echo
echo "If you no longer need the ~/bin directory in your PATH,"
echo "please manually edit your ~/.bashrc, ~/.zshrc or other shell"
echo "configuration files to remove it."
echo 