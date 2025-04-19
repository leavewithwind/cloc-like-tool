package com.codeanalyzer.core;

/**
 * 存储代码行计数结果的数据类
 */
public class LineCounter {
    private int codeLines;
    private int commentLines;
    private int blankLines;

    public LineCounter() {
        this.codeLines = 0;
        this.commentLines = 0;
        this.blankLines = 0;
    }

    public LineCounter(int codeLines, int commentLines, int blankLines) {
        this.codeLines = codeLines;
        this.commentLines = commentLines;
        this.blankLines = blankLines;
    }

    public void addCodeLine() {
        codeLines++;
    }

    public void addCommentLine() {
        commentLines++;
    }

    public void addBlankLine() {
        blankLines++;
    }

    public void merge(LineCounter other) {
        this.codeLines += other.codeLines;
        this.commentLines += other.commentLines;
        this.blankLines += other.blankLines;
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

    public int getTotalLines() {
        return codeLines + commentLines + blankLines;
    }

    @Override
    public String toString() {
        return String.format("Code lines: %d, Comment lines: %d, Blank lines: %d, Total lines: %d",
                codeLines, commentLines, blankLines, getTotalLines());
    }
} 