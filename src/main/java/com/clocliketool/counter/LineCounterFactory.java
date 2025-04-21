package com.clocliketool.counter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码行计数器工厂类，负责创建合适的代码行计数器
 * 以工厂模式，实现根据命令行参数选择特定语言的行计数器
 */
public class LineCounterFactory {
    
    private static final Map<String, Class<? extends LineCounter>> counterMap = new HashMap<>();
    
    static {
        // 注册所有支持的计数器
        // C/C++使用"c++"选项
        registerCounter("c++", CLineCounter.class);
        registerCounter("ruby", RubyLineCounter.class);
    }
    
    /**
     * 根据指定的语言名称创建代码行计数器
     * 
     * @param language 语言名称
     * @return 对应的计数器实例，不支持则返回null
     */
    public static LineCounter createCounter(String language) {
        // 检查语言参数是否为空或不在支持的计数器映射中
        if (language == null || !counterMap.containsKey(language.toLowerCase())) {
            return null; 
        }
        
        try {
            // 从映射中获取对应语言的计数器类
            Class<? extends LineCounter> counterClass = counterMap.get(language.toLowerCase());
            // 使用反射创建计数器实例
            return counterClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace(); // 捕获异常并打印堆栈跟踪信息
            return null; // 实例化失败
        }
    }
    
    /**
     * 注册行计数器
     * 
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
