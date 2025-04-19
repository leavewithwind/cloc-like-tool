package com.codeanalyzer.parser;

import com.codeanalyzer.core.LineCounter;

import java.io.File;
import java.io.IOException;

/**
 * 代码解析器接口，定义统计代码行数的行为
 * 策略模式的核心接口，不同语言的解析器实现该接口
 */
public interface CodeParser {
    
    /**
     * 解析单个文件并统计行数
     * @param file 要解析的文件
     * @return 统计结果
     * @throws IOException 如果文件读取失败
     */
    LineCounter parseFile(File file) throws IOException;
    
    /**
     * 获取此解析器支持的文件扩展名
     * @return 支持的文件扩展名数组
     */
    String[] getSupportedExtensions();
    
    /**
     * 检查文件是否受此解析器支持
     * @param file 要检查的文件
     * @return 如果文件受支持则为true，否则为false
     */
    default boolean supportsFile(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }
        
        String fileName = file.getName().toLowerCase();
        for (String ext : getSupportedExtensions()) {
            if (fileName.endsWith("." + ext.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
} 