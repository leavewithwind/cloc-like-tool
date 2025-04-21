package com.clocliketool.exception;

import com.clocliketool.config.AppConfig;
import com.clocliketool.config.ConfigKeys;

import java.io.File;

/**
 * 文件处理异常
 * 当文件读取或处理时出现问题抛出
 */
public class FileProcessingException extends LineCounterException {
    
    private final String filePath;
    
    /**
     * 创建一个文件处理异常
     * 
     * @param file 处理的文件
     * @param cause 原因
     */
    public FileProcessingException(File file, Throwable cause) {
        super(AppConfig.formatMessage(ConfigKeys.ERROR_FILE_PROCESSING, file.getAbsolutePath()), cause);
        this.filePath = file.getAbsolutePath();
    }
    
    /**
     * 创建一个文件处理异常
     * 
     * @param filePath 处理的文件路径
     * @param cause 原因
     */
    public FileProcessingException(String filePath, Throwable cause) {
        super(AppConfig.formatMessage(ConfigKeys.ERROR_FILE_PROCESSING, filePath), cause);
        this.filePath = filePath;
    }
    
    /**
     * 获取处理失败的文件路径
     * 
     * @return 文件路径
     */
    public String getFilePath() {
        return filePath;
    }
} 