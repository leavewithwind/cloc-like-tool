package com.clocliketool.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 语言映射器，将文件扩展名映射到编程语言
 */
public class LanguageMapper {
    
    private static final Map<String, String> extensionLanguageMap = new HashMap<>();
    // private static final Map<String, String> specialFileMap = new HashMap<>();
    
    static {
        // C/C++统一归为C类 - 与CLineCounter中的SUPPORTED_EXTENSIONS保持一致
        extensionLanguageMap.put("c", "C");
        extensionLanguageMap.put("cpp", "C");
        extensionLanguageMap.put("cc", "C");
        
        // Ruby - 与RubyLineCounter中的SUPPORTED_EXTENSIONS保持一致
        extensionLanguageMap.put("rb", "Ruby");
        extensionLanguageMap.put("rake", "Ruby");
        extensionLanguageMap.put("gemspec", "Ruby");
        extensionLanguageMap.put("rbw", "Ruby");
        extensionLanguageMap.put("rbx", "Ruby");
        extensionLanguageMap.put("rjs", "Ruby");
        extensionLanguageMap.put("rabl", "Ruby");
        extensionLanguageMap.put("ru", "Ruby");
        extensionLanguageMap.put("thor", "Ruby");
        extensionLanguageMap.put("podspec", "Ruby");
        extensionLanguageMap.put("rxml", "Ruby");
        
        // Ruby特殊文件 - 与RubyLineCounter中的SUPPORTED_SPECIAL_FILES保持一致。目前无相关逻辑，无需启用

        // for (String specialFile : new String[] {
        //     "Rakefile", "Gemfile", "Capfile", "Guardfile", "Brewfile", "Vagrantfile", "Thorfile",
        //     "config.ru", "Berksfile", "Deliverfile", "Fastfile", "Snapfile", "Podfile", "Dangerfile",
        //     "Appfile", "Matchfile", "Pluginfile", "Scanfile", "Gymfile", "Cartfile"
        // }) {
        //     specialFileMap.put(specialFile, "Ruby");
        // }
    }
    
    /**
     * 根据文件获取编程语言名称。目前无相关逻辑，无需启用
     * 
     * @param file 要识别的文件
     * @return 语言名称，如果不支持则返回"Unknown"
     */
    // public static String getLanguageForFile(File file) {
    //     if (file == null) {
    //         return "Unknown";
    //     }
        
    //     // 首先检查是否为特殊文件
    //     String fileName = file.getName();
    //     if (specialFileMap.containsKey(fileName)) {
    //         return specialFileMap.get(fileName);
    //     }
        
    //     // 然后检查扩展名
    //     String extension = getFileExtension(file);
    //     return getLanguageByExtension(extension);
    // }
    
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
    
} 