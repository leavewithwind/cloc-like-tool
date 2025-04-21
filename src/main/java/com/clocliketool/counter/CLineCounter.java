package com.clocliketool.counter;

import com.clocliketool.config.AppConfig;
import com.clocliketool.config.ConfigKeys;
import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * C/C++代码行计数器实现类
 * 处理.c, .cpp, .cc文件的代码行统计
 */
public class CLineCounter extends LineCounter {
    
    private static final String[] SUPPORTED_EXTENSIONS = 
            AppConfig.getStringArray(ConfigKeys.EXTENSION_CPP, ",");
    
    private boolean inMultilineComment = false;
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                // 空行检测
                if (trimmedLine.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 获取注释标记符
                String lineCommentStart = AppConfig.getString(ConfigKeys.CPP_LINE_COMMENT_START);
                String blockCommentStart = AppConfig.getString(ConfigKeys.CPP_BLOCK_COMMENT_START);
                String blockCommentEnd = AppConfig.getString(ConfigKeys.CPP_BLOCK_COMMENT_END);
                
                // 如果在多行注释中，寻找结束标记
                if (inMultilineComment) {
                    int commentEndPos = findUnquotedString(line, blockCommentEnd);
                    
                    if (commentEndPos >= 0) {
                        // 多行注释结束
                        inMultilineComment = false;
                        
                        // 检查注释结束后是否还有代码
                        String afterComment = line.substring(commentEndPos + blockCommentEnd.length()).trim();
                        if (!afterComment.isEmpty() && !afterComment.startsWith(lineCommentStart)) {
                            result.addCodeLine();
                        } else {
                            result.addCommentLine();
                        }
                    } else {
                        // 仍在多行注释内
                        result.addCommentLine();
                    }
                    continue;
                }
                
                // 检查行首是否为单行注释 //
                if (trimmedLine.startsWith(lineCommentStart)) {
                    result.addCommentLine();
                    continue;
                }
                
                // 检查是否有行内多行注释开始 /*
                int blockCommentPos = findUnquotedString(line, blockCommentStart);
                
                if (blockCommentPos >= 0) {
                    // 有代码注释混合行
                    boolean hasCode = blockCommentPos > 0 && 
                            !line.substring(0, blockCommentPos).trim().isEmpty();
                    
                    // 寻找同一行内的多行注释结束符 */
                    int commentEndPos = findUnquotedString(line, blockCommentEnd, blockCommentPos + blockCommentStart.length());
                    
                    if (commentEndPos >= 0) {
                        // 多行注释在同一行内结束
                        String afterComment = line.substring(commentEndPos + blockCommentEnd.length()).trim();
                        boolean hasMoreCode = !afterComment.isEmpty() && !afterComment.startsWith(lineCommentStart);
                        
                        // 如果注释前有代码或注释后有代码，算作代码行，否则算作注释行
                        if (hasCode || hasMoreCode) {
                            result.addCodeLine();
                        } else {
                            result.addCommentLine();
                        }
                    } else {
                        // 多行注释没有在当前行结束
                        inMultilineComment = true;
                        
                        // 如果注释前有代码，则算作代码行，否则算作注释行
                        if (hasCode) {
                            result.addCodeLine();
                        } else {
                            result.addCommentLine();
                        }
                    }
                    continue;
                }
                
                // 检查行内单行注释
                int lineCommentPos = findUnquotedString(line, lineCommentStart);
                
                if (lineCommentPos >= 0) {
                    // 行内有单行注释，检查注释前是否有代码
                    String beforeComment = line.substring(0, lineCommentPos).trim();
                    
                    if (!beforeComment.isEmpty()) {
                        // 注释前有代码，算作代码行
                        result.addCodeLine();
                    } else {
                        // 注释前无代码，算作注释行
                        result.addCommentLine();
                    }
                    continue;
                }
                
                // 其它情况算作代码行
                result.addCodeLine();
            }
        }
        
        return result;
    }
    
    @Override
    public String[] getSupportedExtensions() {
        return SUPPORTED_EXTENSIONS;
    }
    
    /**
     * 查找未被引号包围的字符串的位置
     * 处理文本字符串、字符字面量中可能出现的注释字符的情况
     * 
     * @param line 要搜索的行
     * @param target 要查找的目标字符串
     * @return 找到的位置，如果未找到，则返回-1
     */
    private int findUnquotedString(String line, String target) {
        return findUnquotedString(line, target, 0);
    }
    
    /**
     * 从指定位置开始查找未被引号包围的字符串的位置
     * 处理文本字符串、字符字面量中可能出现的注释字符的情况
     * 
     * @param line 要搜索的行
     * @param target 要查找的目标字符串
     * @param fromIndex 搜索的起始位置
     * @return 找到的位置，如果未找到，则返回-1
     */
    private int findUnquotedString(String line, String target, int fromIndex) {
        boolean inSingleQuote = false; // 是否在单引号内
        boolean inDoubleQuote = false; // 是否在双引号内
        boolean escaped = false; // 是否被转义
        
        for (int i = fromIndex; i <= line.length() - target.length(); i++) {
            
            if (i < line.length()) {
                char c = line.charAt(i);
                
                // 处理转义字符
                if (escaped) {
                    escaped = false;
                    continue;
                }
                
                // 检查是否为转义符
                if (c == '\\') {
                    escaped = true;
                    continue;
                }
                
                // 处理引号
                if (c == '"' && !inSingleQuote) {
                    inDoubleQuote = !inDoubleQuote;
                    continue;
                }
                if (c == '\'' && !inDoubleQuote) {
                    inSingleQuote = !inSingleQuote;
                    continue;
                }
            }
            
            // 如果不在引号内，检查是否匹配目标字符串
            if (!inSingleQuote && !inDoubleQuote && i + target.length() <= line.length()) {
                boolean match = true;
                for (int j = 0; j < target.length(); j++) {
                    if (line.charAt(i + j) != target.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;
                }
            }
        }
        
        return -1; // 未找到
    }
}
