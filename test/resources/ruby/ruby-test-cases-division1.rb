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