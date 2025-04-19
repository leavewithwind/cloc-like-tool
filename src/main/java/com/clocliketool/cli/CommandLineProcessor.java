package com.clocliketool.cli;

import com.clocliketool.counter.LineCounter;
import com.clocliketool.counter.LineCounterFactory;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令行处理类，负责解析命令行参数
 */
public class CommandLineProcessor {
    
    private final String[] args;
    private CommandLine cmd;
    private Options options;
    
    public CommandLineProcessor(String[] args) {
        this.args = args;
        this.options = createOptions();
    }
    
    /**
     * 解析命令行参数
     * 
     * @return 解析成功返回true，解析失败返回false
     */
    public boolean parseArguments() {
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            return true;
        } catch (ParseException e) {
            System.err.println("参数解析错误: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查是否需要显示帮助信息
     */
    public boolean shouldShowHelp() {
        return cmd.hasOption("h") || args.length == 0;
    }
    
    /**
     * 获取要处理的路径
     */
    public String[] getPaths() {
        return cmd.getArgs();
    }
    
    /**
     * 获取用户选择的行计数器
     */
    public List<LineCounter> getSelectedCounters() {
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
    
    /**
     * 创建命令行选项
     */
    private Options createOptions() {
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
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("cloc [选项] <路径1> [<路径2> ...]", 
                "代码行统计工具 - 统计C/C++和Ruby源文件中的代码行、注释行和空行", 
                options, 
                "\n支持的语言: " + LineCounterFactory.getSupportedLanguages());
    }
} 