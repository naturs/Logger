package com.github.naturs.logger.adapter;

import com.github.naturs.logger.strategy.format.FormatStrategy;
import com.github.naturs.logger.strategy.format.PrettyFormatStrategy;
import com.github.naturs.logger.strategy.log.DefaultLogStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultLogAdapter implements LogAdapter {
    
    private final FormatStrategy formatStrategy;

    /**
     * @param globalTag if you have not specify a tag, globalTag will be used.
     */
    public DefaultLogAdapter(@Nullable String globalTag) {
        this.formatStrategy =
                PrettyFormatStrategy
                        .newBuilder()
                        .tag(globalTag)
                        .methodCount(1)
                        .showThreadInfo(false)
                        .optimizeSingleLine(true)
                        .logStrategy(new DefaultLogStrategy())
                        .build();
    }

    public DefaultLogAdapter(FormatStrategy formatStrategy) {
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