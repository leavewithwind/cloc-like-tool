package com.clocliketool.analyzer;

import com.clocliketool.counter.LineCounter;
import com.clocliketool.exception.FileProcessingException;
import com.clocliketool.exception.InvalidArgumentException;
import com.clocliketool.model.LineCountResult;
import com.clocliketool.util.DirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件分析器类，负责文件分析和统计
 * 修改为只处理指定语言的文件
 */
public class FileAnalyzer {
    
    private final LineCounter counter; // 改为单个计数器
    private LineCountResult result = new LineCountResult(); // 直接使用一个结果对象
    private String languageName; // 存储语言名称
    
    /**
     * 构造函数
     * 
     * @param counters 要使用的语言计数器列表
     * @throws InvalidArgumentException 如果提供的计数器列表为空
     */
    public FileAnalyzer(LineCounter counter) {
        // 检查参数有效性
        if (counter == null) {
            throw new InvalidArgumentException("counters", "计数器列表不能为null");
        }
        
        // 只使用列表中的第一个计数器
        this.counter = counter;
        
        // 获取语言名称
        String[] extensions = this.counter.getSupportedExtensions();
        if (extensions.length > 0) {
            this.languageName = LanguageMapper.getLanguageByExtension(extensions[0]);
        } else {
            this.languageName = "Unknown";
        }
    }
    
    /**
     * 分析指定路径的文件或目录
     * 
     * @param pathsToAnalyze 要分析的路径数组
     * @return 分析是否成功完成
     * @throws InvalidArgumentException 如果路径数组为空
     */
    public boolean analyzePaths(String[] pathsToAnalyze) {
        if (pathsToAnalyze == null || pathsToAnalyze.length == 0) {
            throw new InvalidArgumentException("pathsToAnalyze", "未指定要分析的路径");
        }
        
        for (String path : pathsToAnalyze) {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("警告: 路径不存在: " + path);
                continue;
            }
            
            if (file.isFile()) {
                processFile(file);
            } else if (file.isDirectory()) {
                processDirectory(file);
            }
        }
        
        return result.getFileCount() > 0;
    }
    
    /**
     * 处理单个文件
     * 
     * @param file 要处理的文件
     * @throws FileProcessingException 如果文件处理失败
     */
    private void processFile(File file) {
        if (counter.supportsFile(file)) {
            try {
                LineCountResult fileResult = counter.countLines(file);
                result.merge(fileResult); // 合并结果
            } catch (IOException e) {
                throw new FileProcessingException(file, e);
            }
        }
    }
    
    /**
     * 处理目录
     */
    private void processDirectory(File directory) {
        // 获取当前计数器支持的扩展名和特殊文件名
        String[] extensions = counter.getSupportedExtensions();
        String[] specialFiles = counter.getSupportedSpecialFiles();
        
        // 扫描目录，同时处理扩展名和特殊文件名
        List<File> matchedFiles = DirectoryScanner.scanDirectory(
                directory, extensions, specialFiles);
        
        // 处理找到的文件
        for (File matchedFile : matchedFiles) {
            processFile(matchedFile);
        }
    }
    
    /**
     * 获取分析结果
     * 
     * @return 按语言分类的统计结果
     */
    public Map<String, LineCountResult> getLanguageResults() {
        Map<String, LineCountResult> results = new HashMap<>();
        results.put(languageName, result);
        return Collections.unmodifiableMap(results);
    }
}