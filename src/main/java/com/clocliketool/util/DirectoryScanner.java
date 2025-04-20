package com.clocliketool.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 目录扫描器，扫描目录并找出符合条件的文件
 */
public class DirectoryScanner {
    
    /**
     * 扫描目录并找出所有符合扩展名的文件
     * 
     * @param directory 要扫描的目录
     * @param extensions 要匹配的文件扩展名数组
     * @return 匹配的文件列表
     */
    public static List<File> scanDirectory(File directory, String[] extensions) {
        // 检查目录是否为空（先检查null避免空指针异常）、是否存在、是否为有效目录
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return Collections.emptyList(); // 目录无效则返回一个空文件列表
        }
        
        // 创建存储匹配文件的列表
        List<File> matchedFiles = new ArrayList<>();

        // 调用递归方法扫描目录，将匹配的文件添加到列表中
        scanDirectoryRecursively(directory, extensions, matchedFiles);
        
        return matchedFiles; // 返回包含所有匹配文件的列表
    }
    
    /**
     * 递归扫描给定目录，查找具有指定扩展名的文件，并将其添加到匹配文件列表中。
     *
     * @param directory 当前要扫描的目录
     * @param extensions 要匹配的文件扩展名数组
     * @param matchedFiles 存储匹配文件的列表
     */
    private static void scanDirectoryRecursively(File directory, String[] extensions, List<File> matchedFiles) {
        File[] files = directory.listFiles(); // 获取目录中的文件和子目录
        if (files == null) {
            return; // 如果目录为空或无法访问，直接返回
        }
        
        for (File file : files) { // 遍历目录中每个文件和其子目录
            if (file.isDirectory()) { // 如果是子目录，递归扫描
                scanDirectoryRecursively(file, extensions, matchedFiles);
            } else if (file.isFile() && hasMatchingExtension(file, extensions)) { // 如果是文件且扩展名匹配
                matchedFiles.add(file); // 将文件添加到匹配列表中
            }
        }
    }
    
    /**
     * 检查给定的文件是否具有匹配的扩展名
     *
     * @param file 要检查的文件
     * @param extensions 要匹配的文件扩展名数组
     * @return 如果文件有匹配的扩展名，则返回true；否则返回false
     */
    private static boolean hasMatchingExtension(File file, String[] extensions) {
        // 为兼容不同操作系统的文件系统，将文件名转换为小写，进行不区分大小写的比较
        String fileName = file.getName().toLowerCase();
        
        // 遍历所有指定的扩展名
        for (String ext : extensions) {
            // 将扩展名转换为小写，并检查文件名是否以该扩展名结尾
            if (fileName.endsWith("." + ext.toLowerCase())) {
                return true; // 如果匹配，返回true
            }
        }
        
        return false; // 没有匹配的扩展名，返回false
    }
}
