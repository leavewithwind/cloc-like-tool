package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Ruby代码行计数器实现
 * 处理.rb文件的代码行统计
 */
public class RubyLineCounter extends LineCounter {
    
    private static final String[] SUPPORTED_EXTENSIONS = {"rb"};
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inMultiLineComment = false;
            
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                // 空行检测
                if (trimmedLine.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 处理=begin到=end的多行注释
                if (inMultiLineComment) {
                    result.addCommentLine();
                    if (trimmedLine.equals("=end")) {
                        inMultiLineComment = false;
                    }
                    continue;
                }
                
                // 检查是否开始多行注释
                if (trimmedLine.equals("=begin")) {
                    result.addCommentLine();
                    inMultiLineComment = true;
                    continue;
                }
                
                // 检查是否是单行注释 #
                int hashIndex = findUnquotedIndex(line, "#");
                if (hashIndex == 0) {
                    result.addCommentLine();
                    continue;
                }
                
                // 行内包含注释和代码的情况
                if (hashIndex > 0) {
                    result.addCodeLine(); // Ruby中，行内包含代码和注释，优先计为代码行
                    continue;
                }
                
                // 其他情况算作代码行
                result.addCodeLine();
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
     * 检查指定位置的字符是否在引号外
     */
    private boolean isUnquoted(String line, int index) {
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean escaped = false;
        
        for (int i = 0; i < index; i++) {
            char c = line.charAt(i);
            
            if (escaped) {
                escaped = false;
                continue;
            }
            
            if (c == '\\') {
                escaped = true;
                continue;
            }
            
            if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            } else if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            }
        }
        
        return !inSingleQuote && !inDoubleQuote;
    }
    
    /**
     * 找到不在引号内的子字符串位置
     */
    private int findUnquotedIndex(String line, String substring) {
        int index = line.indexOf(substring);
        while (index >= 0) {
            if (isUnquoted(line, index)) {
                return index;
            }
            index = line.indexOf(substring, index + 1);
        }
        return -1;
    }
}
