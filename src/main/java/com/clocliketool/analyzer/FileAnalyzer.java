package com.clocliketool.analyzer;

import com.clocliketool.config.AppConfig;
import com.clocliketool.config.ConfigKeys;
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
    private int totalFiles = 0;
    private String languageName; // 存储语言名称
    
    /**
     * 构造函数
     * 
     * @param counters 要使用的语言计数器列表
     * @throws InvalidArgumentException 如果提供的计数器列表为空
     */
    public FileAnalyzer(List<LineCounter> counters) {
        // 检查参数有效性
        if (counters == null) {
            throw new InvalidArgumentException("counters", "计数器列表不能为null");
        }
        
        if (counters.isEmpty()) {
            throw new InvalidArgumentException("counters", "计数器列表不能为空，请提供至少一个有效的语言计数器");
        }
        
        // 只使用列表中的第一个计数器
        this.counter = counters.get(0);
        
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
        
        boolean hasResults = false;
        
        for (String path : pathsToAnalyze) {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println(AppConfig.formatMessage(ConfigKeys.ERROR_PATH_NONEXISTENT, path));
                continue;
            }
            
            hasResults |= analyzeFile(file);
        }
        
        return hasResults;
    }
    
    /**
     * 分析单个文件或目录
     * 
     * @param file 要分析的文件或目录
     * @return 是否找到匹配的文件
     */
    private boolean analyzeFile(File file) {
        if (file.isFile()) {
            if (counter.supportsFile(file)) {
                try {
                    LineCountResult fileResult = counter.countLines(file);
                    result.merge(fileResult);
                    totalFiles++;
                    return true;
                } catch (IOException e) {
                    throw new FileProcessingException(file, e);
                }
            }
            return false;
        } else if (file.isDirectory()) {
            return analyzeDirectory(file);
        }
        return false;
    }
    
    /**
     * 分析目录
     * 
     * @param directory 要分析的目录
     * @return 是否找到匹配的文件
     */
    private boolean analyzeDirectory(File directory) {
        List<File> matchedFiles = DirectoryScanner.scanDirectory(directory, counter.getSupportedExtensions());
        
        if (matchedFiles.isEmpty()) {
            return false;
        }
        
        boolean hasResults = false;
        
        for (File file : matchedFiles) {
            try {
                LineCountResult fileResult = counter.countLines(file);
                result.merge(fileResult);
                totalFiles++;
                hasResults = true;
            } catch (IOException e) {
                throw new FileProcessingException(file, e);
            }
        }
        
        return hasResults;
    }
    
    /**
     * 获取按语言分类的统计结果
     * 
     * @return 包含每种语言统计结果的映射
     */
    public Map<String, LineCountResult> getLanguageResults() {
        Map<String, LineCountResult> results = new HashMap<>();
        results.put(languageName, result);
        return results;
    }
    
    /**
     * 获取处理的文件总数
     * 
     * @return 文件总数
     */
    public int getTotalFiles() {
        return totalFiles;
    }
}