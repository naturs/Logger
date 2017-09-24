package com.github.naturs.logger.strategy.converter;

import com.github.naturs.logger.internal.ObjectConverter;
import com.github.naturs.logger.internal.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class MapConverterStrategy implements ConverterStrategy {

    private static final int INDENT = 4;
    private static final int DEFAULT_PRIORITY = 400;

    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (!(object instanceof Map)) {
            return null;
        }

        Map map = (Map) object;
        StringBuilder builder = new StringBuilder();
        builder.append(map.getClass().getName());
        builder.append(" (size = ").append(map.size()).append(")").append(" [");
        builder.append(DEFAULT_DIVIDER);

        Set set = map.keySet();

        int itemLevel = level + INDENT;
        String itemSpace = Utils.getSpace(itemLevel);

        for (Object key : set) {
            Object value = map.get(key);
            if (value == null) {
                value = "NULL";
            }
            String prefix = key.toString() + " -> ";

            builder.append(itemSpace);
            builder.append(prefix);
            builder.append(ObjectConverter.convert(null, value, itemLevel  + prefix.length()));
            builder.append(DEFAULT_DIVIDER);
        }

        builder.append(Utils.getSpace(level));
        builder.append("]");
        return Utils.concat(message, builder.toString(), DEFAULT_DIVIDER);
    }

    @Override
    public int priority() {
        return DEFAULT_PRIORITY;
    }

}
