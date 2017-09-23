package com.github.naturs.logger.adapter;

import com.github.naturs.logger.strategy.format.FormatStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LogAdapter {
    
    boolean isLoggable(int priority, String tag);
    
    void log(int priority, String tag, String message, @Nullable FormatStrategy strategy, @NotNull Class[] invokeClass);
}