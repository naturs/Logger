package com.github.naturs.logger.android;

import com.github.naturs.logger.adapter.LogAdapter;
import com.github.naturs.logger.strategy.format.PrettyFormatStrategy;
import com.github.naturs.logger.strategy.format.FormatStrategy;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class AndroidLogAdapter implements LogAdapter {
    
    private final FormatStrategy formatStrategy;
    
    public AndroidLogAdapter() {
        this.formatStrategy =
                PrettyFormatStrategy
                        .newBuilder()
                        .logStrategy(new LogcatLogStrategy())
                        .optimizeSingleLine(true)
                        .showThreadInfo(true)
                        .build();
    }
    
    public AndroidLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }
    
    @Override
    public boolean isLoggable(int priority, String tag) {
        return true;
    }
    
    @Override
    public void log(int priority, String tag, String message, @Nullable FormatStrategy strategy, @NotNull Class[] invokeClass) {
        if (strategy != null) {
            strategy.log(priority, tag, message, invokeClass);
        } else {
            formatStrategy.log(priority, tag, message, invokeClass);
        }
    }
    
}