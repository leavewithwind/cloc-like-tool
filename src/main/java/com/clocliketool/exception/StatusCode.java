package com.clocliketool.exception;

/**
 * 状态代码枚举类
 * 定义了应用程序可能返回的状态代码
 */
public enum StatusCode {

    // 成功
    SUCCESS(0, "操作成功"), 

    // 错误
    COMMAND_LINE_ERROR(1, "命令行参数错误"), // 命令行参数错误
    UNSUPPORTED_LANGUAGE(2, "不支持的语言"), // 不支持的语言
    PATH_NOT_FOUND(3, "文件或目录不存在"), // 文件或目录不存在
    FILE_PROCESSING_ERROR(4, "文件处理错误"), // 文件处理错误
    FILE_NOT_FOUND(5, "找不到指定的文件"), // 找不到指定的文件
    ACCESS_DENIED(6, "无权访问指定的文件或目录"), // 无权访问指定的文件或目录
    IO_ERROR(7, "读取文件时发生I/O错误"), // I/O错误
    INVALID_ARGUMENT(8, "参数无效"), // 参数无效
    UNEXPECTED_ERROR(98, "发生未预期的异常"), // 未预期的异常
    UNKNOWN_ERROR(99, "未知错误"); // 未知错误
    
    
    // 状态代码的数值表示
    private final int code;
    
    // 状态代码的描述信息
    private final String message;
    
    /**
     * 初始化状态代码和消息
     * 
     * @param code 状态代码的数值表示
     * @param message 状态代码的描述信息
     */
    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    /**
     * 获取状态代码
     * 
     * @return 状态代码
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 获取状态消息
     * 
     * @return 状态消息
     */
    public String getMessage() {
        return message;
    }
} 