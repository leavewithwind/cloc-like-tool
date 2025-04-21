# 代码行统计工具 (CLOC)

这是一个用于统计C/C++和Ruby源文件中的代码行、注释行和空行数量的工具。

## 功能特性

- 支持C/C++文件（.c, .cpp, .cc）的代码行统计
- 支持Ruby文件的代码行统计
- 按指定语言检测特定语言的代码
- 正确处理注释和代码混合的行
- 不会错误识别字符串中的注释字符
- 正确识别多种注释风格（C/C++的//和/* */，Ruby的#和=begin/=end）
- 简单友好的使用方式，提供友好的错误信息
- 面向更多语言的易拓展设计

## 项目架构

项目采用了模块化的面向对象结构设计，使用以下设计模式：

- **策略模式**：使用`LineCounter`抽象类定义代码行计数行为，不同语言的计数器实现该类
- **工厂模式**：使用`LineCounterFactory`根据命令行输入创建适当的计数器
- **异常处理模式**：使用自定义异常层次结构和统一的异常处理机制

## 设计原则

本项目遵循以下设计原则：

1. **单一职责原则**：每个类尽量保持单一职责
2. **开闭原则**：代码对扩展开放，对修改关闭，易于添加新的语言支持
3. **依赖倒置原则**：高层模块不依赖低层模块，通过抽象接口交互
4. **关注点分离**：将数据存储、业务逻辑和UI展示分离
5. **异常处理原则**：使用明确的异常类型和统一的异常处理机制

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
│                   │   └── ExceptionHandler.java      # 异常处理工具类
│                   └── util/                          # 工具类模块
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



## 主要类的职责

1. **Main** - 应用程序入口点，控制程序整体流程
2. **CommandLineProcessor** - 处理命令行参数
3. **LineCounter** - 抽象类，定义计数行为
4. **LineCounterFactory** - 工厂方法，创建适当的计数器实例
5. **FileAnalyzer** - 分析文件和目录
6. **CliTableFormatter** - 格式化输出结果
7. **LanguageMapper** - 将文件名映射到编程语言
8. **DirectoryScanner** - 目录扫描器
9. **ExceptionHandler** - 统一异常处理器


## 异常处理框架

本项目实现了一个完整的异常处理框架，确保程序能够优雅地处理各种错误情况：

### 错误代码管理

使用**StatusCode**枚举定义了标准化的错误代码和消息：

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

### 统一的异常处理

**ExceptionHandler**类提供了统一的异常处理机制：
- 根据异常类型确定适当的错误代码
- 生成友好的错误消息
- 对不同类型的异常提供不同级别的详细信息
- 仅在必要时显示堆栈跟踪


### 异常类体系

- **LineCounterException** - 基础异常类，所有自定义异常的父类
- **UnsupportedLanguageException** - 当指定了不支持的语言时抛出
- **FileProcessingException** - 当文件处理过程中出现问题时抛出
- **InvalidArgumentException** - 当提供的参数无效时抛出


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

构建完成后，可在`target`目录中找到可执行JAR文件：
- `cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`

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

为了使用更简便的`cloc`命令，您可以使用安装脚本进行安装。

#### Windows安装

1. 将JAR文件和安装脚本放在同一目录
2. 运行 `install-scripts/windows/install.bat`
3. 脚本会自动:
   - 在`%USERPROFILE%\bin`目录中创建`cloc-like-tool`子目录
   - 将JAR文件复制到该子目录
   - 创建启动脚本`cloc.bat`到`%USERPROFILE%\bin`目录
   - 将`%USERPROFILE%\bin`添加到系统PATH环境变量
4. 重新打开命令提示符或PowerShell窗口
5. 输入 `cloc` 测试安装是否成功

#### macOS/Linux安装

1. 将JAR文件和安装脚本放在同一目录
2. 打开终端，导航到该目录
3. 执行以下命令:
   ```bash
   chmod +x install-scripts/unix/install.sh
   ./install-scripts/unix/install.sh
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
install-scripts/windows/uninstall.bat

# macOS/Linux
./install-scripts/unix/uninstall.sh
```

## 输出格式

```
-----------------------------------------------------------
     Files      Lines        Code   Comments     Blanks
-----------------------------------------------------------
       32       7153        6228        442        483
-----------------------------------------------------------
```

## 错误码说明

当程序遇到错误时，会返回以下退出码：

- **0**: 成功执行
- **1**: 命令行参数错误
- **2**: 不支持的语言
- **3**: 文件或目录不存在
- **4**: 文件处理错误
- **5**: 找不到指定的文件
- **6**: 无权访问指定的文件或目录
- **7**: 读取文件时发生I/O错误
- **8**: 参数无效
- **98**: 发生未预期的异常
- **99**: 未知错误

## 扩展支持新的语言

要添加对新语言的支持，需要执行以下步骤：

1. 创建一个继承`LineCounter`的新计数器类
2. 在`LineCounterFactory`中注册新的计数器
3. 在`LanguageMapper`中添加新的文件名到语言的映射
4. 实现对应语言的注释和代码行识别逻辑，确保正确处理该语言特有的语法特性
5. 如果需要特殊的异常处理，可以在异常框架中添加新的异常类型

## 测试策略

本项目采用了特定的测试策略，主要依赖于功能测试而非传统的单元测试：

1. **比对式功能测试**：项目中的`scripts/test-ruby-count.sh`脚本采用比对式测试方法，通过将本工具的计数结果与官方cloc工具的结果进行对比，验证计数算法的准确性。

2. **针对性与高效性**：由于项目规模较小且功能聚焦于代码行计数，这种端到端的测试方法能够直接验证核心功能，比单独测试每个组件更加高效。

3. **实际使用场景验证**：比对测试使用真实的代码文件作为输入，确保工具在实际使用场景中的可靠性，这比模拟测试更能发现实际问题。

4. **结果导向的质量保证**：对于计数工具而言，最终计数结果的准确性是最关键的质量指标，通过与业界标准工具的结果比对，可以直接验证这一点。

这种测试方法针对本项目的特点，提供了一个简单有效的质量保证机制，确保代码计数结果符合行业标准，同时减少了开发和维护测试代码的工作量。

