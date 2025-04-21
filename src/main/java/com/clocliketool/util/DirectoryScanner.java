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
        scanDirectoryRecursively(directory, extensions, null, matchedFiles);
        
        return matchedFiles; // 返回包含所有匹配文件的列表
    }
    
    /**
     * 扫描目录并找出所有符合扩展名或特殊文件名的文件
     * 
     * @param directory 要扫描的目录
     * @param extensions 要匹配的文件扩展名数组
     * @param specialFiles 要匹配的特殊文件名数组，可以为null
     * @return 匹配的文件列表
     */
    public static List<File> scanDirectory(File directory, String[] extensions, String[] specialFiles) {
        // 检查目录是否为空（先检查null避免空指针异常）、是否存在、是否为有效目录
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return Collections.emptyList(); // 目录无效则返回一个空文件列表
        }
        
        // 创建存储匹配文件的列表
        List<File> matchedFiles = new ArrayList<>();

        // 调用递归方法扫描目录，将匹配的文件添加到列表中
        scanDirectoryRecursively(directory, extensions, specialFiles, matchedFiles);
        
        return matchedFiles; // 返回包含所有匹配文件的列表
    }
    
    /**
     * 递归扫描给定目录，查找具有指定扩展名或特殊文件名的文件，并将其添加到匹配文件列表中。
     *
     * @param directory 当前要扫描的目录
     * @param extensions 要匹配的文件扩展名数组
     * @param specialFiles 要匹配的特殊文件名数组，可以为null
     * @param matchedFiles 存储匹配文件的列表
     */
    private static void scanDirectoryRecursively(File directory, String[] extensions, String[] specialFiles, List<File> matchedFiles) {
        File[] files = directory.listFiles(); // 获取目录中的文件和子目录
        if (files == null) {
            return; // 如果目录为空或无法访问，直接返回
        }
        
        for (File file : files) { // 遍历目录中每个文件和其子目录
            if (file.isDirectory()) { // 如果是子目录，递归扫描
                scanDirectoryRecursively(file, extensions, specialFiles, matchedFiles);
            } else if (file.isFile()) { // 如果是文件
                if (hasMatchingExtension(file, extensions) || hasMatchingSpecialFileName(file, specialFiles)) {
                    matchedFiles.add(file); // 将文件添加到匹配列表中
                }
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
        if (extensions == null || extensions.length == 0) {
            return false;
        }
        
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
    
    /**
     * 检查给定的文件是否具有匹配的特殊文件名
     *
     * @param file 要检查的文件
     * @param specialFiles 要匹配的特殊文件名数组
     * @return 如果文件有匹配的特殊文件名，则返回true；否则返回false
     */
    private static boolean hasMatchingSpecialFileName(File file, String[] specialFiles) {
        if (specialFiles == null || specialFiles.length == 0) {
            return false;
        }
        
        String fileName = file.getName();
        
        // 遍历所有指定的特殊文件名
        for (String specialFileName : specialFiles) {
            if (fileName.equals(specialFileName)) {
                return true; // 如果匹配，返回true
            }
        }
        
        return false; // 没有匹配的特殊文件名，返回false
    }
}
