package com.github.naturs.logger.strategy.format;

import com.github.naturs.logger.internal.Utils;
import com.github.naturs.logger.strategy.log.DefaultLogStrategy;
import com.github.naturs.logger.strategy.log.LogStrategy;

public class PrettyFormatStrategy implements FormatStrategy {
    public static final String LINE_SEPARATOR = "\n";
    
    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;
    
    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;
    
    private final int methodCount;
    private final int methodOffset;
    private final boolean showThreadInfo;
    private final boolean optimizeSingleLine;
    private final LogStrategy logStrategy;
    private final String tag;
    
    private PrettyFormatStrategy(Builder builder) {
        methodCount = builder.methodCount;
        methodOffset = builder.methodOffset;
        showThreadInfo = builder.showThreadInfo;
        optimizeSingleLine = builder.optimizeSingleLine;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Override
    public void log(int priority, String onceOnlyTag, String message, Class[] invokeClass) {
        String tag = formatTag(onceOnlyTag);
        
        // get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        
        // optimize: just show one line
        if (optimizeSingleLine
                && !showThreadInfo
                && length <= CHUNK_SIZE
                && methodCount <= 1
                && !message.trim().contains(LINE_SEPARATOR)) {
            logContentSingleLine(priority, tag, message, invokeClass);
            return;
        }
        
        logTopBorder(priority, tag);
        logHeaderContent(priority, tag, methodCount, invokeClass);
        
        if (length <= CHUNK_SIZE) {
            if (methodCount > 0) {
                logDivider(priority, tag);
            }
            logContent(priority, tag, message);
            logBottomBorder(priority, tag);
            return;
        }
        if (methodCount > 0) {
            logDivider(priority, tag);
        }
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(priority, tag, new String(bytes, i, count));
        }
        logBottomBorder(priority, tag);
    }
    
    private void logTopBorder(int logType, String tag) {
        logChunk(logType, tag, TOP_BORDER);
    }
    
    @SuppressWarnings("StringBufferReplaceableByString")
    private void logHeaderContent(int logType, String tag, int methodCount, Class[] invokeClass) {
        if (showThreadInfo) {
            logChunk(logType, tag, HORIZONTAL_LINE + " Thread: " + Thread.currentThread().getName());
            logDivider(logType, tag);
        }
        String level = "";

        StackTraceElement[] elements = Utils.getStackTraceElement(invokeClass, methodCount, methodOffset);

        if (!Utils.isEmpty(elements)) {
            for (StackTraceElement element : elements) {
                StringBuilder builder = new StringBuilder();
                builder.append(HORIZONTAL_LINE)
                        .append(' ')
                        .append(level)
                        .append(Utils.getSimpleClassName(element.getClassName()))
                        .append(".")
                        .append(element.getMethodName())
                        .append(" ")
                        .append(" (")
                        .append(element.getFileName())
                        .append(":")
                        .append(element.getLineNumber())
                        .append(")");
                level += "   ";
                logChunk(logType, tag, builder.toString());
            }
        }
    }
    
    private void logBottomBorder(int logType, String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }
    
    private void logDivider(int logType, String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }
    
    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(LINE_SEPARATOR);
        for (String line : lines) {
            logChunk(logType, tag, HORIZONTAL_LINE + " " + line);
        }
    }
    
    private void logContentSingleLine(int logType, String tag, String chunk, Class... invokeClass) {
        StackTraceElement trace = Utils.getStackTraceElement(invokeClass);
        if (trace == null) {
            logChunk(logType, tag, chunk);
        } else {
            String methodInfo = Utils.getMethodInfo(trace);
            logChunk(logType, tag, methodInfo + " " + chunk);
        }
    }
    
    private void logChunk(int priority, String tag, String chunk) {
        logStrategy.log(priority, tag, chunk);
    }
    
    private String formatTag(String tag) {
        if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }
    
    public static class Builder {
        int methodCount = 1;
        int methodOffset = 0;
        boolean showThreadInfo = false;
        boolean optimizeSingleLine = false;
        LogStrategy logStrategy;
        String tag = "PRETTY_LOGGER";
        
        private Builder() {
        }
        
        public Builder methodCount(int val) {
            methodCount = val;
            return this;
        }
        
        public Builder methodOffset(int val) {
            methodOffset = val;
            return this;
        }
        
        public Builder optimizeSingleLine(boolean val) {
            optimizeSingleLine = val;
            return this;
        }
        
        public Builder showThreadInfo(boolean val) {
            showThreadInfo = val;
            return this;
        }
        
        public Builder logStrategy(LogStrategy val) {
            logStrategy = val;
            return this;
        }
        
        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }
        
        public PrettyFormatStrategy build() {
            if (logStrategy == null) {
                logStrategy = new DefaultLogStrategy();
            }
            return new PrettyFormatStrategy(this);
        }
    }
    
}
