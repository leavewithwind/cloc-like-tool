package com.clocliketool.config;

/**
 * 配置键常量类
 * 集中管理所有配置项的键名，避免在代码中使用字符串字面量
 */
public class ConfigKeys {
    // 私有构造函数，防止该类实例化
    private ConfigKeys() {
    }

    // 支持的语言配置
    public static final String LANGUAGE_SUPPORTED_CPP = "language.supported.cpp";
    public static final String LANGUAGE_SUPPORTED_RUBY = "language.supported.ruby";

    // 文件扩展名配置
    public static final String EXTENSION_CPP = "extension.cpp";
    public static final String EXTENSION_RUBY = "extension.ruby";

    // 输出格式配置
    public static final String OUTPUT_SEPARATOR_LINE = "output.separator.line";
    public static final String OUTPUT_FORMAT_HEADER = "output.format.header";
    public static final String OUTPUT_FORMAT_DATA = "output.format.data";
    public static final String OUTPUT_COLUMN_FILES = "output.column.files";
    public static final String OUTPUT_COLUMN_LINES = "output.column.lines";
    public static final String OUTPUT_COLUMN_CODE = "output.column.code";
    public static final String OUTPUT_COLUMN_COMMENTS = "output.column.comments";
    public static final String OUTPUT_COLUMN_BLANKS = "output.column.blanks";

    // 错误消息配置
    public static final String ERROR_INVALID_ARGUMENT = "error.invalid.argument";
    public static final String ERROR_UNSUPPORTED_LANGUAGE = "error.unsupported.language";
    public static final String ERROR_FILE_PROCESSING = "error.file.processing";
    public static final String ERROR_GENERAL = "error.general";
    public static final String ERROR_CAUSE = "error.cause";
    public static final String ERROR_PATH_NONEXISTENT = "error.path.nonexistent";

    // Ruby 语言特殊标记
    public static final String RUBY_MULTILINE_START = "ruby.multiline.start";
    public static final String RUBY_MULTILINE_END = "ruby.multiline.end";
    public static final String RUBY_COMMENT_CHAR = "ruby.comment.char";

    // C/C++ 语言特殊标记
    public static final String CPP_LINE_COMMENT_START = "cpp.line.comment.start";
    public static final String CPP_BLOCK_COMMENT_START = "cpp.block.comment.start";
    public static final String CPP_BLOCK_COMMENT_END = "cpp.block.comment.end";
} 