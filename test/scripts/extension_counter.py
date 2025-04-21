#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import sys
from collections import Counter

# 统计目录及其子目录下所有文件的扩展名，返回对象包含文件扩展名及次数、无扩展名文件路径列表
def count_file_extensions(directory):
    if not os.path.isdir(directory):
        print(f"错误: '{directory}' 不是一个有效的目录")
        sys.exit(1)
        
    extension_counter = Counter()
    no_extension_files = []
    
    # 遍历目录及所有子目录
    for root, dirs, files in os.walk(directory):
        for file in files:
            # 获取文件扩展名（转为小写以避免大小写差异）
            file_path = os.path.join(root, file)
            _, extension = os.path.splitext(file)
            if extension:
                # 去掉扩展名前面的点号，例如 '.txt' -> 'txt'
                extension = extension[1:].lower()
                extension_counter[extension] += 1
            else:
                # 没有扩展名的文件
                extension_counter['no_extension'] += 1
                no_extension_files.append(file_path)
    
    return extension_counter, no_extension_files

def main():
    if len(sys.argv) != 2:
        print("用法: python extension_counter.py <目录路径>")
        sys.exit(1)
        
    directory = sys.argv[1]
    extension_counts, no_extension_files = count_file_extensions(directory)
    
    # 没有找到任何文件时
    if not extension_counts:
        print(f"在 '{directory}' 及其子目录中没有找到任何文件")
        sys.exit(0)
    
    # 按照扩展名出现次数降序排序并打印结果
    print(f"\n在 '{directory}' 及其子目录中找到的文件扩展名统计:")
    print("-" * 50)
    
    # 输出格式
    ext_column_width = 30  # 列宽
    format_str = "{:<" + str(ext_column_width) + "} {}"
    
    print(format_str.format("扩展名", "数量"))
    print("-" * 50)
    
    # 分离无扩展名和有扩展名的统计
    no_ext_count = extension_counts.get('no_extension', 0)
    ext_counts = {ext: count for ext, count in extension_counts.items() if ext != 'no_extension'}
    
    # 先输出有扩展名的文件统计
    for ext, count in sorted(ext_counts.items(), key=lambda x: x[1], reverse=True):
        print(format_str.format(ext, count))
    
    # 最后输出无扩展名的文件统计（如果有的话）
    if no_ext_count > 0:
        print(format_str.format('no_extension', no_ext_count))
    
    total_files = sum(extension_counts.values())
    
    print("-" * 50)
    print(f"总计: {total_files} 个文件，{len(extension_counts)} 种不同的扩展名")
    
    # 输出无扩展名文件的路径
    if no_extension_files:
        print("\n无扩展名文件的路径:")
        print("-" * 50)
        for file_path in no_extension_files:
            print(file_path)
        print("-" * 50)
        print(f"共有 {len(no_extension_files)} 个无扩展名文件")

if __name__ == "__main__":
    main()