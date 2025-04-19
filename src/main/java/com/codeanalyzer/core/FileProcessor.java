package com.codeanalyzer.core;

import com.codeanalyzer.parser.CodeParser;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件处理器，负责遍历目录和处理文件
 */
public class FileProcessor {
    
    private final List<CodeParser> parsers;
    private final Map<String, LineCounter> fileResults = new HashMap<>();
    private final Map<String, LineCounter> languageResults = new HashMap<>();
    private int processedFiles = 0;
    
    public FileProcessor(List<CodeParser> parsers) {
        this.parsers = parsers;
    }
    
    /**
     * 处理指定的路径（文件或目录）
     * @param path 要处理的路径
     * @return 统计结果，Key为语言名称，Value为统计结果
     */
    public Map<String, LineCounter> process(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("错误: 路径不存在: " + path);
            return Collections.emptyMap();
        }
        
        processPath(file);
        return languageResults;
    }
    
    /**
     * 递归处理路径
     */
    private void processPath(File file) {
        if (file.isFile()) {
            processFile(file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    processPath(subFile);
                }
            }
        }
    }
    
    /**
     * 处理单个文件
     */
    private void processFile(File file) {
        for (CodeParser parser : parsers) {
            if (parser.supportsFile(file)) {
                try {
                    LineCounter counter = parser.parseFile(file);
                    String extension = getFileExtension(file);
                    String language = getLanguageByExtension(extension);
                    
                    // 保存文件结果
                    fileResults.put(file.getAbsolutePath(), counter);
                    
                    // 更新语言汇总结果
                    if (languageResults.containsKey(language)) {
                        languageResults.get(language).merge(counter);
                    } else {
                        languageResults.put(language, new LineCounter(
                                counter.getCodeLines(),
                                counter.getCommentLines(),
                                counter.getBlankLines()));
                    }
                    
                    processedFiles++;
                    break; // 找到支持的解析器后不再继续
                } catch (IOException e) {
                    System.err.println("错误: 处理文件时出错: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    /**
     * 根据扩展名获取语言名称
     */
    private String getLanguageByExtension(String extension) {
        switch (extension) {
            case "c":
            case "h":
                return "C";
            case "cpp":
            case "cc":
            case "hpp":
                return "C++";
            case "rb":
                return "Ruby";
            default:
                return "Unknown";
        }
    }
    
    /**
     * 获取处理的文件数量
     */
    public int getProcessedFilesCount() {
        return processedFiles;
    }
    
    /**
     * 获取单个文件的统计结果
     */
    public Map<String, LineCounter> getFileResults() {
        return Collections.unmodifiableMap(fileResults);
    }
    
    /**
     * 打印结果
     */
    public void printResults() {
        // 打印标题
        System.out.println("语言\t\t代码行\t\t注释行\t\t空行\t\t总行数");
        System.out.println("----------------------------------------------------");
        
        // 计算总计
        LineCounter total = new LineCounter();
        
        // 按字母顺序打印每种语言的结果
        List<String> languages = new ArrayList<>(languageResults.keySet());
        Collections.sort(languages);
        
        for (String language : languages) {
            LineCounter counter = languageResults.get(language);
            System.out.printf("%-10s\t%d\t\t%d\t\t%d\t\t%d%n",
                    language,
                    counter.getCodeLines(),
                    counter.getCommentLines(),
                    counter.getBlankLines(),
                    counter.getTotalLines());
            
            total.merge(counter);
        }
        
        // 打印总计
        System.out.println("----------------------------------------------------");
        System.out.printf("%-10s\t%d\t\t%d\t\t%d\t\t%d%n",
                "总计",
                total.getCodeLines(),
                total.getCommentLines(),
                total.getBlankLines(),
                total.getTotalLines());
        
        System.out.println("\n处理的文件数量: " + processedFiles);
    }
} 