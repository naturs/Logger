package com.github.naturs.logger.strategy.format;

import com.github.naturs.logger.border.LogBorder;
import com.github.naturs.logger.border.ThickBorder;
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
    
    private final int methodCount;
    private final int methodOffset;
    private final boolean showThreadInfo;
    private final boolean optimizeSingleLine;
    private final LogBorder logBorder;
    private final LogStrategy logStrategy;
    private final String tag;
    
    private PrettyFormatStrategy(Builder builder) {
        methodCount = builder.methodCount;
        methodOffset = builder.methodOffset;
        showThreadInfo = builder.showThreadInfo;
        optimizeSingleLine = builder.optimizeSingleLine;
        logBorder = builder.logBorder;
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
        logChunk(logType, tag, logBorder.topBorder());
    }
    
    @SuppressWarnings("StringBufferReplaceableByString")
    private void logHeaderContent(int logType, String tag, int methodCount, Class[] invokeClass) {
        if (showThreadInfo) {
            logChunk(logType, tag, logBorder.leftBorder() + " Thread: " + Thread.currentThread().getName());
            logDivider(logType, tag);
        }
        String level = "";

        StackTraceElement[] elements = Utils.getStackTraceElement(invokeClass, methodCount, methodOffset);

        if (!Utils.isEmpty(elements)) {
            for (StackTraceElement element : elements) {
                StringBuilder builder = new StringBuilder();
                builder.append(logBorder.leftBorder())
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
        logChunk(logType, tag, logBorder.bottomBorder());
    }
    
    private void logDivider(int logType, String tag) {
        if (logBorder.showMiddleBorder()) {
            logChunk(logType, tag, logBorder.middleBorder());
        }
    }
    
    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(LINE_SEPARATOR);
        for (String line : lines) {
            logChunk(logType, tag, logBorder.leftBorder() + " " + line);
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
            if (Utils.isEmpty(this.tag)) {
                return tag;
            }
            return this.tag + "-" + tag;
        }
        return this.tag;
    }
    
    public static class Builder {
        int methodCount = 1;
        int methodOffset = 0;
        boolean showThreadInfo = false;
        boolean optimizeSingleLine = false;
        LogBorder logBorder;
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

        public Builder logBorder(LogBorder logBorder) {
            this.logBorder = logBorder;
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
            if (logBorder == null) {
                logBorder = new ThickBorder(true);
            }
            if (logStrategy == null) {
                logStrategy = new DefaultLogStrategy();
            }
            return new PrettyFormatStrategy(this);
        }
    }
    
}
