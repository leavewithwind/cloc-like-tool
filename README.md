# 代码行统计工具 (CLOC)

这是一个用于统计C/C++和Ruby源文件中的代码行、注释行和空行数量的工具。

## 功能特性

- 支持C/C++文件（.c, .cpp, .cc）的代码行统计
- 支持Ruby文件（.rb）的代码行统计
- 递归遍历目录
- 正确处理注释和代码混合的行
- 可以按语言分别查看统计结果
- 处理字符串中的注释字符
- 支持多种注释风格（C/C++的//和/* */，Ruby的#和=begin/=end）
- 完善的异常处理机制，提供友好的错误信息

## 项目架构

项目采用了模块化的面向对象结构设计，使用以下设计模式：

- **策略模式**：使用`LineCounter`抽象类定义代码行计数行为，不同语言的计数器实现该类
- **工厂模式**：使用`LineCounterFactory`根据命令行输入创建适当的计数器
- **异常处理模式**：使用自定义异常层次结构和统一的异常处理机制

### 目录结构

```
cloc-like-tool/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── clocliketool/
│   │   │           ├── Main.java                      # 程序入口点
│   │   │           ├── ApplicationRunner.java         # 应用程序执行器
│   │   │           ├── model/                         # 模型类
│   │   │           │   └── LineCountResult.java       # 计数结果模型
│   │   │           ├── counter/                       # 计数器模块
│   │   │           │   ├── LineCounter.java           # 抽象基础计数器类
│   │   │           │   ├── CLineCounter.java         # C/C++实现
│   │   │           │   ├── RubyLineCounter.java       # Ruby实现
│   │   │           │   └── LineCounterFactory.java    # 计数器工厂
│   │   │           ├── analyzer/                      # 分析器模块
│   │   │           │   ├── FileAnalyzer.java          # 文件分析器
│   │   │           │   └── LanguageMapper.java        # 语言映射器
│   │   │           ├── cli/                           # 命令行处理模块
│   │   │           │   └── CommandLineProcessor.java  # 命令行处理器
│   │   │           ├── exception/                     # 异常处理模块
│   │   │           │   ├── LineCounterException.java  # 基础异常类
│   │   │           │   ├── UnsupportedLanguageException.java  # 不支持语言异常
│   │   │           │   ├── FileProcessingException.java  # 文件处理异常
│   │   │           │   ├── InvalidArgumentException.java  # 无效参数异常
│   │   │           │   ├── ErrorCode.java            # 错误代码枚举
│   │   │           │   └── ExceptionHandler.java     # 异常处理工具类
│   │   │           └── util/                          # 工具类模块
│   │   │               ├── DirectoryScanner.java      # 目录扫描器
│   │   │               └── ResultFormatter.java       # 结果格式化工具
│   └── test/
│       ├── java/                                     # 测试代码目录
│       └── resources/                                # 测试资源目录
│           └── testdata/                             # 测试数据文件
│               ├── cpp/                              # C/C++测试文件
│               └── ruby/                             # Ruby测试文件
├── scripts/                                          # 脚本文件目录
│   ├── unix/                                         # Unix/Linux/macOS脚本
│   │   ├── cloc.sh                                   # 运行程序的脚本
│   │   ├── install.sh                                # 安装程序的脚本
│   │   └── uninstall.sh                              # 卸载程序的脚本
│   ├── windows/                                      # Windows脚本
│   │   ├── cloc.bat                                  # 运行程序的脚本
│   │   ├── install.bat                               # 安装程序的脚本
│   │   └── uninstall.bat                             # 卸载程序的脚本
│   ├── test-ruby-count.sh                           # 测试Ruby计数的脚本
│   └── README.md                                     # 脚本目录说明文档
├── target/                                           # 编译输出目录（自动生成）
├── cloc.sh                                           # 根目录运行脚本（转发到scripts/unix）
├── cloc.bat                                          # 根目录运行脚本（转发到scripts/windows）
├── pom.xml                                           # Maven配置文件
├── release.xml                                       # 发布包配置文件
└── README.md                                         # 项目说明文档
```

## 设计原则

本项目遵循以下设计原则：

1. **单一职责原则**：每个类都有明确的单一职责
2. **开闭原则**：代码对扩展开放，对修改关闭，易于添加新的语言支持
3. **依赖倒置原则**：高层模块不依赖低层模块，通过抽象接口交互
4. **接口隔离原则**：通过适当粒度的接口设计，确保依赖最小化
5. **最少知识原则**：各模块之间通过最少的接口通信
6. **关注点分离**：将数据存储、业务逻辑和UI展示分离
7. **异常处理原则**：使用明确的异常类型和统一的异常处理机制

## 主要类的职责

1. **Main** - 应用程序入口点
2. **ApplicationRunner** - 控制程序整体流程
3. **CommandLineProcessor** - 处理命令行参数
4. **LineCounter** - 抽象类，定义计数行为
5. **LineCounterFactory** - 创建适当的计数器实例
6. **FileAnalyzer** - 分析文件和目录
7. **LanguageMapper** - 将文件扩展名映射到编程语言
8. **DirectoryScanner** - 遍历目录
9. **ResultFormatter** - 格式化输出结果
10. **ExceptionHandler** - 统一处理异常

## 异常处理框架

本项目实现了一个完整的异常处理框架，确保程序能够优雅地处理各种错误情况：

### 异常类体系

- **LineCounterException** - 基础异常类，所有自定义异常的父类
- **UnsupportedLanguageException** - 当指定了不支持的语言时抛出
- **FileProcessingException** - 当文件处理过程中出现问题时抛出
- **InvalidArgumentException** - 当提供的参数无效时抛出

### 错误代码管理

使用**ErrorCode**枚举定义了标准化的错误代码和消息：

| 错误代码 | 描述 | 返回值 |
|---------|------|-------|
| SUCCESS | 操作成功 | 0 |
| COMMAND_LINE_ERROR | 命令行参数错误 | 1 |
| UNSUPPORTED_LANGUAGE | 不支持的语言 | 2 |
| PATH_NOT_FOUND | 文件或目录不存在 | 3 |
| FILE_PROCESSING_ERROR | 文件处理错误 | 4 |
| UNKNOWN_ERROR | 未知错误 | 99 |

### 统一的异常处理

**ExceptionHandler**类提供了统一的异常处理机制：
- 根据异常类型确定适当的错误代码
- 生成友好的错误消息
- 对不同类型的异常提供不同级别的详细信息
- 仅在必要时显示堆栈跟踪

## 系统要求

- Java JDK 11或更高版本
- Windows、macOS 或 Linux 操作系统

## 安装与使用

### 构建项目

#### 前提条件

- Java JDK 11或更高版本
- Maven 3.6或更高版本

#### 构建步骤

```bash
# 克隆项目
git clone https://your-repository-url/cloc-like-tool.git
cd cloc-like-tool

# 使用Maven构建
mvn clean package
```

构建完成后，可在`target`目录中找到两种类型的文件：
- 可执行JAR文件：`cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`
- 发布包：`cloc-like-tool-1.0-SNAPSHOT-release.zip`（Windows用）和`cloc-like-tool-1.0-SNAPSHOT-release.tar.gz`（Unix/Linux/macOS用）

### 使用JAR文件直接运行

```bash
java -jar target/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar [选项] <路径1> [<路径2> ...]
```

#### 选项

- `-l`, `--language <语言>`: 指定要统计的语言（支持: c++, ruby）

#### 示例

```bash
# 统计指定目录中的C/C++文件
java -jar cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar -l c++ /path/to/cpp/project

# 统计指定目录中的Ruby文件
java -jar cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar -l ruby /path/to/ruby/project

# 统计多个目录
java -jar cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar -l c++ /path/to/dir1 /path/to/dir2
```

### 简化命令执行（使用"cloc"命令）

为了使用更简便的`cloc`命令，您可以安装发布包。

#### Windows安装

1. 解压下载的`cloc-like-tool-1.0-SNAPSHOT-release.zip`
2. 双击运行 `scripts/windows/install.bat`
3. 脚本会自动:
   - 在`%USERPROFILE%\bin`目录中创建`cloc-like-tool`子目录
   - 将JAR文件复制到该子目录
   - 创建启动脚本`cloc.bat`到`%USERPROFILE%\bin`目录
   - 将`%USERPROFILE%\bin`添加到系统PATH环境变量
4. 重新打开命令提示符或PowerShell窗口
5. 输入 `cloc` 测试安装是否成功

#### macOS/Linux安装

1. 解压下载的`cloc-like-tool-1.0-SNAPSHOT-release.tar.gz`
2. 打开终端，导航到解压目录
3. 执行以下命令:
   ```bash
   chmod +x scripts/unix/install.sh
   ./scripts/unix/install.sh
   ```
4. 脚本会自动:
   - 在`~/bin`目录中创建`cloc-like-tool`子目录
   - 将JAR文件复制到该子目录
   - 创建启动脚本`cloc`到`~/bin`目录
   - 将`~/bin`添加到系统PATH环境变量
5. 重新打开终端或执行 `source ~/.bashrc` 更新环境变量
6. 输入 `cloc` 测试安装是否成功

#### 安装后的使用方法

安装完成后，您可以使用以下简化命令运行CLOC工具:

```bash
# 统计C/C++代码
cloc -l c++ /path/to/source/code

# 统计Ruby代码
cloc -l ruby /path/to/source/code
```

#### 手动安装（如果自动脚本不工作）

##### Windows手动安装

1. 创建目录结构:
   ```cmd
   mkdir "%USERPROFILE%\bin"
   mkdir "%USERPROFILE%\bin\cloc-like-tool"
   ```

2. 复制文件:
   - 将JAR文件复制到`%USERPROFILE%\bin\cloc-like-tool`目录
   - 创建批处理文件`%USERPROFILE%\bin\cloc.bat`，内容为:
     ```
     @echo off
     java -jar "%USERPROFILE%\bin\cloc-like-tool\cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" %*
     ```

3. 将目录添加到系统PATH环境变量:
   - 右键点击「此电脑」-> 属性 -> 高级系统设置 -> 环境变量
   - 找到「Path」变量，点击编辑，添加`%USERPROFILE%\bin`
   - 点击确定保存更改

##### macOS/Linux手动安装

1. 创建目录结构:
   ```bash
   mkdir -p ~/bin/cloc-like-tool
   ```

2. 复制文件:
   ```bash
   cp cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar ~/bin/cloc-like-tool/
   cat > ~/bin/cloc << 'EOF'
   #!/bin/bash
   java -jar "$HOME/bin/cloc-like-tool/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "$@"
   EOF
   chmod +x ~/bin/cloc
   ```

3. 添加到PATH:
   ```bash
   echo 'export PATH="$HOME/bin:$PATH"' >> ~/.bashrc
   source ~/.bashrc
   ```

### 卸载工具

卸载工具时，系统会进行以下操作：

#### Windows卸载
1. 删除`%USERPROFILE%\bin\cloc.bat`和`%USERPROFILE%\bin\cloc-like-tool`目录
2. 检查`%USERPROFILE%\bin`目录中是否还有其他文件或目录
   - 如果目录为空，则从PATH中移除`%USERPROFILE%\bin`并询问是否删除空目录
   - 如果目录中还有其他文件或目录（可能是其他工具），则保留`%USERPROFILE%\bin`在PATH中

#### macOS/Linux卸载
1. 删除`~/bin/cloc`和`~/bin/cloc-like-tool`目录
2. 检查`~/bin`目录中是否还有其他文件或目录
   - 如果目录为空，则从`~/.bashrc`和`~/.zshrc`中移除PATH条目并询问是否删除空目录
   - 如果目录中还有其他文件或目录（可能是其他工具），则保留`~/bin`在PATH中

#### 卸载命令
```bash
# Windows
scripts/windows/uninstall.bat

# macOS/Linux
./scripts/unix/uninstall.sh
```

### 环境变量过长问题解决方案

在Windows系统中，环境变量有长度限制。在安装或卸载过程中，如果您遇到"环境变量过长"的错误提示，请使用以下PowerShell命令手动添加或移除PATH条目：

#### 手动添加PATH（Windows安装）

1. 按Win+X，选择"Windows PowerShell"或"PowerShell"
2. 运行以下命令：
   ```powershell
   $currentPath = [Environment]::GetEnvironmentVariable('PATH', 'User')
   $newPath = "$env:USERPROFILE\bin;" + $currentPath
   [Environment]::SetEnvironmentVariable('PATH', $newPath, 'User')
   ```

#### 手动移除PATH（Windows卸载）

1. 按Win+X，选择"Windows PowerShell"或"PowerShell"
2. 运行以下命令：
   ```powershell
   $currentPath = [Environment]::GetEnvironmentVariable('PATH', 'User')
   $newPath = ($currentPath -split ';' | Where-Object { $_ -ne "$env:USERPROFILE\bin" }) -join ';'
   [Environment]::SetEnvironmentVariable('PATH', $newPath, 'User')
   ```

## 输出格式

```
-----------------------------------------------------
     Files      Lines       Code   Comments     Blanks
-----------------------------------------------------
       32       7153       6228        442        483
-----------------------------------------------------
```

## 错误码说明

当程序遇到错误时，会返回以下退出码：

- **0**: 成功执行
- **1**: 命令行参数错误
- **2**: 不支持的语言
- **3**: 文件或目录不存在
- **4**: 文件处理错误
- **99**: 未知错误

## 扩展支持新的语言

要添加对新语言的支持，需要执行以下步骤：

1. 创建一个继承`LineCounter`的新计数器类
2. 在`LineCounterFactory`中注册新的计数器
3. 在`LanguageMapper`中添加新的文件扩展名到语言的映射
4. 如果需要特殊的异常处理，可以在异常框架中添加新的异常类型

## 许可证

本项目采用MIT许可证。 