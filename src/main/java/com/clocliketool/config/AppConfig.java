package com.clocliketool.config;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 应用配置管理类
 * 负责加载和提供应用程序配置
 */
public class AppConfig {
    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());
    private static final Properties properties = new Properties();
    private static boolean isInitialized = false;

    // 私有构造函数，防止实例化
    private AppConfig() {
    }

    /**
     * 初始化配置，从配置文件加载配置项
     */
    public static synchronized void init() {
        if (isInitialized) {
            return;
        }

        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                LOGGER.severe("无法找到配置文件 'config.properties'");
                throw new RuntimeException("无法找到配置文件 'config.properties'");
            }
            properties.load(inputStream);
            isInitialized = true;
            LOGGER.fine("配置文件加载成功");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "加载配置文件时出错", e);
            throw new RuntimeException("加载配置文件时出错", e);
        }
    }

    /**
     * 获取字符串类型的配置值
     *
     * @param key 配置键
     * @return 配置值
     */
    public static String getString(String key) {
        if (!isInitialized) {
            init();
        }
        return properties.getProperty(key);
    }

    /**
     * 获取字符串类型的配置值，如果不存在则返回默认值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值或默认值
     */
    public static String getString(String key, String defaultValue) {
        if (!isInitialized) {
            init();
        }
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 获取字符串数组类型的配置值
     *
     * @param key 配置键
     * @param delimiter 分隔符
     * @return 配置值数组
     */
    public static String[] getStringArray(String key, String delimiter) {
        String value = getString(key);
        if (value != null && !value.isEmpty()) {
            return value.split(delimiter);
        }
        return new String[0];
    }

    /**
     * 使用参数格式化消息字符串
     *
     * @param key 消息键
     * @param params 格式化参数
     * @return 格式化后的消息
     */
    public static String formatMessage(String key, Object... params) {
        String pattern = getString(key);
        if (pattern == null) {
            return key;
        }
        return MessageFormat.format(pattern, params);
    }
} 