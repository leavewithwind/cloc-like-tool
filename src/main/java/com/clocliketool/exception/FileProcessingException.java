package com.clocliketool.exception;

import java.io.File;

/**
 * 文件处理异常
 * 当处理文件时发生I/O错误或其他问题时抛出
 */
public class FileProcessingException extends LineCounterException {
    
    private final String filePath;
    
    /**
     * 创建一个文件处理异常
     * 
     * @param file 处理失败的文件
     * @param cause 原始异常
     */
    public FileProcessingException(File file, Throwable cause) {
        super("处理文件时发生错误: " + file.getAbsolutePath(), cause);
        this.filePath = file.getAbsolutePath();
    }
    
    /**
     * 创建一个文件处理异常
     * 
     * @param filePath 处理失败的文件路径
     * @param cause 原始异常
     */
    public FileProcessingException(String filePath, Throwable cause) {
        super("处理文件时发生错误: " + filePath, cause);
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