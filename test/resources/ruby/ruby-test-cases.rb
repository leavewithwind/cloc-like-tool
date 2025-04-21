#!/usr/bin/env ruby

# 这是单行注释

=begin
这是一个多行注释块
跨越多行
=end

# 基本代码示例
class RubyTestCases
  def initialize
    @value = 100
  end
  
  # 带有doc注释的方法
  def documented_method
    puts "这个方法有文档"
  end
  
  def method_with_inline_comment # 这是行内注释
    value = 42
    return value
  end
  
  # 字符串中包含注释标记的例子
  def string_with_hash
    str1 = "这个 # 不是注释"
    str2 = '这个 # 也不是注释'
    
    # 这是真正的注释
    return str1 + str2
  end
  
  # HEREDOC示例
  def heredoc_example
    sql = <<-SQL
    SELECT *
    FROM users
    WHERE name = 'John'
    # 这个在HEREDOC中，不应该被计为注释
    SQL
    
    return sql
  end
  
  # 带引号的HEREDOC
  def quoted_heredoc
    text = <<-"TEXT"
    这是一个带引号的HEREDOC
    # 这不是注释
    TEXT
    
    text2 = <<-'LITERAL'
    这是字面HEREDOC
    # 这也不是注释
    LITERAL
    
    return text + text2
  end
  
  # 不同类型的HEREDOC定界符
  def different_heredoc_styles
    # 传统风格
    doc1 = <<EOF
标准HEREDOC
# 不是注释
EOF
    
    # 缩进风格
    doc2 = <<-INDENT
    缩进式HEREDOC
    # 不是注释
    INDENT
    
    # ~风格（允许缩进去除）
    doc3 = <<~TILDE
      波浪线风格HEREDOC
      # 不是注释
    TILDE
    
    return doc1 + doc2 + doc3
  end
  
  # 空行示例

  
  def empty_lines_and_comments
    
    # 注释前有空行
    
    # 注释后有空行
    
    code = true
    
    return code
  end
  
=begin
另一个多行注释
包含空行

和更多文本
=end

  # 嵌套结构中的注释
  def nested_structure
    [1, 2, 3].each do |number|
      # 循环内注释
      puts number
    end
    
    if true
      # 条件内注释
      puts "True"
    else
      # else条件内注释
      puts "False"
    end
  end
end

# 主执行代码
if __FILE__ == $0
  test = RubyTestCases.new
  test.documented_method
  test.method_with_inline_comment
  test.string_with_hash
  test.heredoc_example
  puts "测试完成"
end 