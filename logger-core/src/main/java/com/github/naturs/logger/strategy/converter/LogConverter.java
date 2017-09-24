package com.github.naturs.logger.strategy.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handle all {@link ConverterStrategy}
 */
public interface LogConverter {

    void add(ConverterStrategy strategy);

    ConverterStrategy remove(ConverterStrategy strategy);

    void clear();

    String convert(@Nullable String message, @NotNull Object object, int level);

}
