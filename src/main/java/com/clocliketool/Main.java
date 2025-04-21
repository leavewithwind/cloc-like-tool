package com.clocliketool;

import com.clocliketool.config.AppConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 应用程序主类，作为程序入口点
 */
public class Main {
    
    /**
     * 主方法，启动应用程序
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置日志级别为WARNING，避免显示INFO及以下级别的日志
        Logger.getLogger("").setLevel(Level.WARNING);
        
        // 初始化配置
        AppConfig.init();
        
        // 创建并运行应用
        ApplicationRunner runner = new ApplicationRunner(args);
        int exitCode = runner.run();
        
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}
