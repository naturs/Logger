package com.github.naturs.logger.strategy.converter;

import com.github.naturs.logger.internal.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * base converter.
 */
public class StringConverterStrategy implements ConverterStrategy {
    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        return Utils.concat(message, object.toString(), DEFAULT_DIVIDER);
    }
}
