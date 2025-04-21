package com.clocliketool;

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
        ApplicationRunner runner = new ApplicationRunner(args);
        int exitCode = runner.run();
        
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}
