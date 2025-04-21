package com.clocliketool.model;

/**
 * 存储代码行计数结果的数据模型类
 */
public class LineCountResult {
    private int codeLines; // 代码行数计数
    private int commentLines; // 注释行数计数
    private int blankLines; // 空白行数计数
    private int fileCount; // 文件数量跟踪

    public LineCountResult() {
        this.codeLines = 0;
        this.commentLines = 0;
        this.blankLines = 0;
        this.fileCount = 0;
    }

    /**
     * 使用指定的代码行数、注释行数和空白行数初始化一个新的LineCountResult实例，文件数量默认初始化为1。
     */
    public LineCountResult(int codeLines, int commentLines, int blankLines) {
        this.codeLines = codeLines;
        this.commentLines = commentLines;
        this.blankLines = blankLines;
        this.fileCount = 1;
    }

    public void addCodeLine() {
        codeLines++;
    }

    public void addCommentLine() {
        commentLines++;
    }
    
    public void decrementCommentLine() {
        if (commentLines > 0) {
            commentLines--;
        }
    }

    public void addBlankLine() {
        blankLines++;
    }

    public void incrementFileCount() {
        fileCount++;
    }

    public void merge(LineCountResult other) {
        this.codeLines += other.codeLines;
        this.commentLines += other.commentLines;
        this.blankLines += other.blankLines;
        this.fileCount += other.fileCount;
    }

    public int getCodeLines() {
        return codeLines;
    }

    public int getCommentLines() {
        return commentLines;
    }

    public int getBlankLines() {
        return blankLines;
    }
    
    public int getFileCount() {
        return fileCount;
    }

    public int getTotalLines() {
        return codeLines + commentLines + blankLines;
    }

    /**
     * 重写toString方法，返回一个包含代码行数、注释行数、空白行数、总行数和文件数量的字符串。
     * 目前没有使用，用于调试。
     * @return 包含统计结果的字符串
     */
    @Override
    public String toString() {
        return String.format("Code lines: %d, Comment lines: %d, Blank lines: %d, Total lines: %d, Files: %d",
                codeLines, commentLines, blankLines, getTotalLines(), fileCount);
    }
}
