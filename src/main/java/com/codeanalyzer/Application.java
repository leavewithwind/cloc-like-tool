package com.codeanalyzer;

import com.codeanalyzer.core.FileProcessor;
import com.codeanalyzer.factory.ParserFactory;
import com.codeanalyzer.parser.CodeParser;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用程序主类，处理命令行参数并执行代码行统计
 */
public class Application {
    
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
            List<CodeParser> parsers = getSelectedParsers(cmd);
            
            // 获取要处理的路径
            String[] paths = cmd.getArgs();
            if (paths.length == 0) {
                System.err.println("错误: 未指定要分析的路径");
                printHelp(options);
                System.exit(1);
            }
            
            // 处理文件
            FileProcessor processor = new FileProcessor(parsers);
            for (String path : paths) {
                processor.process(path);
            }
            
            // 打印结果
            processor.printResults();
            
        } catch (ParseException e) {
            System.err.println("参数解析错误: " + e.getMessage());
            printHelp(options);
            System.exit(1);
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
        formatter.printHelp("java -jar code-line-counter.jar [选项] <路径1> [<路径2> ...]", 
                "代码行统计工具 - 统计C/C++和Ruby源文件中的代码行、注释行和空行", 
                options, 
                "\n支持的语言: " + ParserFactory.getSupportedLanguages());
    }
    
    /**
     * 获取用户选择的解析器
     */
    private static List<CodeParser> getSelectedParsers(CommandLine cmd) {
        List<CodeParser> selectedParsers = new ArrayList<>();
        
        if (cmd.hasOption("l")) {
            String langParam = cmd.getOptionValue("l").toLowerCase();
            if ("all".equals(langParam)) {
                selectedParsers.addAll(ParserFactory.getAllParsers());
            } else {
                CodeParser parser = ParserFactory.createParser(langParam);
                if (parser != null) {
                    selectedParsers.add(parser);
                } else {
                    System.err.println("警告: 不支持的语言: " + langParam);
                    System.err.println("使用所有支持的解析器...");
                    selectedParsers.addAll(ParserFactory.getAllParsers());
                }
            }
        } else {
            // 默认使用所有解析器
            selectedParsers.addAll(ParserFactory.getAllParsers());
        }
        
        return selectedParsers;
    }
} 