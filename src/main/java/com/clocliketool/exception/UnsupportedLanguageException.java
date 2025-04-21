package com.clocliketool.exception;

import com.clocliketool.cli.CommandLineProcessor;
import java.util.List;

/**
 * 不支持的语言异常
 * 当用户指定了程序不支持的语言时抛出
 */
public class UnsupportedLanguageException extends LineCounterException {
    
    private final String language;
    private final List<String> supportedLanguages;
    
    /**
     * 创建一个不支持语言的异常
     * 
     * @param language 用户指定的不支持的语言
     * @param supportedLanguages 程序支持的语言列表
     */
    public UnsupportedLanguageException(String language, List<String> supportedLanguages) {
        super("不支持的语言: " + language + 
              "\n请使用受支持的语言: " + supportedLanguages + 
              "\n\n" + CommandLineProcessor.getLanguageSupportInfo());
        this.language = language;
        this.supportedLanguages = supportedLanguages;
    }
    
    /**
     * 获取用户指定的不支持的语言
     * 
     * @return 不支持的语言
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * 获取程序支持的语言列表
     * 
     * @return 支持的语言列表
     */
    public List<String> getSupportedLanguages() {
        return supportedLanguages;
    }
} 