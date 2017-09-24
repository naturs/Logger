package com.github.naturs.logger.android.strategy.converter;

import android.os.Bundle;

import com.github.naturs.logger.internal.ObjectConverter;
import com.github.naturs.logger.internal.Utils;
import com.github.naturs.logger.strategy.converter.ConverterStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Convert <code>Bundle</code> to formatted string.
 */
public class BundleConverterStrategy implements ConverterStrategy {

    private static final int DEFAULT_PRIORITY = 800;

    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (!(object instanceof Bundle)) {
            return null;
        }
        
        Bundle bundle = (Bundle) object;
        StringBuilder builder = new StringBuilder();
    
        builder.append(bundle.getClass().getName());
        builder.append(" (size = ").append(bundle.size()).append(")").append(" [");
        builder.append(DEFAULT_DIVIDER);
    
        int itemLevel = level + DEFAULT_INDENT;
        String itemSpace = Utils.getSpace(itemLevel);
        
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (value == null) {
                value = "NULL";
            }
            String prefix = key + " -> ";
    
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
