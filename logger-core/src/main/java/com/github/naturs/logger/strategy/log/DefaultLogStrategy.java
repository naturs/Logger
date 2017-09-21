package com.github.naturs.logger.strategy.log;

/**
 * default log output by System.out
 */
public class DefaultLogStrategy implements LogStrategy {
    @Override
    public void log(int priority, String tag, String message) {
        System.out.println(tag + ": " + message);
    }
}
