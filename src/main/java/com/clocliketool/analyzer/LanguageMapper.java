package com.clocliketool.analyzer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 语言映射器类，负责将文件扩展名映射到编程语言
 */
public class LanguageMapper {
    
    private static final Map<String, String> extensionLanguageMap = new HashMap<>();
    
    static {
        // C/C++统一归为C类
        extensionLanguageMap.put("c", "C");
        extensionLanguageMap.put("cpp", "C");
        extensionLanguageMap.put("cc", "C");
        
        // Ruby
        extensionLanguageMap.put("rb", "Ruby");
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
} 