# 脚本目录

本目录包含用于安装、运行和测试CLOC工具的脚本文件。

## 目录结构

- `unix/`: 包含Unix/Linux/macOS系统使用的脚本
  - `cloc.sh`: 运行程序的脚本
  - `install.sh`: 安装程序到用户目录的脚本
  - `uninstall.sh`: 卸载程序的脚本
  
- `windows/`: 包含Windows系统使用的脚本
  - `cloc.bat`: 运行程序的脚本
  - `install.bat`: 安装程序到用户目录的脚本
  - `uninstall.bat`: 卸载程序的脚本
  
- `test-ruby-count.sh`: 测试Ruby文件计数功能与官方cloc工具的一致性

## 脚本使用说明

### 安装

对于Unix/Linux/macOS系统：
```
./scripts/unix/install.sh
```

对于Windows系统：
```
.\scripts\windows\install.bat
```

### 运行

安装后可以在任何地方使用`cloc`命令:
```
cloc 路径/到/代码目录或文件
```

或者直接使用脚本运行:
```
# Unix/Linux/macOS
./scripts/unix/cloc.sh 路径/到/代码目录或文件

# Windows
.\scripts\windows\cloc.bat 路径\到\代码目录或文件
```

### 卸载

对于Unix/Linux/macOS系统：
```
./scripts/unix/uninstall.sh
```

对于Windows系统：
```
.\scripts\windows\uninstall.bat
``` 