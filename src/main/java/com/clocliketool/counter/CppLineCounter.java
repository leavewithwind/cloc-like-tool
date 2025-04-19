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
    
    // 移除头文件支持
    private static final String[] SUPPORTED_EXTENSIONS = {"c", "cpp", "cc"};
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inMultiLineComment = false;
            
            while ((line = reader.readLine()) != null) {
                // 记录原始行
                String originalLine = line;
                // 检查空行
                if (originalLine.trim().isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 处理多行注释状态
                if (inMultiLineComment) {
                    // 默认处理为注释行
                    result.addCommentLine();
                    
                    // 检查是否有多行注释结束
                    int endCommentPos = originalLine.indexOf("*/");
                    if (endCommentPos >= 0) {
                        inMultiLineComment = false;
                        
                        // 如果注释结束后还有内容
                        if (endCommentPos + 2 < originalLine.length()) {
                            String afterComment = originalLine.substring(endCommentPos + 2);
                            
                            // 检查注释后的内容是否只包含空白
                            if (!afterComment.trim().isEmpty()) {
                                // 检查是否有新的注释开始
                                int newSingleCommentPos = findUnquotedIndex(afterComment, "//");
                                int newBlockCommentPos = findUnquotedIndex(afterComment, "/*");
                                
                                // 如果注释后立即跟随另一个注释，保持注释行分类
                                if (newSingleCommentPos == 0 || newBlockCommentPos == 0) {
                                    // 已经添加了注释行，无需操作
                                    // 检查是否又进入了多行注释
                                    if (newBlockCommentPos == 0 && afterComment.indexOf("*/", 2) < 0) {
                                        inMultiLineComment = true;
                                    }
                                }
                                // 如果注释后有实际代码（而非另一个注释），则重新分类为代码行
                                else if (hasNonCommentCode(afterComment)) {
                                    // 将此行从注释改为代码
                                    result.decrementCommentLine();
                                    result.addCodeLine();
                                }
                            }
                        }
                    }
                    
                    continue;
                }
                
                // 检查行首单行注释 //
                int singleCommentPos = findUnquotedIndex(originalLine, "//");
                if (singleCommentPos == 0) {
                    result.addCommentLine();
                    continue;
                }
                
                // 检查行首块注释 /*
                int blockCommentPos = findUnquotedIndex(originalLine, "/*");
                if (blockCommentPos == 0) {
                    result.addCommentLine();
                    
                    // 检查块注释是否在同一行结束
                    int endBlockPos = originalLine.indexOf("*/", 2);
                    if (endBlockPos >= 0) {
                        // 如果结束后有内容
                        if (endBlockPos + 2 < originalLine.length()) {
                            String afterComment = originalLine.substring(endBlockPos + 2);
                            
                            // 如果注释后有非空内容
                            if (!afterComment.trim().isEmpty()) {
                                // 检查是否紧跟着另一个注释
                                int newSingleComment = findUnquotedIndex(afterComment, "//");
                                int newBlockComment = findUnquotedIndex(afterComment, "/*");
                                
                                if (newSingleComment == 0 || newBlockComment == 0) {
                                    // 紧跟着另一个注释，依然算作注释行
                                    if (newBlockComment == 0 && afterComment.indexOf("*/", 2) < 0) {
                                        inMultiLineComment = true;
                                    }
                                }
                                // 如果不是紧跟着注释，检查是否有实际代码
                                else if (hasNonCommentCode(afterComment)) {
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
                    if (hasNonCommentCode(beforeComment)) {
                        // 注释前有代码，算作代码行
                        result.addCodeLine();
                        
                        // 如果是块注释且未在此行结束，设置多行注释状态
                        if (blockCommentPos > 0 && blockCommentPos == firstCommentPos && 
                            originalLine.indexOf("*/", blockCommentPos + 2) < 0) {
                            inMultiLineComment = true;
                        }
                    } else {
                        // 注释前只有空白，算作注释行
                        result.addCommentLine();
                        
                        // 如果是块注释且未在此行结束，设置多行注释状态
                        if (blockCommentPos > 0 && blockCommentPos == firstCommentPos && 
                            originalLine.indexOf("*/", blockCommentPos + 2) < 0) {
                            inMultiLineComment = true;
                        }
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
     * 检查字符串是否包含非注释的实际代码
     * 排除掉只包含空白和注释的情况
     */
    private boolean hasNonCommentCode(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        
        // 检查是否以注释开头
        if (trimmed.startsWith("//") || trimmed.startsWith("/*")) {
            return false;
        }
        
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

/** *2025-04-19 11:52 **/ 