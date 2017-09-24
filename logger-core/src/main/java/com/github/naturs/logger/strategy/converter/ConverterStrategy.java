package com.github.naturs.logger.strategy.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * convert object to string.
 */
public interface ConverterStrategy {

    /**
     * System.getProperty("line.separator") works terrible in windows.
     */
    String DEFAULT_DIVIDER = /*System.getProperty("line.separator")*/"\n";

    /**
     * Default indent for converter.
     */
    int DEFAULT_INDENT = 4;

    /**
     * Convert object to an expected and formatted string.
     * @param message custom tip.
     * @param object the object to be converted.
     * @param level to help format complex object, such as Map, Collection
     * @return the converted string
     */
    String convert(@Nullable String message, @NotNull Object object, int level);

    /**
     * Priority for current <code>ConverterStrategy</code>, the smaller value, the higher priority.
     * @return priority value
     */
    int priority();
}
