package com.clocliketool.analyzer;

import com.clocliketool.counter.LineCounter;
import com.clocliketool.model.LineCountResult;
import com.clocliketool.util.DirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件分析器类，负责文件分析和统计
 */
public class FileAnalyzer {
    
    private final List<LineCounter> counters;
    private final Map<String, LineCountResult> languageResults = new HashMap<>();
    private int totalFiles = 0;
    
    public FileAnalyzer(List<LineCounter> counters) {
        this.counters = counters;
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
        for (LineCounter counter : counters) {
            if (counter.supportsFile(file)) {
                try {
                    LineCountResult result = counter.countLines(file);
                    String language = LanguageMapper.getLanguageForFile(file);
                    
                    // 更新语言汇总结果
                    if (languageResults.containsKey(language)) {
                        languageResults.get(language).merge(result);
                    } else {
                        languageResults.put(language, result);
                    }
                    
                    totalFiles++;
                    break; // 找到支持的计数器后不再继续
                } catch (IOException e) {
                    System.err.println("错误: 处理文件时出错: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 处理目录
     */
    private void processDirectory(File directory) {
        // 收集所有支持的扩展名
        Set<String> extensions = new HashSet<>();
        for (LineCounter counter : counters) {
            extensions.addAll(Arrays.asList(counter.getSupportedExtensions()));
        }
        
        // 扫描目录
        List<File> matchedFiles = DirectoryScanner.scanDirectory(
                directory, extensions.toArray(new String[0]));
        
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
        return Collections.unmodifiableMap(languageResults);
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
        languageResults.clear();
        totalFiles = 0;
    }
} 