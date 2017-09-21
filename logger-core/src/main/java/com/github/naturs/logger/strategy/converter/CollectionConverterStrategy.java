package com.github.naturs.logger.strategy.converter;

import com.github.naturs.logger.internal.ObjectConverter;
import com.github.naturs.logger.internal.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;

public class CollectionConverterStrategy implements ConverterStrategy {

    private static final int INDENT = 4;

    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (!(object instanceof Collection)) {
            return null;
        }

        Collection collection = (Collection) object;

        StringBuilder builder = new StringBuilder();
        builder.append(collection.getClass().getName());
        builder.append(" (size = ").append(collection.size());
        builder.append(") ").append("[").append(DEFAULT_DIVIDER);
        if (!collection.isEmpty()) {
            Iterator iterator = collection.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Object item = iterator.next();
                String prefix = "[" + (index ++) + "]: ";
                int itemLevel = level + INDENT;
                builder.append(Utils.getSpace(itemLevel));
                builder.append(prefix);
                builder.append(ObjectConverter.convert(null, item, itemLevel  + prefix.length()));
                if (index < collection.size() - 1) {
                    builder.append(",");
                }
                builder.append(DEFAULT_DIVIDER);
            }
        }
        builder.append(Utils.getSpace(level));
        builder.append("]");
        return builder.toString();
    }
}
