package com.clocliketool.analyzer;

import com.clocliketool.counter.LineCounter;
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
    private int totalFiles = 0;
    private String languageName; // 存储语言名称
    
    public FileAnalyzer(List<LineCounter> counters) {
        // 只使用列表中的第一个计数器
        if (counters != null && !counters.isEmpty()) {
            this.counter = counters.get(0);
            // 获取语言名称
            String[] extensions = this.counter.getSupportedExtensions();
            if (extensions.length > 0) {
                this.languageName = LanguageMapper.getLanguageByExtension(extensions[0]);
            } else {
                this.languageName = "Unknown";
            }
        } else {
            throw new IllegalArgumentException("至少需要提供一个计数器");
        }
    }
    
    /**
     * 分析指定路径的文件或目录
     * 
     * @param pathsToAnalyze 要分析的路径数组
     * @return 分析是否成功完成
     */
    public boolean analyzePaths(String[] pathsToAnalyze) {
        if (pathsToAnalyze == null || pathsToAnalyze.length == 0) {
            System.err.println("错误: 未指定要分析的路径");
            return false;
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
        
        return totalFiles > 0;
    }
    
    /**
     * 处理单个文件
     */
    private void processFile(File file) {
        if (counter.supportsFile(file)) {
            try {
                LineCountResult fileResult = counter.countLines(file);
                result.merge(fileResult); // 合并结果
                totalFiles++;
            } catch (IOException e) {
                System.err.println("错误: 处理文件时出错: " + file.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 处理目录
     */
    private void processDirectory(File directory) {
        // 获取当前计数器支持的扩展名
        String[] extensions = counter.getSupportedExtensions();
        
        // 扫描目录
        List<File> matchedFiles = DirectoryScanner.scanDirectory(
                directory, extensions);
        
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
    
    /**
     * 获取处理的文件总数
     * 
     * @return 文件总数
     */
    public int getTotalFiles() {
        return totalFiles;
    }
    
    /**
     * 清除当前分析结果
     */
    public void clear() {
        result = new LineCountResult();
        totalFiles = 0;
    }
} 