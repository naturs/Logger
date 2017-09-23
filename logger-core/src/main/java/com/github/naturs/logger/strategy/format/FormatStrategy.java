package com.github.naturs.logger.strategy.format;

public interface FormatStrategy {

  void log(int priority, String tag, String message, Class[] invokeClass);
}
