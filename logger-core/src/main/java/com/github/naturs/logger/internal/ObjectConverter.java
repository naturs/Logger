package com.github.naturs.logger.internal;

import com.github.naturs.logger.strategy.converter.ConverterStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * convert object to string.
 */
public class ObjectConverter {

    private final static List<ConverterStrategy> strategies = new ArrayList<>();

    public static void add(ConverterStrategy strategy) {
        if (strategy != null) {
            strategies.add(strategy);
        }
    }

    public static void remove(ConverterStrategy strategy) {
        if (strategy != null) {
            strategies.remove(strategy);
        }
    }

    public static void clear() {
        strategies.clear();
    }

    public static String convert(@Nullable String message, @NotNull Object object) {
        return convert(message, object, 0);
    }

    public static String convert(@Nullable String message, @NotNull Object object, int level) {
        for (ConverterStrategy strategy : strategies) {
            String str = strategy.convert(message, object, level);
            if (str != null) {
                return str;
            }
        }
        throw new RuntimeException("you should add StringConverterStrategy at least.");
    }

    public static String getSpace(int level) {
        return getSpace(level, " ");
    }

    public static String getSpace(int level, @NotNull String space) {
        if (level <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; i ++) {
            builder.append(space);
        }
        return builder.toString();
    }
}
