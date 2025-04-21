package com.clocliketool.util;

import com.clocliketool.model.LineCountResult;

import java.util.Map;

/**
 * 输出格式化工具，格式化统计结果的输出
 */
public class ResultFormatter {
    
    /**
     * 生成规范的表格样式的统计结果
     * 
     * @param languageResults 按语言分类的统计结果
     * @return 格式化后的结果字符串
     */
    public static String formatResults(Map<String, LineCountResult> languageResults) {

        // 用StringBuilder构建和拼接字符串，比直接使用+更高效，避免了创建多个中间字符串对象。
        StringBuilder sb = new StringBuilder();
        
        // 获取结果对象
        LineCountResult result = languageResults.values().iterator().next();
        
        // 分隔线
        sb.append("-----------------------------------------------------------\n");
        
        // 表头 - 右对齐 (默认正数右对齐)
        // 使用String.format格式化字符串，指定宽度，确保输出的表格列对齐
        sb.append(String.format("%6s %12s %12s %12s %12s\n",
                "Files", "Lines", "Code", "Comments", "Blanks"));
        
        // 分隔线
        sb.append("-----------------------------------------------------------\n");
        
        // 输出统计结果 - 右对齐
        sb.append(String.format("%6d %12d %12d %12d %12d\n",
                result.getFileCount(),
                result.getTotalLines(),
                result.getCodeLines(),
                result.getCommentLines(),
                result.getBlankLines()));
        
        // 分隔线
        sb.append("-----------------------------------------------------------\n");
        
        return sb.toString();
    }
}
