package com.github.naturs.logger.strategy.converter;

import com.github.naturs.logger.internal.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class PrimaryArrayConverterStrategy implements ConverterStrategy {
    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (object instanceof boolean[]) {
            return Utils.concat(message, Arrays.toString((boolean[]) object), DEFAULT_DIVIDER);
        }
        if (object instanceof byte[]) {
            return Utils.concat(message, Arrays.toString((byte[]) object), DEFAULT_DIVIDER);
        }
        if (object instanceof char[]) {
            return Utils.concat(message, Arrays.toString((char[]) object), DEFAULT_DIVIDER);
        }
        if (object instanceof short[]) {
            return Utils.concat(message, Arrays.toString((short[]) object), DEFAULT_DIVIDER);
        }
        if (object instanceof int[]) {
            return Utils.concat(message, Arrays.toString((int[]) object), DEFAULT_DIVIDER);
        }
        if (object instanceof long[]) {
            return Utils.concat(message, Arrays.toString((long[]) object), DEFAULT_DIVIDER);
        }
        if (object instanceof float[]) {
            return Utils.concat(message, Arrays.toString((float[]) object), DEFAULT_DIVIDER);
        }
        if (object instanceof double[]) {
            return Utils.concat(message, Arrays.toString((double[]) object), DEFAULT_DIVIDER);
        }
        return null;
    }
}
