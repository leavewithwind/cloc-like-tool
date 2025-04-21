package com.clocliketool.exception;

/**
 * 代码行统计工具的基础异常类
 * 作为程序中所有自定义异常的父类
 */
public class LineCounterException extends RuntimeException {
    
    /**
     * 创建一个基础异常
     * 
     * @param message 异常消息
     */
    public LineCounterException(String message) {
        super(message);
    }
    
    /**
     * 创建一个基础异常，携带原始异常信息
     * 
     * @param message 异常消息
     * @param cause 原始异常
     */
    public LineCounterException(String message, Throwable cause) {
        super(message, cause);
    }
} 