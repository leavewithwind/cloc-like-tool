package com.clocliketool.exception;

import com.clocliketool.config.AppConfig;
import com.clocliketool.config.ConfigKeys;

/**
 * 异常处理工具类
 * 提供统一的异常处理功能
 */
public class ExceptionHandler {
    
    /**
     * 根据异常类型返回适合的错误代码
     * 
     * @param e 异常
     * @return 对应的错误代码
     */
    public static ErrorCode getErrorCode(Exception e) {
        if (e instanceof UnsupportedLanguageException) {
            return ErrorCode.UNSUPPORTED_LANGUAGE;
        } else if (e instanceof InvalidArgumentException) {
            return ErrorCode.COMMAND_LINE_ERROR;
        } else if (e instanceof FileProcessingException) {
            return ErrorCode.FILE_PROCESSING_ERROR;
        } else {
            return ErrorCode.UNKNOWN_ERROR;
        }
    }
    
    /**
     * 处理异常并打印错误信息
     * 
     * @param e 异常
     * @return 对应的错误代码
     */
    public static int handleException(Exception e) {
        ErrorCode errorCode = getErrorCode(e);
        
        // 打印错误信息
        System.err.println(AppConfig.formatMessage(ConfigKeys.ERROR_GENERAL, e.getMessage()));
        
        // 对于文件处理异常，显示更多信息
        if (e instanceof FileProcessingException && e.getCause() != null) {
            System.err.println(AppConfig.formatMessage(ConfigKeys.ERROR_CAUSE, e.getCause().getMessage()));
        }
        
        // 对于未知异常，打印堆栈跟踪
        if (errorCode == ErrorCode.UNKNOWN_ERROR) {
            e.printStackTrace();
        }
        
        return errorCode.getCode();
    }
} 