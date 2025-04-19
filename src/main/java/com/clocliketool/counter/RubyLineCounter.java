package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Ruby代码行计数器实现
 * 处理.rb文件的代码行统计
 * 修正注释识别和代码行识别逻辑，提高统计准确性
 * 扩展支持更多Ruby文件扩展名
 */
public class RubyLineCounter extends LineCounter {
    
    private static final String[] SUPPORTED_EXTENSIONS = {
        "rb",    // 标准Ruby文件
        "rbw",   // Ruby for Windows
        "rake",  // Ruby Rakefile
        "gemspec", // Ruby Gem规范文件
        "ru",    // Ruby rack配置文件
        "thor",  // Ruby Thor文件
        "podspec", // Ruby CocoaPods规范文件
        "rbs"    // Ruby类型签名文件
    };
    
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
                
                // 检查是否是行首的单行注释 #
                int hashIndex = findUnquotedIndex(originalLine, "#");
                if (hashIndex >= 0 && originalLine.substring(0, hashIndex).trim().isEmpty()) {
                    result.addCommentLine();
                    continue;
                }
                
                // 行内包含注释和代码的情况
                if (hashIndex > 0) {
                    // 检查注释前是否有实际代码
                    String beforeComment = originalLine.substring(0, hashIndex);
                    if (isActualCode(beforeComment.trim())) {
                        result.addCodeLine(); // 注释前有代码，计为代码行
                    } else {
                        result.addCommentLine(); // 注释前只有空白字符，计为注释行
                    }
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
     * 检查字符串是否包含实际代码（非空白字符且不仅包含特殊符号）
     */
    private boolean isActualCode(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        
        // 排除只包含特殊符号的行
        String trimmed = text.trim();
        if (trimmed.equals(";") || trimmed.equals("{") || trimmed.equals("}") || 
            trimmed.equals("end") || trimmed.equals("do") || trimmed.equals("else")) {
            return false;
        }
        
        // 否则认为是代码
        return true;
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
