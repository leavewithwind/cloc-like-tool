package com.clocliketool.exception;

import com.clocliketool.cli.CommandLineProcessor;
import java.util.List;

/**
 * 不支持的语言异常
 * 当用户输入程序不支持的语言时抛出
 */
public class UnsupportedLanguageException extends LineCounterException {
    
    /**
     * 创建不支持语言的异常
     * 
     * @param language 用户输入的不支持的语言
     * @param supportedLanguages 程序支持的语言列表
     */
    public UnsupportedLanguageException(String language, List<String> supportedLanguages) {
        super("不支持的语言: " + language + 
              "\n请使用受支持的语言: " + supportedLanguages + 
              "\n\n" + CommandLineProcessor.getLanguageSupportInfo());
    }
    
} 