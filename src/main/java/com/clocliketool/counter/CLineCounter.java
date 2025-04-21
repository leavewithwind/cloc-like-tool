package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * C/C++代码行计数器实现
 * 将C和C++作为同一类语言处理
 * 处理.c, .cpp, .cc文件的代码行统计
 * 修改以匹配cloc官方工具的处理方式
 */
public class CLineCounter extends LineCounter {
    
    private static final String[] SUPPORTED_EXTENSIONS = {"c", "cpp", "cc"};
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inMultiLineComment = false;
            
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                // 检查空行
                if (trimmedLine.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 处理多行注释状态
                if (inMultiLineComment) {
                    result.addCommentLine();
                    
                    // 检查注释是否结束
                    int endCommentPos = line.indexOf("*/");
                    if (endCommentPos >= 0) {
                        inMultiLineComment = false;
                        
                        // 关键修改：注释结束后即使有代码，cloc也仍然将这一行视为注释行
                        // 这是与你原来程序的主要区别点
                        continue;
                    }
                    
                    continue;
                }
                
                // 检查行首单行注释 //（允许前导空白）
                int singleCommentPos = findUnquotedIndex(line, "//");
                if (singleCommentPos >= 0 && line.substring(0, singleCommentPos).trim().isEmpty()) {
                    result.addCommentLine();
                    continue;
                }
                
                // 检查行首块注释 /*（允许前导空白）
                int blockCommentPos = findUnquotedIndex(line, "/*");
                if (blockCommentPos >= 0 && line.substring(0, blockCommentPos).trim().isEmpty()) {
                    result.addCommentLine();
                    
                    // 检查块注释是否在同一行结束
                    int endBlockPos = line.indexOf("*/", blockCommentPos + 2);
                    if (endBlockPos < 0) {
                        // 块注释在此行未结束
                        inMultiLineComment = true;
                    }
                    continue;
                }
                
                // 处理行中间的注释（代码和注释混合）
                if (singleCommentPos > 0 || (blockCommentPos > 0 && !line.substring(0, blockCommentPos).trim().isEmpty())) {
                    // 注释前有代码，算作代码行
                    result.addCodeLine();
                    
                    // 检查是否开始多行注释
                    if (blockCommentPos > 0 && line.indexOf("*/", blockCommentPos + 2) < 0) {
                        inMultiLineComment = true;
                    }
                    continue;
                }
                
                // 其它情况：纯代码行
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
        if (index < 0 || index >= line.length()) {
            return false;
        }
        
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
        if (line == null || substring == null || line.isEmpty() || substring.isEmpty()) {
            return -1;
        }
        
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
