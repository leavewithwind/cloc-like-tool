package com.clocliketool.util;

import com.clocliketool.model.LineCountResult;

import java.util.Map;

/**
 * 结果格式化工具，负责格式化统计结果的输出
 * 简化输出格式，适应只统计单一语言的需求
 */
public class ResultFormatter {
    
    /**
     * 生成简洁的表格格式的统计结果
     * 
     * @param languageResults 按语言分类的统计结果
     * @param totalFiles 处理的文件总数
     * @return 格式化后的结果字符串
     */
    public static String formatResults(Map<String, LineCountResult> languageResults, int totalFiles) {
        StringBuilder sb = new StringBuilder();
        
        // 获取唯一的结果对象
        String language = languageResults.keySet().iterator().next();
        LineCountResult result = languageResults.values().iterator().next();
        
        // 表头
        sb.append(String.format("%-5s     %-7s     %-7s     %-7s     %-7s%n",
                "Files", "Lines", "Code", "Comments", "Blanks"));
        
        // 分隔线
        sb.append("-----");
        for (int i = 0; i < 40; i++) {
            sb.append("-");
        }
        sb.append("\n");
        
        // 输出统计结果
        sb.append(String.format("%-5d     %-7d     %-7d     %-7d     %-7d%n",
                totalFiles,
                result.getTotalLines(),
                result.getCodeLines(),
                result.getCommentLines(),
                result.getBlankLines()));
        
        // 分隔线
        sb.append("-----");
        for (int i = 0; i < 40; i++) {
            sb.append("-");
        }
        sb.append("\n");
        
        // 添加语言信息
        sb.append("统计语言: ").append(language);
        
        return sb.toString();
    }
}
