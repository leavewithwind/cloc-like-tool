# 代码行统计工具 (CLOC)

[![语言](https://img.shields.io/badge/语言-Java-orange.svg)](https://www.java.com/)
[![JDK版本](https://img.shields.io/badge/JDK-11+-green.svg)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![许可证](https://img.shields.io/badge/许可证-MIT-blue.svg)](https://opensource.org/licenses/MIT)

一个用于统计C/C++和Ruby源文件中的代码行、注释行和空行数量的简单而高效的命令行工具。

## 📋 目录

- [功能特性](#功能特性)
- [快速开始](#快速开始)
  - [前提条件](#前提条件)
  - [安装](#安装)
  - [使用方法](#使用方法)
  - [卸载](#卸载)
- [输出示例](#输出示例)
- [项目架构](#项目架构)
  - [目录结构](#目录结构)
  - [核心组件](#核心组件)
- [扩展指南](#扩展指南)
- [设计原则](#设计原则)
- [异常处理](#异常处理)
- [错误码参考](#错误码参考)
- [测试策略](#测试策略)

## 功能特性

✅ 支持C/C++文件（.c, .cpp, .cc）的代码行统计  
✅ 支持Ruby文件的代码行统计  
✅ 按指定语言检测特定语言的代码  
✅ 递归遍历目录分析源代码文件  
✅ 正确处理注释和代码混合的行  
✅ 排除字符串中的注释字符误识别  
✅ 正确识别多种注释风格（C/C++的//和/* */，Ruby的#和=begin/=end）  
✅ 简单友好的使用方式，提供详细的错误信息  
✅ 易于扩展以支持更多编程语言  

## 快速开始

### 环境依赖

- Java JDK 11或更高版本
- Maven 3.6或更高版本（用于构建项目）

### 安装

#### 从源码构建

```bash
# 克隆项目
git clone https://github.com/your-username/cloc-like-tool.git
cd cloc-like-tool

# 使用Maven构建
mvn clean package
```

构建完成后，在`target`目录中生成可执行JAR文件：`cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`

#### 快捷命令安装

##### Windows

1. 运行 `install-scripts/windows/install.bat`
2. 脚本会自动:
   - 在`%USERPROFILE%\bin`目录中创建`cloc-like-tool`子目录
   - 将可执行JAR文件复制到该子目录
   - 创建启动脚本`cloc.bat`到`%USERPROFILE%\bin`目录
   - 将`%USERPROFILE%\bin`添加到系统PATH环境变量

##### macOS/Linux

```bash
chmod +x install-scripts/unix/install.sh
./install-scripts/unix/install.sh
```

### 使用方法

#### 基本用法

进行安装后:

```bash
cloc [选项] <路径1> [<路径2> ...]
```

或者只使用"mvn clean package"生成JAR包后，在项目根目录终端中使用：

```bash
java -jar cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar [选项] <路径1> [<路径2> ...]
```

#### 参数选项

- `-l`, `--language <语言>`: 指定要统计的语言（支持: c++, ruby）

#### 使用示例
安装本工具后，通过命令行使用：

```bash
# 显示使用说明
cloc

# 统计C/C++文件
cloc -l c++ /path/to/cpp/project

# 统计Ruby文件
cloc -l ruby /path/to/ruby/project

# 统计多个目录
cloc -l c++ /path/to/dir1 /path/to/dir2
```

### 卸载

#### Windows
双击运行install-scripts/windows/目录下的uninstall.bat脚本，或：

```bash
install-scripts/windows/uninstall.bat
```

#### macOS/Linux

```bash
chmod +x install-scripts/unix/uninstall.sh
./install-scripts/unix/uninstall.sh
```

## 输出示例

```
-----------------------------------------------------
 Files     Lines        Code    Comments      Blanks
-----------------------------------------------------
    32      7153        6228         442         483
-----------------------------------------------------
```

## 项目架构

本项目采用模块化的面向对象结构设计，使用以下设计模式：

- **策略模式**：使用`LineCounter`抽象类定义代码行计数行为，不同语言的计数器实现该类
- **工厂模式**：使用`LineCounterFactory`根据命令行输入创建适当的计数器
- **异常处理模式**：使用自定义异常层次结构和统一的异常处理机制

### 目录结构

```
cloc-like-tool/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── clocliketool/
│                   ├── Main.java                      # 程序入口点
│                   ├── model/                         # 模型类
│                   │   └── LineCountResult.java       # 计数结果模型
│                   ├── counter/                       # 计数器模块
│                   │   ├── LineCounter.java           # 抽象基础计数器类
│                   │   ├── CLineCounter.java          # C/C++实现
│                   │   ├── RubyLineCounter.java       # Ruby实现
│                   │   └── LineCounterFactory.java    # 计数器工厂
│                   ├── analyzer/                      # 分析器模块
│                   │   └── FileAnalyzer.java          # 文件分析器
│                   ├── cli/                           # 命令行处理模块
│                   │   ├── CliTableFormatter.java     # 命令行表格格式化工具
│                   │   └── CommandLineProcessor.java  # 命令行处理器
│                   ├── exception/                     # 异常处理模块
│                   │   ├── StatusCode.java            # 状态代码枚举
│                   │   ├── LineCounterException.java  # 基础异常类
│                   │   ├── UnsupportedLanguageException.java # 语言不支持异常
│                   │   ├── FileProcessingException.java # 文件处理异常
│                   │   ├── InvalidArgumentException.java # 参数无效异常
│                   │   └── ExceptionHandler.java      # 异常处理工具类
│                   └── util/                          # 工具类模块
│                       ├── LanguageMapper.java        # 语言映射工具
│                       └── DirectoryScanner.java      # 目录扫描器
├── test/
│   ├── scripts/                                       # 测试脚本
│   └── resources/                                     # 测试资源文件
├── install-scripts/                                   # 安装脚本目录
│   ├── unix/                                          # Unix/Linux/macOS脚本
│   │   ├── install.sh                                 # 安装程序的脚本
│   │   └── uninstall.sh                               # 卸载程序的脚本
│   ├── windows/                                       # Windows脚本
│   │   ├── install.bat                                # 安装程序的脚本
│   │   └── uninstall.bat                              # 卸载程序的脚本
│   └── README.md                                      # 脚本目录说明文档
├── target/                                            # 编译输出目录（自动生成）
├── pom.xml                                            # Maven配置文件
└── README.md                                          # 项目说明文档
```

### 核心组件

1. **Main** - 应用程序入口点，控制程序整体流程
2. **CommandLineProcessor** - 处理命令行参数，解析用户输入
3. **LineCounter** - 抽象类，定义代码行计数行为
4. **LineCounterFactory** - 工厂方法，创建适当的计数器实例
5. **FileAnalyzer** - 分析文件和目录，统计代码行
6. **CliTableFormatter** - 格式化统计结果为表格输出
7. **DirectoryScanner** - 递归遍历目录，查找源码文件
8. **LanguageMapper** - 将文件名映射到对应的编程语言
9. **ExceptionHandler** - 统一异常处理器

## 扩展指南

要添加对新编程语言的支持，按照以下步骤操作：

1. 创建一个继承`LineCounter`的新计数器类
2. 在`LineCounterFactory`中注册新的计数器
3. 在`LanguageMapper`中添加新的文件扩展名到语言的映射
4. 实现对应语言的注释和代码行识别逻辑
5. 确保正确处理该语言特有的语法特性（字符串、注释等）

## 设计原则

本项目遵循了以下设计原则：

1. **单一职责原则**：每个类尽量保持单一职责
2. **开闭原则**：代码对扩展开放，对修改关闭，易于添加新的语言支持
3. **依赖倒置原则**：高层模块不依赖低层模块，通过抽象接口交互
4. **关注点分离**：将数据存储、业务逻辑和UI展示分离
5. **异常处理原则**：使用明确的异常类型和统一的异常处理机制

## 异常处理

本项目应用了异常处理框架，以优雅地处理错误情况：

### 异常类体系

- **LineCounterException** - 基础异常类，所有自定义异常的父类
- **UnsupportedLanguageException** - 当指定了不支持的语言时抛出
- **FileProcessingException** - 当文件处理过程中出现问题时抛出
- **InvalidArgumentException** - 当提供的参数无效时抛出

### 统一的异常处理流程

**ExceptionHandler**类提供了统一的异常处理机制：
- 根据异常类型确定适当的错误代码
- 对不同类型的异常提供不同级别的详细信息

## 错误码参考

| 错误代码 | 描述 | 返回值 |
|---------|------|-------|
| SUCCESS | 操作成功 | 0 |
| COMMAND_LINE_ERROR | 命令行参数错误 | 1 |
| UNSUPPORTED_LANGUAGE | 不支持的语言 | 2 |
| PATH_NOT_FOUND | 文件或目录不存在 | 3 |
| FILE_PROCESSING_ERROR | 文件处理错误 | 4 |
| FILE_NOT_FOUND | 找不到指定的文件 | 5 |
| ACCESS_DENIED | 无权访问指定的文件或目录 | 6 |
| IO_ERROR | 读取文件时发生I/O错误 | 7 |
| INVALID_ARGUMENT | 参数无效 | 8 |
| UNEXPECTED_ERROR | 发生未预期的异常 | 98 |
| UNKNOWN_ERROR | 未知错误 | 99 |

## 测试策略

本项目采用了特定的测试策略，主要依赖于功能测试而非传统的单元测试：

1. **比对式功能测试**：通过将本工具的计数结果与官方cloc工具的结果进行对比，验证计数算法的准确性。

2. **针对性与高效性**：端到端的测试方法能够直接验证核心功能，比单独测试每个组件更加高效。

3. **实际使用场景验证**：使用真实的代码文件作为输入，确保工具在实际使用场景中的可靠性。

4. **结果导向的质量保证**：对于计数工具而言，最终计数结果的准确性是最关键的质量指标。

## 许可证

本项目采用MIT许可证。详细信息请参见LICENSE文件。

## Issues

欢迎提交问题报告和拉取请求。对于重大更改，请先开启一个issue讨论您想要更改的内容。

## 联系方式

如有其他问题或建议，请通过issue tracker联系我。

