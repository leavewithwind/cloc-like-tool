# 代码行统计工具 (CLOC)

这是一个用于统计C/C++和Ruby源文件中的代码行、注释行和空行数量的工具。

## 功能特性

- 支持C/C++文件（.c, .cpp, .cc, .h, .hpp）的代码行统计
- 支持Ruby文件（.rb）的代码行统计
- 递归遍历目录
- 正确处理注释和代码混合的行
- 可以按语言分别查看统计结果
- 处理字符串中的注释字符
- 支持多种注释风格（C/C++的//和/* */，Ruby的#和=begin/=end）

## 项目架构

项目采用了模块化的面向对象结构设计，使用以下设计模式：

- **策略模式**：使用`LineCounter`抽象类定义代码行计数行为，不同语言的计数器实现该类
- **工厂模式**：使用`LineCounterFactory`根据命令行输入创建适当的计数器

### 目录结构

```
cloc-like-tool/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── clocliketool/
│   │   │           ├── Main.java                   # 应用程序入口
│   │   │           ├── model/                      # 模型类
│   │   │           │   └── LineCountResult.java    # 计数结果模型
│   │   │           ├── counter/                    # 计数器模块
│   │   │           │   ├── LineCounter.java        # 抽象基础计数器类
│   │   │           │   ├── CppLineCounter.java     # C/C++实现
│   │   │           │   ├── RubyLineCounter.java    # Ruby实现
│   │   │           │   └── LineCounterFactory.java # 计数器工厂
│   │   │           ├── exception/                  # 异常处理模块
│   │   │           └── util/                       # 工具类模块
│   │   │               ├── DirectoryScanner.java   # 目录扫描器
│   │   │               └── ResultFormatter.java    # 结果格式化工具
│   └── test/
│       ├── java/                                   # 测试代码目录
│       └── resources/                              # 测试资源目录
│           └── testdata/                           # 测试数据文件
│               ├── cpp/                            # C/C++测试文件
│               └── ruby/                           # Ruby测试文件
├── target/                                         # 编译输出目录（自动生成）
├── pom.xml                                         # Maven配置文件
└── README.md                                       # 项目说明文档
```

## 设计原则

本项目遵循以下设计原则：

1. **单一职责原则**：每个类都有明确的单一职责
2. **开闭原则**：代码对扩展开放，对修改关闭，易于添加新的语言支持
3. **依赖倒置原则**：高层模块不依赖低层模块，通过抽象接口交互
4. **接口隔离原则**：通过适当粒度的接口设计，确保依赖最小化
5. **最少知识原则**：各模块之间通过最少的接口通信
6. **关注点分离**：将数据存储、业务逻辑和UI展示分离

## 构建项目

### 前提条件

- Java JDK 11或更高版本
- Maven 3.6或更高版本

### 构建步骤

```bash
# 克隆项目
git clone https://your-repository-url/cloc-like-tool.git
cd cloc-like-tool

# 使用Maven构建
mvn clean package
```

构建完成后，可以在`target`目录中找到可执行的JAR文件。

## 使用方法

```bash
java -jar target/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar [选项] <路径1> [<路径2> ...]
```

### 选项

- `-h`, `--help`: 显示帮助信息
- `-l`, `--language <语言>`: 指定要统计的语言（支持: c, cpp, ruby, all）

### 示例

```bash
# 统计当前目录中的所有支持的文件
java -jar cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar .

# 统计指定目录中的C++文件
java -jar cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar -l cpp /path/to/cpp/project

# 统计多个目录
java -jar cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar /path/to/dir1 /path/to/dir2
```

## 输出格式

```
Files     Language     Lines     Code          Comments     Blanks
------------------------------------------------------------------
32        C++          6813      5678          901          234
8         C            1890      1234          567          89
12        Ruby         424       345           67           12
------------------------------------------------------------------
52        Total        9127      7257          1535         335
```

## 扩展支持新的语言

要添加对新语言的支持，需要执行以下步骤：

1. 创建一个继承`LineCounter`的新计数器类
2. 在`LineCounterFactory`中注册新的计数器
3. 更新`Main.getLanguageByExtension()`方法以添加新语言的映射

## 许可证

本项目采用MIT许可证。 