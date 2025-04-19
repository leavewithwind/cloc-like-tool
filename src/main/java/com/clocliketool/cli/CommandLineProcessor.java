package com.clocliketool.cli;

import com.clocliketool.counter.LineCounter;
import com.clocliketool.counter.LineCounterFactory;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令行处理类，负责解析命令行参数
 * 要求通过命令行选项明确指定要统计的语言
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
            
            // 检查语言选项是否存在
            if (!cmd.hasOption("l")) {
                System.err.println("错误: 必须使用 -l 或 --language 选项指定要统计的语言");
                return false;
            }
            
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
        
        String langParam = cmd.getOptionValue("l").toLowerCase();
        LineCounter counter = LineCounterFactory.createCounter(langParam);
        
        if (counter != null) {
            selectedCounters.add(counter);
        } else {
            System.err.println("错误: 不支持的语言: " + langParam);
            System.err.println("支持的语言: " + LineCounterFactory.getSupportedLanguages());
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
                .desc("指定要统计的语言 (支持: c, cpp, ruby)")
                .required(true)
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
        formatter.printHelp("cloc -l <language> <路径1> [<路径2> ...]", 
                "代码行统计工具 - 统计指定语言源文件中的代码行、注释行和空行", 
                options, 
                "\n支持的语言: " + LineCounterFactory.getSupportedLanguages());
    }
} 