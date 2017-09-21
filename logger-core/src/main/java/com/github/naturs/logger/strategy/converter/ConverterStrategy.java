package com.github.naturs.logger.strategy.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * convert object to string.
 */
public interface ConverterStrategy {

    String DEFAULT_DIVIDER = System.getProperty("line.separator");

    String convert(@Nullable String message, @NotNull Object object, int level);

}
