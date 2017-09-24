package com.github.naturs.logger.android.strategy.log;

import android.util.Log;
import com.github.naturs.logger.strategy.log.LogStrategy;

public class LogcatLogStrategy implements LogStrategy {

    @Override
    public void log(int priority, String tag, String message) {
        Log.println(priority, tag, message);
    }

}