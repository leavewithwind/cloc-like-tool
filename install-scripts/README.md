# 安装脚本目录

本目录包含用于安装、运行和卸载本代码统计工具的脚本文件。

## 目录结构

- `unix/`: 包含Unix/Linux/macOS系统使用的脚本
  - `install.sh`: 安装程序到用户目录的脚本
  - `uninstall.sh`: 卸载程序的脚本
  
- `windows/`: 包含Windows系统使用的脚本
  - `install.bat`: 安装程序到用户目录的脚本
  - `uninstall.bat`: 卸载程序的脚本

## 脚本使用说明

### 安装

脚本会自动将编译好的JAR文件安装到用户目录中，并设置适当的路径环境变量，用户可以直接使用`cloc`命令。

#### 对于Unix/Linux/macOS系统：

```bash
chmod +x install-scripts/unix/install.sh
./install-scripts/unix/install.sh
```

安装脚本会：
1. 在`~/bin`目录中创建`cloc-like-tool`子目录
2. 将可执行JAR文件复制到该子目录
3. 创建启动脚本`cloc`到`~/bin`目录
4. 将`~/bin`添加到系统PATH环境变量（如果尚未添加）

#### 对于Windows系统：

```bash
install-scripts\windows\install.bat
```

安装脚本会：
1. 在`%USERPROFILE%\bin`目录中创建`cloc-like-tool`子目录
2. 将可执行JAR文件复制到该子目录
3. 创建启动脚本`cloc.bat`到`%USERPROFILE%\bin`目录
4. 将`%USERPROFILE%\bin`添加到系统PATH环境变量

### 安装后使用

安装完成后，重新打开命令行终端，即可在任何目录下使用以下命令：

```bash
cloc -l <语言> 路径/到/代码目录或文件
```

例如：
```bash
# 统计C/C++代码行数
cloc -l c++ /path/to/cpp/project

# 统计Ruby代码行数
cloc -l ruby /path/to/ruby/files
```

### 卸载

#### 对于Unix/Linux/macOS系统：

```bash
./install-scripts/unix/uninstall.sh
```

卸载脚本会：
1. 删除`~/bin/cloc`和`~/bin/cloc-like-tool`目录；
2. 如果`~/bin`目录为空，则提示从PATH中移除环境变量如用户需要。
#### 对于Windows系统：

```bash
install-scripts\windows\uninstall.bat
```

卸载脚本会：
1. 删除`%USERPROFILE%\bin\cloc.bat`和`%USERPROFILE%\bin\cloc-like-tool`目录；
2. 如果`%USERPROFILE%\bin`目录为空则会删除，提示从PATH中移除环境变量如用户需要。

## 安装位置

- **Unix/Linux/macOS**：程序安装在`~/bin/cloc-like-tool`目录
- **Windows**：程序安装在`%USERPROFILE%\bin\cloc-like-tool`目录

## 故障排除

如果安装后无法使用`cloc`命令：

1. 确保你已经重新打开了命令行终端
2. 检查环境变量PATH中是否包含以下路径：
   - Unix/Linux/macOS: `~/bin`
   - Windows: `%USERPROFILE%\bin`
3. 检查安装目录中是否包含必要的文件
