package com.clocliketool;

import com.clocliketool.analyzer.FileAnalyzer;
import com.clocliketool.cli.CliTableFormatter;
import com.clocliketool.cli.CommandLineProcessor;
import com.clocliketool.counter.LineCounter;
import com.clocliketool.exception.StatusCode;
import com.clocliketool.exception.ExceptionHandler;
import com.clocliketool.model.LineCountResult;

import java.util.Map;

/**
 * 程序入口
 */
public class Main {

    public static void main(String[] args) {
        int exitCode = run(args);
        
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
    
    /**
     * 启动应用程序
     * 
     * @param args 命令行参数
     * @return 执行结果码，0表示成功，非0表示失败
     */
    private static int run(String[] args) {
        // 完全没有参数时显示帮助菜单
        if (args.length == 0) {
            CommandLineProcessor cmdProcessor = new CommandLineProcessor(args);
            cmdProcessor.printHelp();
            return StatusCode.SUCCESS.getCode();
        }
        
        // 解析命令行参数
        CommandLineProcessor cmdProcessor = new CommandLineProcessor(args);
        
        try {
            // 尝试解析命令行参数
            if (!cmdProcessor.parseArguments()) {
                // 如果解析失败，打印帮助信息以指导用户正确使用命令行参数
                cmdProcessor.printHelp();
                return StatusCode.COMMAND_LINE_ERROR.getCode(); // 返回命令行参数错误的状态码
            }
            
            // 获取要处理的路径
            String[] paths = cmdProcessor.getPaths();
            if (paths.length == 0) {
                System.err.println("错误: 未指定要分析的路径");
                cmdProcessor.printHelp();
                return StatusCode.PATH_NOT_FOUND.getCode();
            }
            
            // 获取指定的语言计数器
            LineCounter counter = cmdProcessor.getSelectedCounters();
            
            // 执行文件分析
            FileAnalyzer analyzer = new FileAnalyzer(counter);
            boolean hasResults = analyzer.analyzePaths(paths);
            
            // 处理结果
            if (hasResults) {
                Map<String, LineCountResult> languageResults = analyzer.getLanguageResults();
                
                // 打印结果
                String formattedResults = CliTableFormatter.formatResults(languageResults);
                System.out.println(formattedResults);
            } else {
                System.out.println("未找到匹配的文件。");
            }
            
            return StatusCode.SUCCESS.getCode();
            
        } catch (Exception e) {
            return ExceptionHandler.handleException(e); // 使用异常处理器统一处理异常
        }
    }
}
