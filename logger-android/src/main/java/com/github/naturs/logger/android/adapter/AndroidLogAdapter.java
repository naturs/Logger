package com.github.naturs.logger.android.adapter;

import com.github.naturs.logger.adapter.LogAdapter;
import com.github.naturs.logger.android.strategy.log.LogcatLogStrategy;
import com.github.naturs.logger.strategy.format.PrettyFormatStrategy;
import com.github.naturs.logger.strategy.format.FormatStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AndroidLogAdapter implements LogAdapter {
    
    private final FormatStrategy formatStrategy;
    
    public AndroidLogAdapter(@Nullable String globalTag, boolean showThreadInfo) {
        this.formatStrategy =
                PrettyFormatStrategy
                        .newBuilder()
                        .tag(globalTag)
                        .logStrategy(new LogcatLogStrategy())
                        .optimizeSingleLine(true)
                        .showThreadInfo(showThreadInfo)
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