package com.clocliketool.counter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行计数器工厂类，负责创建合适的代码行计数器
 * 实现工厂模式，根据命令行参数选择特定语言的行计数器
 */
public class LineCounterFactory {
    
    private static final Map<String, Class<? extends LineCounter>> counterMap = new HashMap<>();
    
    static {
        // 注册所有支持的计数器
        registerCounter("cpp", CppLineCounter.class);
        registerCounter("c", CppLineCounter.class);
        registerCounter("ruby", RubyLineCounter.class);
    }
    
    /**
     * 根据指定的语言名称创建行计数器
     * @param language 语言名称
     * @return 对应的行计数器实例，如果不支持则返回null
     */
    public static LineCounter createCounter(String language) {
        if (language == null || !counterMap.containsKey(language.toLowerCase())) {
            return null;
        }
        
        try {
            Class<? extends LineCounter> counterClass = counterMap.get(language.toLowerCase());
            return counterClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 注册行计数器
     * @param language 语言名称
     * @param counterClass 行计数器类
     */
    private static void registerCounter(String language, Class<? extends LineCounter> counterClass) {
        counterMap.put(language.toLowerCase(), counterClass);
    }
    
    /**
     * 获取所有支持的语言名称
     * @return 支持的语言名称列表
     */
    public static List<String> getSupportedLanguages() {
        return new ArrayList<>(counterMap.keySet());
    }
}
