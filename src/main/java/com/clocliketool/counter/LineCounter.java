package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.File;
import java.io.IOException;

/**
 * 代码行计数器抽象基类，定义计数操作的基本行为
 * 
 */
public abstract class LineCounter {

    /**
     * 计算指定文件的代码行、注释行和空行
     * 
     * @param file 要统计的文件
     * @return 包含计数结果的LineCountResult对象
     * @throws IOException 文件读取失败
     */
    public abstract LineCountResult countLines(File file) throws IOException;
    
    /**
     * 获取当前计数器支持的文件扩展名
     * 
     * @return 支持的文件扩展名数组
     */
    public abstract String[] getSupportedExtensions();
    
    /**
     * 获取当前计数器支持的无扩展名特殊文件名
     * 
     * @return 支持的特殊文件名数组，如不支持则返回空数组
     */
    public String[] getSupportedSpecialFiles() {
        return new String[0]; // 默认实现不支持特殊文件
    }
    
    /**
     * 检查文件是否受当前计数器支持
     * 
     * @param file 要检查的文件
     * @return 如果文件受支持则为true，否则为false
     */
    public boolean supportsFile(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }
        
        String fileName = file.getName();
        String lowerFileName = fileName.toLowerCase();
        
        // 检查是否支持该扩展名
        for (String ext : getSupportedExtensions()) {
            if (lowerFileName.endsWith("." + ext.toLowerCase())) {
                return true;
            }
        }
        
        // 检查是否属于特殊无扩展名文件
        for (String specialFile : getSupportedSpecialFiles()) {
            if (fileName.equals(specialFile)) {
                return true;
            }
        }
        
        return false;
    }
}
