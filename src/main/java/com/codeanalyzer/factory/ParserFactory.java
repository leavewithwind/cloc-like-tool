package com.codeanalyzer.factory;

import com.codeanalyzer.parser.CPPParser;
import com.codeanalyzer.parser.CodeParser;
import com.codeanalyzer.parser.RubyParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析器工厂类，负责创建合适的代码解析器
 * 工厂模式的实现，根据命令行参数选择合适的解析器
 */
public class ParserFactory {
    
    private static final Map<String, Class<? extends CodeParser>> parserMap = new HashMap<>();
    
    static {
        // 注册所有支持的解析器
        registerParser("cpp", CPPParser.class);
        registerParser("c", CPPParser.class);
        registerParser("ruby", RubyParser.class);
    }
    
    /**
     * 根据指定的语言名称创建解析器
     * @param language 语言名称
     * @return 对应的解析器实例，如果不支持则返回null
     */
    public static CodeParser createParser(String language) {
        if (language == null || !parserMap.containsKey(language.toLowerCase())) {
            return null;
        }
        
        try {
            Class<? extends CodeParser> parserClass = parserMap.get(language.toLowerCase());
            return parserClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取所有支持的解析器实例
     * @return 所有支持的解析器实例列表
     */
    public static List<CodeParser> getAllParsers() {
        List<CodeParser> parsers = new ArrayList<>();
        
        try {
            parsers.add(new CPPParser());
            parsers.add(new RubyParser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return parsers;
    }
    
    /**
     * 注册解析器
     * @param language 语言名称
     * @param parserClass 解析器类
     */
    private static void registerParser(String language, Class<? extends CodeParser> parserClass) {
        parserMap.put(language.toLowerCase(), parserClass);
    }
    
    /**
     * 获取所有支持的语言名称
     * @return 支持的语言名称列表
     */
    public static List<String> getSupportedLanguages() {
        return new ArrayList<>(parserMap.keySet());
    }
} 