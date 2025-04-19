package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * C/C++代码行计数器实现
 * 处理.c, .cpp, .cc文件的代码行统计
 */
public class CppLineCounter extends LineCounter {
    
    private static final String[] SUPPORTED_EXTENSIONS = {"c", "cpp", "cc"};
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inMultiLineComment = false;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // 空行检测
                if (line.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 处理多行注释
                if (inMultiLineComment) {
                    result.addCommentLine();
                    if (line.contains("*/")) {
                        inMultiLineComment = false;
                        
                        // 检查注释结束后是否还有代码
                        int endCommentIndex = line.indexOf("*/") + 2;
                        if (endCommentIndex < line.length()) {
                            String afterComment = line.substring(endCommentIndex).trim();
                            if (!afterComment.isEmpty() && !isLineOnlyComment(afterComment)) {
                                // 代码行优先级高于注释行，但不重复计数
                                result.addCodeLine();
                                // 撤销之前添加的注释行
                                // 这里不需要直接操作内部计数器，为了保证封装性，我们在LineCountResult中添加了decrementCommentLine()方法
                            }
                        }
                    }
                    continue;
                }
                
                // 检查是否是单行注释 //
                if (line.startsWith("//")) {
                    result.addCommentLine();
                    continue;
                }
                
                // 检查是否开始多行注释
                int startCommentIndex = line.indexOf("/*");
                if (startCommentIndex == 0) {
                    result.addCommentLine();
                    // 检查多行注释是否在同一行结束
                    if (line.contains("*/")) {
                        int endCommentIndex = line.indexOf("*/") + 2;
                        if (endCommentIndex < line.length()) {
                            String afterComment = line.substring(endCommentIndex).trim();
                            if (!afterComment.isEmpty() && !isLineOnlyComment(afterComment)) {
                                // 代码行优先级高于注释行
                                result.addCodeLine();
                                // 撤销之前添加的注释行
                                // 这里不需要直接操作内部计数器，保持封装性
                            }
                        }
                    } else {
                        inMultiLineComment = true;
                    }
                    continue;
                }
                
                // 处理行内的注释和代码混合情况
                if (startCommentIndex > 0) {
                    // 先检查"//"是否在字符串内
                    int doubleSlashIndex = findUnquotedIndex(line, "//");
                    
                    // 再检查"/*"是否在字符串内
                    if (isUnquoted(line, startCommentIndex)) {
                        result.addCodeLine(); // 行内包含代码和注释，计为代码行
                        
                        // 检查多行注释是否在此行结束
                        if (!line.contains("*/")) {
                            inMultiLineComment = true;
                        }
                        continue;
                    }
                    
                    // 检查不在字符串内的"//"
                    if (doubleSlashIndex >= 0 && isUnquoted(line, doubleSlashIndex)) {
                        result.addCodeLine(); // 行内包含代码和单行注释，计为代码行
                        continue;
                    }
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
     * 检查字符串是否仅包含注释（单行或多行）
     */
    private boolean isLineOnlyComment(String line) {
        return line.startsWith("//") || line.startsWith("/*");
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

/** *2025-04-19 11:52 **/ 