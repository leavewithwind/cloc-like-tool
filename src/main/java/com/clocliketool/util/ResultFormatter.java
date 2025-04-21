package com.clocliketool.util;

import com.clocliketool.config.AppConfig;
import com.clocliketool.config.ConfigKeys;
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
     * @param totalFiles 处理的文件总数
     * @return 格式化后的结果字符串
     */
    public static String formatResults(Map<String, LineCountResult> languageResults, int totalFiles) {

        // 用StringBuilder构建和拼接字符串，比直接使用+更高效，避免了创建多个中间字符串对象。
        StringBuilder sb = new StringBuilder();
        
        // 获取结果对象
        LineCountResult result = languageResults.values().iterator().next();
        
        // 从配置中获取分隔线
        String separatorLine = AppConfig.getString(ConfigKeys.OUTPUT_SEPARATOR_LINE);
        sb.append(separatorLine).append("\n");
        
        // 表头 - 从配置中获取格式和列名
        String headerFormat = AppConfig.getString(ConfigKeys.OUTPUT_FORMAT_HEADER);
        String filesColumn = AppConfig.getString(ConfigKeys.OUTPUT_COLUMN_FILES);
        String linesColumn = AppConfig.getString(ConfigKeys.OUTPUT_COLUMN_LINES);
        String codeColumn = AppConfig.getString(ConfigKeys.OUTPUT_COLUMN_CODE);
        String commentsColumn = AppConfig.getString(ConfigKeys.OUTPUT_COLUMN_COMMENTS);
        String blanksColumn = AppConfig.getString(ConfigKeys.OUTPUT_COLUMN_BLANKS);
        
        // 格式化表头
        sb.append(String.format(headerFormat + "\n",
                filesColumn, linesColumn, codeColumn, commentsColumn, blanksColumn));
        
        // 分隔线
        sb.append(separatorLine).append("\n");
        
        // 从配置中获取数据行格式
        String dataFormat = AppConfig.getString(ConfigKeys.OUTPUT_FORMAT_DATA);
        
        // 输出统计结果
        sb.append(String.format(dataFormat + "\n",
                totalFiles,
                result.getTotalLines(),
                result.getCodeLines(),
                result.getCommentLines(),
                result.getBlankLines()));
        
        // 分隔线
        sb.append(separatorLine).append("\n");
        
        return sb.toString();
    }
}
