package com.github.naturs.logger.strategy.converter;

import com.github.naturs.logger.internal.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimaryConverterStrategy implements ConverterStrategy {
    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (object.getClass().isPrimitive()) {
            return Utils.concat(message, String.valueOf(object), DEFAULT_DIVIDER);
        }
        return null;
    }
}
