package com.clocliketool.counter;

import com.clocliketool.config.AppConfig;
import com.clocliketool.config.ConfigKeys;
import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Ruby 代码行计数器实现类
 */
public class RubyLineCounter extends LineCounter {
    
    private static final String[] SUPPORTED_EXTENSIONS = 
            AppConfig.getStringArray(ConfigKeys.EXTENSION_RUBY, ",");
    
    private boolean inMultilineComment = false;
    
    /**
     * 计算Ruby文件的代码行、注释行和空行
     * 
     * @param file 要统计的文件
     * @return LineCountResult对象，包含统计结果
     * @throws IOException 如果文件读取失败
     */
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                if (trimmedLine.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 检查多行注释结束标记
                if (inMultilineComment) {
                    result.addCommentLine();
                    if (trimmedLine.equals(AppConfig.getString(ConfigKeys.RUBY_MULTILINE_END))) {
                        inMultilineComment = false;
                    }
                    continue;
                }
                
                // 检查多行注释开始标记
                if (trimmedLine.equals(AppConfig.getString(ConfigKeys.RUBY_MULTILINE_START))) {
                    result.addCommentLine();
                    inMultilineComment = true;
                    continue;
                }
                
                // 检查单行注释
                int hashIndex = findUnquotedIndex(line, AppConfig.getString(ConfigKeys.RUBY_COMMENT_CHAR));
                if (hashIndex == 0) {
                    // 行首为 #，整行是注释
                    result.addCommentLine();
                } else if (hashIndex > 0) {
                    // 行中有 # 但不在行首，有代码也有注释
                    result.addCodeLine();
                } else {
                    // 没有注释符号，整行是代码
                    result.addCodeLine();
                }
            }
        }
        
        // 增加文件计数
        result.incrementFileCount();
        return result;
    }
    
    @Override
    public String[] getSupportedExtensions() {
        return SUPPORTED_EXTENSIONS;
    }
    
    /**
     * 查找非引号内的第一个指定字符的位置
     * 
     * @param line 要搜索的行
     * @param target 目标字符
     * @return 如果找到则返回索引，否则返回-1
     */
    private int findUnquotedIndex(String line, String target) {
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean escaped = false;
        
        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            
            if (escaped) {
                escaped = false;
                continue;
            }
            
            if (current == '\\') {
                escaped = true;
                continue;
            }
            
            if (current == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            } else if (current == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            } else if (!inSingleQuote && !inDoubleQuote && i + target.length() <= line.length() 
                    && line.substring(i, i + target.length()).equals(target)) {
                return i;
            }
        }
        
        return -1;
    }
}
