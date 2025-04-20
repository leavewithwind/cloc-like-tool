package com.clocliketool.exception;

/**
 * 错误代码枚举
 * 定义了应用程序可能返回的错误代码
 */
public enum ErrorCode {
    
    /**
     * 成功
     */
    SUCCESS(0, "操作成功"),
    
    /**
     * 命令行参数错误
     */
    COMMAND_LINE_ERROR(1, "命令行参数错误"),
    
    /**
     * 不支持的语言
     */
    UNSUPPORTED_LANGUAGE(2, "不支持的语言"),
    
    /**
     * 文件或目录不存在
     */
    PATH_NOT_FOUND(3, "文件或目录不存在"),
    
    /**
     * 文件处理错误
     */
    FILE_PROCESSING_ERROR(4, "文件处理错误"),
    
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(99, "未知错误");
    
    private final int code;
    private final String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    /**
     * 获取错误代码
     * 
     * @return 错误代码
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 获取错误消息
     * 
     * @return 错误消息
     */
    public String getMessage() {
        return message;
    }
} 