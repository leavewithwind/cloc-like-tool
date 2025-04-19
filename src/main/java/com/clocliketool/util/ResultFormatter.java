package com.clocliketool.util;

import com.clocliketool.model.LineCountResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 结果格式化工具，负责格式化统计结果的输出
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
        
        // 计算表格宽度
        int languageWidth = 10; // 最小宽度
        for (String language : languageResults.keySet()) {
            languageWidth = Math.max(languageWidth, language.length());
        }
        
        // 表头
        sb.append(String.format("%-5s     %-" + languageWidth + "s     %-7s     %-10s     %-7s     %-7s%n",
                "Files", "Language", "Lines", "Code", "Comments", "Blanks"));
        
        // 分隔线
        sb.append("-----");
        for (int i = 0; i < languageWidth + 50; i++) {
            sb.append("-");
        }
        sb.append("\n");
        
        // 计算总计
        LineCountResult total = new LineCountResult();
        
        // 按字母顺序打印每种语言的结果
        List<String> languages = new ArrayList<>(languageResults.keySet());
        Collections.sort(languages);
        
        for (String language : languages) {
            LineCountResult result = languageResults.get(language);
            sb.append(String.format("%-5d     %-" + languageWidth + "s     %-7d     %-10d     %-7d     %-7d%n",
                    result.getFileCount(),
                    language,
                    result.getTotalLines(),
                    result.getCodeLines(),
                    result.getCommentLines(),
                    result.getBlankLines()));
            
            total.merge(result);
        }
        
        // 分隔线
        sb.append("-----");
        for (int i = 0; i < languageWidth + 50; i++) {
            sb.append("-");
        }
        sb.append("\n");
        
        // 总计
        sb.append(String.format("%-5d     %-" + languageWidth + "s     %-7d     %-10d     %-7d     %-7d%n",
                totalFiles,
                "Total",
                total.getTotalLines(),
                total.getCodeLines(),
                total.getCommentLines(),
                total.getBlankLines()));
        
        return sb.toString();
    }
}
