package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Ruby代码行计数器实现
 * 处理.rb文件的代码行统计
 * 基于cloc工具的处理逻辑重写
 */
public class RubyLineCounter extends LineCounter {

    // 支持的文件扩展名数组，包含所有Ruby相关的文件扩展名
    private static final String[] SUPPORTED_EXTENSIONS = {"rb", "rake", "gemspec", "rbw", "rbx", "rjs", "rabl", "ru", "thor", "podspec", "rxml"};
    
    // 支持的特殊文件名数组，包含所有不带扩展名的Ruby相关文件名
    private static final String[] SUPPORTED_SPECIAL_FILES = {
        "Rakefile", "Gemfile", "Capfile", "Guardfile", "Brewfile", "Vagrantfile", "Thorfile",
        "config.ru", "Berksfile", "Deliverfile", "Fastfile", "Snapfile", "Podfile", "Dangerfile",
        "Appfile", "Matchfile", "Pluginfile", "Scanfile", "Gymfile", "Cartfile"
    };
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult(); // 存储计数结果
        
        // 使用BufferedReader读取文件内容
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line; // 当前读取的行
            boolean inMultiLineComment = false; // 标记是否在多行注释中
            boolean inHeredoc = false; // 标记是否在HEREDOC中
            String heredocMarker = null; // HEREDOC的结束标记
            
            // 逐行读取文件内容
            while ((line = reader.readLine()) != null) {
                // 去除行首尾空白字符
                String trimmedLine = line.trim();
                
                // 空行检测
                if (trimmedLine.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 多行注释内部处理
                if (inMultiLineComment) {
                    if (trimmedLine.equals("=end")) {
                        inMultiLineComment = false;
                        result.addCommentLine();
                    } else {
                        result.addCommentLine();
                    }
                    continue;
                }
                
                // HEREDOC内部处理
                if (inHeredoc) {
                    if (trimmedLine.equals(heredocMarker)) {
                        inHeredoc = false;
                        result.addCodeLine();
                    } else {
                        result.addCodeLine();
                    }
                    continue;
                }
                
                // =begin开始多行注释处理
                if (trimmedLine.equals("=begin")) {
                    inMultiLineComment = true;
                    result.addCommentLine();
                    continue;
                }
                
                // shebang行（文件头部以#!开头的行）处理
                if (trimmedLine.startsWith("#!")) {
                    result.addCodeLine();
                    continue;
                }
                
                // 处理单行注释 - 任何以#开头的行，无论有没有缩进，都算作注释行
                if (trimmedLine.startsWith("#")) {
                    result.addCommentLine();
                    continue;
                }
                
                // HEREDOC开头的处理
                String marker = findHeredocMarker(line);
                if (marker != null) {
                    inHeredoc = true;
                    heredocMarker = marker;
                    result.addCodeLine();
                    continue;
                }
                
                // 检测行内注释（在代码之后）
                int commentIndex = findUnquotedCommentIndex(line);
                if (commentIndex > 0) {
                    // 行内含有注释但不是行首，算作代码行
                    result.addCodeLine();
                    continue;
                } else if (commentIndex == 0) {
                    // 行首是注释字符，算作注释行
                    result.addCommentLine();
                    continue;
                }
                
                // 其他视为代码行
                result.addCodeLine();
            }
        }
        
        result.incrementFileCount(); // 增加文件计数
        return result;
    }
    
    @Override
    public String[] getSupportedExtensions() {
        return SUPPORTED_EXTENSIONS; // 返回支持的文件扩展名数组
    }
    
    @Override
    public String[] getSupportedSpecialFiles() {
        return SUPPORTED_SPECIAL_FILES; // 返回支持的特殊文件名数组
    }
    
    /**
     * 找到HEREDOC的标记，返回HEREDOC的标记字符串
     * 如不是HEREDOC返回null
     */
    private String findHeredocMarker(String line) {
        // 匹配各类HEREDOC格式：<<MARKER, <<-MARKER, <<~MARKER, <<"MARKER", <<'MARKER'
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "<<[-~]?(?:\"|')?([A-Za-z0-9_]+)(?:\"|')?");
        java.util.regex.Matcher matcher = pattern.matcher(line);
        
        while (matcher.find()) {
            int start = matcher.start();
            // 确保HEREDOC标记不在字符串或注释内
            if (!isInStringOrComment(line, start)) {
                return matcher.group(1);
            }
        }
        
        return null;
    }
    
    /**
     * 找到不在字符串内的注释字符的位置
     *
     * @param line 要检查的字符串行
     * @return 注释字符的索引，如不存在返回-1
     */
    private int findUnquotedCommentIndex(String line) {
        boolean inSingleQuote = false; // 标记是否在单引号内
        boolean inDoubleQuote = false; // 标记是否在双引号内
        boolean escaped = false; // 标记是否遇到转义字符
        
        // 遍历字符串中的每个字符
        for (int i = 0; i < line.length(); i++) {
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
                inDoubleQuote = !inDoubleQuote; // 切换双引号状态
            } 
            // 检查是否遇到单引号，并且不在双引号内
            else if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote; // 切换单引号状态
            } 
            // 检查是否遇到注释字符，并且不在引号内
            else if (c == '#' && !inSingleQuote && !inDoubleQuote) {
                return i; // 返回注释字符的位置
            }
        }
        
        return -1; // 没有找到注释字符
    }
    
    /**
     * 检查指定位置是否在字符串或注释内
     * 
     * @param line 要检查的字符串行
     * @param position 要检查的位置索引
     * @return 如果位置在字符串或注释内返回true，否则返回false
     */
    private boolean isInStringOrComment(String line, int position) {
        boolean inSingleQuote = false; 
        boolean inDoubleQuote = false; 
        boolean escaped = false; // 标记是否遇到转义字符
        
        // 遍历字符串直到指定位置
        for (int i = 0; i < position; i++) {
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
                inDoubleQuote = !inDoubleQuote; // 切换双引号状态
            } 
            // 检查是否遇到单引号，并且不在双引号内
            else if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote; // 切换单引号状态
            } 
            // 检查是否遇到注释字符，并且不在引号内
            else if (c == '#' && !inSingleQuote && !inDoubleQuote) {
                return true; // 位置在注释内
            }
        }
        return inSingleQuote || inDoubleQuote; // 返回位置是否在字符串内
    }
}