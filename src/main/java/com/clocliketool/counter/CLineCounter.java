package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * C/C++代码行计数器实现（将C和C++作为一类语言处理）
 * 处理.c, .cpp, .cc文件的代码行统计
 */
public class CLineCounter extends LineCounter {
    private static final String[] SUPPORTED_EXTENSIONS = {"c", "cpp", "cc"}; // 支持的文件扩展名
    
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
                        continue;
                    }
                    
                    continue;
                }
                
                // 检查行首单行注释（行首可以有空白字符）
                int singleCommentPos = findUnquotedIndex(line, "//");
                if (singleCommentPos >= 0 && line.substring(0, singleCommentPos).trim().isEmpty()) {
                    result.addCommentLine();
                    continue;
                }
                
                // 检查行首块注释（行首可以有空白字符）
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
                
                // 其它情况为纯代码行
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
     * 
     * @param line 要检查的字符串行
     * @param index 要检查的字符索引
     * @return 如果字符在引号外返回true，否则返回false
     */
    private boolean isUnquoted(String line, int index) {
        // 检查索引是否在有效范围内
        if (index < 0 || index >= line.length()) {
            return false;
        }
        
        // 初始化跟踪引号和转义字符
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean escaped = false;
        
        // 遍历字符串直到指定索引
        for (int i = 0; i < index; i++) {
            char c = line.charAt(i);
            
            // 如果前一个字符是转义符，则跳过当前字符
            if (escaped) {
                escaped = false;
                continue;
            }
            
            // 检查是否遇到转义符
            if (c == '\\') {
                escaped = true;
                continue;
            }
            
            // 检查是否遇到双引号，并且不在单引号内
            if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            } 
            // 检查是否遇到单引号，并且不在双引号内
            else if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            }
        }
        
        // 返回字符是否在引号外
        return !inSingleQuote && !inDoubleQuote;
    }
    
    /**
     * 搜索不在引号内的子字符串位置
     * 
     * @param line 要搜索的字符串行
     * @param substring 要查找的子字符串
     * @return 子字符串在不在引号内的情况下首次出现的索引。未找到或在引号内则返回-1
     */
    private int findUnquotedIndex(String line, String substring) {
        if (line == null || substring == null || line.isEmpty() || substring.isEmpty()) {
            return -1;
        }
        
        // 查找子字符串的初始索引
        int index = line.indexOf(substring);
        
        // 循环查找直到找到不在引号内的子字符串
        while (index >= 0) {
            // 检查当前索引处的子字符串是否在引号外
            if (isUnquoted(line, index)) {
                return index;
            }
            // 查找下一个子字符串的索引
            index = line.indexOf(substring, index + 1);
        }
        
        return -1; // 未找到符合条件的子字符串返回-1
    }
}
