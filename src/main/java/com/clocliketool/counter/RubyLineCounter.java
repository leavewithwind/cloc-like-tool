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
    
    private static final String[] SUPPORTED_EXTENSIONS = {"rb", "rake", "gemspec", "rbw", "rbx", "rjs", "rabl", "ru", "thor", "podspec", "rxml"};
    
    private static final String[] SUPPORTED_SPECIAL_FILES = {
        "Rakefile", "Gemfile", "Capfile", "Guardfile", "Brewfile", "Vagrantfile", "Thorfile",
        "config.ru", "Berksfile", "Deliverfile", "Fastfile", "Snapfile", "Podfile", "Dangerfile",
        "Appfile", "Matchfile", "Pluginfile", "Scanfile", "Gymfile", "Cartfile"
    };
    
    @Override
    public LineCountResult countLines(File file) throws IOException {
        LineCountResult result = new LineCountResult();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inMultiLineComment = false;
            boolean inHeredoc = false;
            String heredocMarker = null;
            
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                // 空行检测
                if (trimmedLine.isEmpty()) {
                    result.addBlankLine();
                    continue;
                }
                
                // 处理多行注释内部
                if (inMultiLineComment) {
                    if (trimmedLine.equals("=end")) {
                        inMultiLineComment = false;
                        result.addCommentLine();
                    } else {
                        result.addCommentLine();
                    }
                    continue;
                }
                
                // 处理HEREDOC内部
                if (inHeredoc) {
                    if (trimmedLine.equals(heredocMarker)) {
                        inHeredoc = false;
                        result.addCodeLine();
                    } else {
                        result.addCodeLine();
                    }
                    continue;
                }
                
                // 处理=begin多行注释开始
                if (trimmedLine.equals("=begin")) {
                    inMultiLineComment = true;
                    result.addCommentLine();
                    continue;
                }
                
                // 处理shebang行
                if (trimmedLine.startsWith("#!")) {
                    result.addCodeLine();
                    continue;
                }
                
                // 处理单行注释 - 关键修复：任何以#开头的行，无论有没有缩进，都算作注释行
                if (trimmedLine.startsWith("#")) {
                    result.addCommentLine();
                    continue;
                }
                
                // 检测HEREDOC开始
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
                    // 行首是注释字符，算作注释行（这应该已经被前面的条件捕获，但为了安全起见）
                    result.addCommentLine();
                    continue;
                }
                
                // 其他情况视为代码行
                result.addCodeLine();
            }
        }
        
        result.incrementFileCount();
        return result;
    }
    
    @Override
    public String[] getSupportedExtensions() {
        return SUPPORTED_EXTENSIONS;
    }
    
    @Override
    public String[] getSupportedSpecialFiles() {
        return SUPPORTED_SPECIAL_FILES;
    }
    
    /**
     * 找到HEREDOC的标记
     * 返回HEREDOC的标记字符串，如果不是HEREDOC则返回null
     */
    private String findHeredocMarker(String line) {
        // 匹配各种HEREDOC格式：<<MARKER, <<-MARKER, <<~MARKER, <<"MARKER", <<'MARKER'
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
     * 返回注释字符的索引，如果不存在则返回-1
     */
    private int findUnquotedCommentIndex(String line) {
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean escaped = false;
        
        for (int i = 0; i < line.length(); i++) {
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
            } else if (c == '#' && !inSingleQuote && !inDoubleQuote) {
                return i; // 返回注释字符的位置
            }
        }
        
        return -1; // 没有找到注释字符
    }
    
    /**
     * 检查指定位置是否在字符串或注释内
     */
    private boolean isInStringOrComment(String line, int position) {
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean escaped = false;
        
        for (int i = 0; i < position; i++) {
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
            } else if (c == '#' && !inSingleQuote && !inDoubleQuote) {
                return true; // 位置在注释内
            }
        }
        
        return inSingleQuote || inDoubleQuote; // 位置在字符串内
    }
}