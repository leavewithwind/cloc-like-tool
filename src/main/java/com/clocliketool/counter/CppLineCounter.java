package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * C/C++代码行计数器实现
 * 处理.c, .cpp, .cc文件的代码行统计
 * 修改以匹配cloc官方工具的处理方式
 */
public class CppLineCounter extends LineCounter {
    
    // 支持的文件扩展名
    private static final String[] SUPPORTED_EXTENSIONS = {"c", "cpp", "cc"};
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inMultiLineComment = false;
            
            while ((line = reader.readLine()) != null) {
                // 记录原始行和修剪后的行
                String originalLine = line;
                String trimmedLine = originalLine.trim();
                
                // 空行检测
                if (trimmedLine.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 处理多行注释状态
                if (inMultiLineComment) {
                    result.addCommentLine();
                    int endCommentPos = originalLine.indexOf("*/");
                    if (endCommentPos >= 0) {
                        inMultiLineComment = false;
                        String afterComment = originalLine.substring(endCommentPos + 2).trim();
                        if (!afterComment.isEmpty() && isActualCode(afterComment)) {
                            result.decrementCommentLine();
                            result.addCodeLine();
                        }
                    }
                    continue;
                }
                
                // 检查行首单行注释 //
                int singleCommentPos = findUnquotedIndex(originalLine, "//");
                if (singleCommentPos >= 0 && originalLine.substring(0, singleCommentPos).trim().isEmpty()) {
                    result.addCommentLine();
                    continue;
                }
                
                // 检查行首块注释 /*
                int blockCommentPos = findUnquotedIndex(originalLine, "/*");
                if (blockCommentPos >= 0 && originalLine.substring(0, blockCommentPos).trim().isEmpty()) {
                    result.addCommentLine();
                    
                    // 检查块注释是否在同一行结束
                    int endBlockPos = originalLine.indexOf("*/", blockCommentPos + 2);
                    if (endBlockPos >= 0) {
                        // 如果结束后有内容
                        if (endBlockPos + 2 < originalLine.length()) {
                            String afterComment = originalLine.substring(endBlockPos + 2);
                            String trimmedAfter = afterComment.trim();
                            
                            // 如果注释后有非空内容
                            if (!trimmedAfter.isEmpty()) {
                                // 检查是否紧跟着另一个注释
                                int newSingleComment = findUnquotedIndex(afterComment, "//");
                                int newBlockComment = findUnquotedIndex(afterComment, "/*");
                                
                                // 如果后续内容是另一个注释
                                if ((newSingleComment >= 0 && afterComment.substring(0, newSingleComment).trim().isEmpty()) || 
                                    (newBlockComment >= 0 && afterComment.substring(0, newBlockComment).trim().isEmpty())) {
                                    // 紧跟着另一个注释，依然算作注释行
                                    if (newBlockComment >= 0 && afterComment.indexOf("*/", newBlockComment + 2) < 0) {
                                        inMultiLineComment = true;
                                    }
                                }
                                // 如果后续内容是实际代码
                                else if (isActualCode(trimmedAfter)) {
                                    // 包含代码，重新分类
                                    result.decrementCommentLine();
                                    result.addCodeLine();
                                }
                            }
                        }
                    } else {
                        // 块注释在此行未结束
                        inMultiLineComment = true;
                    }
                    
                    continue;
                }
                
                // 处理行中间的注释（代码和注释混合）
                if (singleCommentPos > 0 || blockCommentPos > 0) {
                    // 确定哪个注释先出现
                    int firstCommentPos;
                    if (singleCommentPos < 0) {
                        firstCommentPos = blockCommentPos;
                    } else if (blockCommentPos < 0) {
                        firstCommentPos = singleCommentPos;
                    } else {
                        firstCommentPos = Math.min(singleCommentPos, blockCommentPos);
                    }
                    
                    // 检查注释前是否有实际代码
                    String beforeComment = originalLine.substring(0, firstCommentPos);
                    if (isActualCode(beforeComment.trim())) {
                        // 注释前有代码，算作代码行
                        result.addCodeLine();
                        
                        // 如果是块注释且未在此行结束，设置多行注释状态
                        if (firstCommentPos == blockCommentPos && originalLine.indexOf("*/", blockCommentPos + 2) < 0) {
                            inMultiLineComment = true;
                        }
                    } else {
                        // 注释前只有空白或特殊字符，算作注释行
                        result.addCommentLine();
                        
                        // 如果是块注释且未在此行结束，设置多行注释状态
                        if (firstCommentPos == blockCommentPos && originalLine.indexOf("*/", blockCommentPos + 2) < 0) {
                            inMultiLineComment = true;
                        }
                    }
                    
                    continue;
                }
                
                // 特殊处理：检查#pragma、#include等预处理指令
                if (trimmedLine.startsWith("#")) {
                    result.addCodeLine();
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
     * 检查字符串是否包含实际代码（非注释的有效内容）
     */
    private boolean isActualCode(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        
        // 排除只有注释的行
        if (text.startsWith("//") || text.startsWith("/*")) {
            return false;
        }
        
        // 排除只包含括号、分号等的行
        String trimmed = text.trim();
        if (trimmed.equals(";") || trimmed.equals("{") || trimmed.equals("}") || trimmed.equals("{}")) {
            return false;
        }
        
        // 否则认为是代码
        return true;
    }
    
    /**
     * 检查指定位置的字符是否在引号外
     * 处理字符串中的注释符号
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
