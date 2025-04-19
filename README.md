# 代码行统计工具

这是一个用于统计C/C++和Ruby源文件中的代码行、注释行和空行数量的工具。

## 功能特性

- 支持C/C++文件（.c, .cpp, .cc, .h, .hpp）的代码行统计
- 支持Ruby文件（.rb）的代码行统计
- 递归遍历目录
- 正确处理注释和代码混合的行
- 可以按语言分别查看统计结果
- 处理字符串中的注释字符
- 支持多种注释风格（C/C++的//和/* */，Ruby的#和=begin/=end）

## 设计模式

- **策略模式**：使用`CodeParser`接口定义代码解析行为，不同语言的解析器实现该接口
- **工厂模式**：使用`ParserFactory`根据命令行输入创建适当的解析器

## 构建项目

### 前提条件

- Java JDK 11或更高版本
- Maven 3.6或更高版本

### 构建步骤

```bash
# 克隆项目
git clone https://your-repository-url/code-line-counter.git
cd code-line-counter

# 使用Maven构建
mvn clean package
```

构建完成后，可以在`target`目录中找到可执行的JAR文件。

## 使用方法

```bash
java -jar target/code-line-counter-1.0-SNAPSHOT-jar-with-dependencies.jar [选项] <路径1> [<路径2> ...]
```

### 选项

- `-h`, `--help`: 显示帮助信息
- `-l`, `--language <语言>`: 指定要统计的语言（支持: c, cpp, ruby, all）

### 示例

```bash
# 统计当前目录中的所有支持的文件
java -jar code-line-counter.jar .

# 统计指定目录中的C++文件
java -jar code-line-counter.jar -l cpp /path/to/cpp/project

# 统计多个目录
java -jar code-line-counter.jar /path/to/dir1 /path/to/dir2
```

## 输出格式

```
语言        代码行        注释行        空行        总行数
----------------------------------------------------
C          1234         567          89          1890
C++        5678         901          234         6813
Ruby       345          67           12          424
----------------------------------------------------
总计        7257         1535         335         9127

处理的文件数量: 42
```

## 扩展支持新的语言

要添加对新语言的支持：

1. 创建一个实现`CodeParser`接口的新类
2. 在`ParserFactory`中注册新的解析器
3. 更新`FileProcessor.getLanguageByExtension()`方法

## 许可证

本项目采用MIT许可证。 