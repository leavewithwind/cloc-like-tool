
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
