package com.clocliketool.model;

/**
 * 存储代码行计数结果的数据类
 * 作为模型类用于表示统计结果
 */
public class LineCountResult {
    private int codeLines;
    private int commentLines;
    private int blankLines;
    private int fileCount; // 文件数量跟踪

    public LineCountResult() {
        this.codeLines = 0;
        this.commentLines = 0;
        this.blankLines = 0;
        this.fileCount = 0;
    }

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
    
    /**
     * 减少注释行计数
     * 用于当一行被重新归类时（从注释行变为代码行）
     */
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
     * 验证行数统计是否正确
     * @return 如果 totalLines = codeLines + commentLines + blankLines 则返回true
     */
    public boolean isValid() {
        return getTotalLines() == (codeLines + commentLines + blankLines);
    }

    @Override
    public String toString() {
        return String.format("Code lines: %d, Comment lines: %d, Blank lines: %d, Total lines: %d, Files: %d",
                codeLines, commentLines, blankLines, getTotalLines(), fileCount);
    }
}
