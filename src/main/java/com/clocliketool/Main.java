package com.clocliketool;

import com.clocliketool.counter.LineCounter;
import com.clocliketool.counter.LineCounterFactory;
import com.clocliketool.model.LineCountResult;
import com.clocliketool.util.DirectoryScanner;
import com.clocliketool.util.ResultFormatter;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 应用程序主类，处理命令行参数并执行代码行统计
 */
public class Main {
    
    public static void main(String[] args) {
        // 创建命令行选项
        Options options = createOptions();
        
        try {
            // 解析命令行参数
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            
            // 显示帮助信息
            if (cmd.hasOption("h") || args.length == 0) {
                printHelp(options);
                System.exit(0);
            }
            
            // 获取指定的语言
            List<LineCounter> counters = getSelectedCounters(cmd);
            
            // 获取要处理的路径
            String[] paths = cmd.getArgs();
            if (paths.length == 0) {
                System.err.println("错误: 未指定要分析的路径");
                printHelp(options);
                System.exit(1);
            }
            
            // 处理文件和目录
            Map<String, LineCountResult> languageResults = new HashMap<>();
            int totalFiles = 0;
            
            for (String path : paths) {
                File file = new File(path);
                if (!file.exists()) {
                    System.err.println("警告: 路径不存在: " + path);
                    continue;
                }
                
                if (file.isFile()) {
                    processFile(file, counters, languageResults);
                    totalFiles++;
                } else if (file.isDirectory()) {
                    // 收集所有支持的扩展名
                    Set<String> extensions = new HashSet<>();
                    for (LineCounter counter : counters) {
                        extensions.addAll(Arrays.asList(counter.getSupportedExtensions()));
                    }
                    
                    // 扫描目录
                    List<File> matchedFiles = DirectoryScanner.scanDirectory(
                            file, extensions.toArray(new String[0]));
                    
                    // 处理找到的文件
                    for (File matchedFile : matchedFiles) {
                        processFile(matchedFile, counters, languageResults);
                        totalFiles++;
                    }
                }
            }
            
            // 打印结果
            if (totalFiles > 0) {
                String formattedResults = ResultFormatter.formatResults(languageResults, totalFiles);
                System.out.println(formattedResults);
            } else {
                System.out.println("未找到匹配的文件。");
            }
            
        } catch (ParseException e) {
            System.err.println("参数解析错误: " + e.getMessage());
            printHelp(options);
            System.exit(1);
        }
    }
    
    /**
     * 处理单个文件
     */
    private static void processFile(File file, List<LineCounter> counters, 
                                  Map<String, LineCountResult> languageResults) {
        for (LineCounter counter : counters) {
            if (counter.supportsFile(file)) {
                try {
                    LineCountResult result = counter.countLines(file);
                    String extension = getFileExtension(file);
                    String language = getLanguageByExtension(extension);
                    
                    // 更新语言汇总结果
                    if (languageResults.containsKey(language)) {
                        languageResults.get(language).merge(result);
                    } else {
                        languageResults.put(language, result);
                    }
                    
                    break; // 找到支持的计数器后不再继续
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
    private static String getFileExtension(File file) {
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
    private static String getLanguageByExtension(String extension) {
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
     * 创建命令行选项
     */
    private static Options createOptions() {
        Options options = new Options();
        
        Option helpOpt = Option.builder("h")
                .longOpt("help")
                .desc("显示帮助信息")
                .build();
        
        Option langOpt = Option.builder("l")
                .longOpt("language")
                .hasArg()
                .argName("语言")
                .desc("指定要统计的语言 (支持: c, cpp, ruby, all)")
                .build();
        
        options.addOption(helpOpt);
        options.addOption(langOpt);
        
        return options;
    }
    
    /**
     * 打印帮助信息
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("cloc [选项] <路径1> [<路径2> ...]", 
                "代码行统计工具 - 统计C/C++和Ruby源文件中的代码行、注释行和空行", 
                options, 
                "\n支持的语言: " + LineCounterFactory.getSupportedLanguages());
    }
    
    /**
     * 获取用户选择的行计数器
     */
    private static List<LineCounter> getSelectedCounters(CommandLine cmd) {
        List<LineCounter> selectedCounters = new ArrayList<>();
        
        if (cmd.hasOption("l")) {
            String langParam = cmd.getOptionValue("l").toLowerCase();
            if ("all".equals(langParam)) {
                selectedCounters.addAll(LineCounterFactory.getAllCounters());
            } else {
                LineCounter counter = LineCounterFactory.createCounter(langParam);
                if (counter != null) {
                    selectedCounters.add(counter);
                } else {
                    System.err.println("警告: 不支持的语言: " + langParam);
                    System.err.println("使用所有支持的计数器...");
                    selectedCounters.addAll(LineCounterFactory.getAllCounters());
                }
            }
        } else {
            // 默认使用所有计数器
            selectedCounters.addAll(LineCounterFactory.getAllCounters());
        }
        
        return selectedCounters;
    }
}
