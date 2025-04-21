package com.clocliketool.exception;

import com.clocliketool.config.AppConfig;
import com.clocliketool.config.ConfigKeys;

/**
 * 无效参数异常
 * 当命令行参数无效时抛出
 */
public class InvalidArgumentException extends LineCounterException {
    
    /**
     * 创建一个无效参数异常
     * 
     * @param message 异常消息
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
    
    /**
     * 创建一个无效参数异常，指定参数名称和原因
     * 
     * @param paramName 参数名称
     * @param reason 无效原因
     */
    public InvalidArgumentException(String paramName, String reason) {
        super(AppConfig.formatMessage(ConfigKeys.ERROR_INVALID_ARGUMENT, paramName, reason));
    }
} 