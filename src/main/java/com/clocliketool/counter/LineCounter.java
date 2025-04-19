package com.clocliketool.counter;

import com.clocliketool.model.LineCountResult;

import java.io.File;
import java.io.IOException;

/**
 * 代码行计数器抽象基类
 * 定义了计数操作的基本行为
 */
public abstract class LineCounter {

    /**
     * 计算指定文件的代码行、注释行和空行
     * 
     * @param file 要统计的文件
     * @return 包含计数结果的LineCountResult对象
     * @throws IOException 如果文件读取失败
     */
    public abstract LineCountResult countLines(File file) throws IOException;
    
    /**
     * 获取此计数器支持的文件扩展名
     * 
     * @return 支持的文件扩展名数组
     */
    public abstract String[] getSupportedExtensions();
    
    /**
     * 检查文件是否受此计数器支持
     * 
     * @param file 要检查的文件
     * @return 如果文件受支持则为true，否则为false
     */
    public boolean supportsFile(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }
        
        String fileName = file.getName().toLowerCase();
        for (String ext : getSupportedExtensions()) {
            if (fileName.endsWith("." + ext.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
}
