package com.clocliketool.cli;

import com.clocliketool.counter.LineCounter;
import com.clocliketool.counter.LineCounterFactory;
import com.clocliketool.exception.UnsupportedLanguageException;
import org.apache.commons.cli.*;


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
                System.err.println("错误: 必须使用 -l 选项指定要统计的语言");
                return false;
            }
            
            return true;
        } catch (ParseException e) {
            // 将技术性错误转换为用户友好的消息
            String errorMessage = "参数解析错误";
            
            // 根据不同类型的异常提供具体的错误信息
            if (e instanceof MissingOptionException) {
                errorMessage = "错误: 必须使用 -l 选项指定要统计的语言";
            } else if (e instanceof MissingArgumentException) {
                errorMessage = "错误: -l 选项后必须指定语言类型";
            } else if (e instanceof UnrecognizedOptionException) {
                errorMessage = "错误: 不支持的选项 " + e.getMessage().replaceAll(".*?: ", "");
            }
            
            System.err.println(errorMessage);
            return false;
        }
    }
    
    /**
     * 检查是否需要显示帮助信息
     * 现在只有在完全没有参数时才显示帮助
     */
    public boolean shouldShowHelp() {
        return args.length == 0;
    }
    
    /**
     * 获取要处理的路径
     */
    public String[] getPaths() {
        return cmd.getArgs();
    }
    
    /**
     * 获取用户选择的行计数器
     * 即使是不支持的语言，也会返回一个空列表，而不是null
     * @return 选择的行计数器列表，如果语言不支持则为空列表
     * @throws UnsupportedLanguageException 如果指定了不支持的语言
     */
    public LineCounter getSelectedCounters() {
        String langParam = cmd.getOptionValue("l").toLowerCase();
        LineCounter selectedCounter = LineCounterFactory.createCounter(langParam);
        
        if (selectedCounter != null) {
            return selectedCounter;
        } else {
            // 抛出异常，避免在这里提示导致重复
            throw new UnsupportedLanguageException(langParam, LineCounterFactory.getSupportedLanguages());
        }
    }
    
    /**
     * 创建命令行选项
     */
    private Options createOptions() {
        Options options = new Options();
        
        Option langOpt = Option.builder("l")
                .longOpt("language")
                .hasArg()
                .argName("语言")
                .desc("指定要统计的语言 (支持: c++, ruby)")
                .required(true)
                .build();
        
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
                "\n支持的语言: " + LineCounterFactory.getSupportedLanguages() + 
                "\n" + getLanguageSupportInfo());
    }
    
    /**
     * 获取语言支持信息的字符串
     * 这个方法抽取出来是为了避免在多处重复定义相同的提示信息
     * 
     * @return 语言支持信息的字符串
     */
    public static String getLanguageSupportInfo() {
        return "统计C/C++代码请使用: -l c++" +
               "\n统计Ruby代码请使用: -l ruby";
    }
}