package com.clocliketool.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 目录扫描器，负责递归遍历目录并找出符合条件的文件
 */
public class DirectoryScanner {
    
    /**
     * 递归扫描目录并找出所有符合扩展名的文件
     * 
     * @param directory 要扫描的目录
     * @param extensions 要匹配的文件扩展名数组
     * @return 匹配的文件列表
     */
    public static List<File> scanDirectory(File directory, String[] extensions) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return Collections.emptyList();
        }
        
        List<File> matchedFiles = new ArrayList<>();
        scanDirectoryRecursively(directory, extensions, matchedFiles);
        return matchedFiles;
    }
    
    /**
     * 递归扫描目录的实现
     */
    private static void scanDirectoryRecursively(File directory, String[] extensions, List<File> matchedFiles) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectoryRecursively(file, extensions, matchedFiles);
            } else if (file.isFile() && hasMatchingExtension(file, extensions)) {
                matchedFiles.add(file);
            }
        }
    }
    
    /**
     * 检查文件是否具有匹配的扩展名
     */
    private static boolean hasMatchingExtension(File file, String[] extensions) {
        if (extensions == null || extensions.length == 0) {
            return true; // 如果未指定扩展名，则匹配所有文件
        }
        
        String fileName = file.getName().toLowerCase();
        for (String ext : extensions) {
            String extLower = ext.toLowerCase();
            if (fileName.endsWith("." + extLower)) {
                return true;
            }
        }
        
        return false;
    }
}
