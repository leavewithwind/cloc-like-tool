package com.clocliketool;

import com.clocliketool.analyzer.FileAnalyzer;
import com.clocliketool.cli.CommandLineProcessor;
import com.clocliketool.counter.LineCounter;
import com.clocliketool.exception.ErrorCode;
import com.clocliketool.exception.ExceptionHandler;
import com.clocliketool.model.LineCountResult;
import com.clocliketool.util.ResultFormatter;

import java.util.List;
import java.util.Map;

/**
 * 应用程序运行器类，负责整体执行流程的协调
 */
public class ApplicationRunner {
    
    private final String[] args;
    
    public ApplicationRunner(String[] args) {
        this.args = args;
    }
    
    /**
     * 执行应用程序
     * 
     * @return 执行结果码，0表示成功，非0表示失败
     */
    public int run() {
        // 完全没有参数时显示帮助菜单作为彩蛋
        if (args.length == 0) {
            CommandLineProcessor cmdProcessor = new CommandLineProcessor(args);
            cmdProcessor.printHelp();
            return ErrorCode.SUCCESS.getCode();
        }
        
        // 解析命令行参数
        CommandLineProcessor cmdProcessor = new CommandLineProcessor(args);
        
        try {
            if (!cmdProcessor.parseArguments()) {
                // 直接显示帮助信息，不额外显示语言支持提示，避免重复
                cmdProcessor.printHelp();
                return ErrorCode.COMMAND_LINE_ERROR.getCode();
            }
            
            // 获取要处理的路径
            String[] paths = cmdProcessor.getPaths();
            if (paths.length == 0) {
                System.err.println("错误: 未指定要分析的路径");
                cmdProcessor.printHelp();
                return ErrorCode.PATH_NOT_FOUND.getCode();
            }
            
            // 获取指定的语言计数器
            List<LineCounter> counters = cmdProcessor.getSelectedCounters();
            
            // 执行文件分析
            FileAnalyzer analyzer = new FileAnalyzer(counters);
            boolean hasResults = analyzer.analyzePaths(paths);
            
            // 处理结果
            if (hasResults) {
                Map<String, LineCountResult> languageResults = analyzer.getLanguageResults();
                int totalFiles = analyzer.getTotalFiles();
                
                // 打印结果
                String formattedResults = ResultFormatter.formatResults(languageResults, totalFiles);
                System.out.println(formattedResults);
            } else {
                System.out.println("未找到匹配的文件。");
            }
            
            return ErrorCode.SUCCESS.getCode();
            
        } catch (Exception e) {
            // 使用异常处理器统一处理异常
            return ExceptionHandler.handleException(e);
        }
    }
} 