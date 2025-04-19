package com.clocliketool.analyzer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 语言映射器类，负责将文件扩展名映射到编程语言
 * 扩展支持更多Ruby文件扩展名
 */
public class LanguageMapper {
    
    private static final Map<String, String> extensionLanguageMap = new HashMap<>();
    
    static {
        // C/C++
        extensionLanguageMap.put("c", "C");
        extensionLanguageMap.put("cpp", "C++");
        extensionLanguageMap.put("cc", "C++");
        
        // Ruby
        extensionLanguageMap.put("rb", "Ruby");
        extensionLanguageMap.put("rbw", "Ruby");     // Ruby for Windows
        extensionLanguageMap.put("rake", "Ruby");    // Ruby Rakefile
        extensionLanguageMap.put("gemspec", "Ruby"); // Ruby Gem规范文件
        extensionLanguageMap.put("ru", "Ruby");      // Ruby rack配置文件
        extensionLanguageMap.put("thor", "Ruby");    // Ruby Thor文件
        extensionLanguageMap.put("podspec", "Ruby"); // Ruby CocoaPods规范文件
        extensionLanguageMap.put("rbs", "Ruby");     // Ruby类型签名文件
    }
    
    /**
     * 根据文件获取编程语言名称
     * 
     * @param file 要识别的文件
     * @return 语言名称，如果不支持则返回"Unknown"
     */
    public static String getLanguageForFile(File file) {
        String extension = getFileExtension(file);
        return getLanguageByExtension(extension);
    }
    
    /**
     * 根据扩展名获取语言名称
     * 
     * @param extension 文件扩展名
     * @return 语言名称，如果不支持则返回"Unknown"
     */
    public static String getLanguageByExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "Unknown";
        }
        
        extension = extension.toLowerCase();
        return extensionLanguageMap.getOrDefault(extension, "Unknown");
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param file 要处理的文件
     * @return 文件扩展名，如果没有则返回空字符串
     */
    public static String getFileExtension(File file) {
        if (file == null) {
            return "";
        }
        
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    /**
     * 注册新的语言映射
     * 
     * @param extension 文件扩展名
     * @param language 语言名称
     */
    public static void registerLanguage(String extension, String language) {
        if (extension != null && !extension.isEmpty() && language != null && !language.isEmpty()) {
            extensionLanguageMap.put(extension.toLowerCase(), language);
        }
    }
} 