package com.github.naturs.logger.internal;

import com.github.naturs.logger.strategy.converter.ConverterStrategy;
import com.github.naturs.logger.strategy.converter.DefaultLogConverter;
import com.github.naturs.logger.strategy.converter.LogConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * convert object to string.
 */
public class ObjectConverter {

    private static LogConverter converter = new DefaultLogConverter();

    public static void converter(LogConverter converter) {
        ObjectConverter.converter = converter;
    }

    public static void add(ConverterStrategy strategy) {
        converter.add(strategy);
    }

    public static ConverterStrategy remove(ConverterStrategy strategy) {
        return converter.remove(strategy);
    }

    public static void clear() {
        converter.clear();
    }

    public static String convert(@Nullable String message, @NotNull Object object, int level) {
        return converter.convert(message, object, level);
    }

}
