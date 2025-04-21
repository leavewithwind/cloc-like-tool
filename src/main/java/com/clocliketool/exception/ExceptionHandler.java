package com.clocliketool.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;

/**
 * 异常处理类，负责统一处理应用程序中的异常
 * 将异常转换为合适的错误代码，并显示错误信息
 */
public class ExceptionHandler {

    /**
     * 处理异常，打印错误信息并返回对应的错误代码
     * 
     * @param e 捕获的异常对象
     * @return 表示错误类型的整数错误代码
     */
    public static int handleException(Exception e) {
        if (e instanceof FileNotFoundException || e instanceof NoSuchFileException) {
            System.err.println("错误: 找不到指定的文件 - " + e.getMessage());
            return StatusCode.FILE_NOT_FOUND.getCode();
        } else if (e instanceof AccessDeniedException) {
            System.err.println("错误: 无权访问指定的文件或目录 - " + e.getMessage());
            return StatusCode.ACCESS_DENIED.getCode();
        } else if (e instanceof IOException) {
            System.err.println("错误: 读取文件时发生I/O错误 - " + e.getMessage());
            return StatusCode.IO_ERROR.getCode();
        } else if (e instanceof IllegalArgumentException) {
            System.err.println("错误: 参数无效 - " + e.getMessage());
            return StatusCode.INVALID_ARGUMENT.getCode();
        } else {
            System.err.println("错误: 发生未预期的异常 - " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("原因: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            return StatusCode.UNEXPECTED_ERROR.getCode();
        }
    }
} 