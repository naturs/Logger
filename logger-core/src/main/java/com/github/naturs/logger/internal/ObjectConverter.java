package com.github.naturs.logger.internal;

import com.github.naturs.logger.strategy.converter.ConverterStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * convert object to string.
 */
public class ObjectConverter {

    private final static Map<Integer, ConverterStrategy> strategies = new TreeMap<>();

    public static void add(ConverterStrategy strategy) {
        if (strategy != null) {
            int priority = strategy.priority();
            if (strategies.containsKey(priority)) {
                throw new RuntimeException(
                        String.format("You must specify different priority for %s, the %s has the same priority.",
                                strategy.getClass().getSimpleName(), strategies.get(priority).getClass().getSimpleName()));
            }
            strategies.put(priority, strategy);
        }
    }

    public static void remove(ConverterStrategy strategy) {
        if (strategy != null) {
            strategies.remove(strategy.priority());
        }
    }

    public static void clear() {
        strategies.clear();
    }

    public static String convert(@Nullable String message, @NotNull Object object) {
        return convert(message, object, 0);
    }

    public static String convert(@Nullable String message, @NotNull Object object, int level) {
        Collection<ConverterStrategy> strategies = ObjectConverter.strategies.values();
        for (ConverterStrategy strategy : strategies) {
            String str = strategy.convert(message, object, level);
            if (str != null) {
                return str;
            }
        }
        throw new RuntimeException("you should add StringConverterStrategy at least.");
    }

}
