package com.github.naturs.logger.android.strategy.converter;

import android.content.Intent;
import android.os.Bundle;

import com.github.naturs.logger.internal.ObjectConverter;
import com.github.naturs.logger.internal.Utils;
import com.github.naturs.logger.strategy.converter.ConverterStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * From:
 * https://github.com/pengwei1024/LogUtils/blob/master/library/src/main/java/com/apkfuns/logutils/parser/IntentParse.java
 */
public class IntentConverterStrategy implements ConverterStrategy {

    private static final int DEFAULT_PRIORITY = 900;

    private static Map<Integer, String> flagMap = new HashMap<>();
    
    static {
        Class cla = Intent.class;
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().startsWith("FLAG_")) {
                int value = 0;
                try {
                    Object object = field.get(cla);
                    if (object instanceof Integer || object.getClass().getSimpleName().equals("int")) {
                        value = (int) object;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (flagMap.get(value) == null) {
                    flagMap.put(value, field.getName());
                }
            }
        }
    }
    
    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (!(object instanceof Intent)) {
            return null;
        }
    
        Intent intent = (Intent) object;
        StringBuilder builder = new StringBuilder();
        builder.append(intent.getClass().getName());
        builder.append("[").append(DEFAULT_DIVIDER);
        
        int contentLevel = level + DEFAULT_INDENT;
        String contentSpace = Utils.getSpace(contentLevel);
        
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "Scheme", intent.getScheme()));
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "Action", intent.getAction()));
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "DataString", intent.getDataString()));
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "Type", intent.getType()));
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "Package", intent.getPackage()));
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "ComponentInfo", intent.getComponent()));
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "Flags", getFlags(intent.getFlags())));
        builder.append(contentSpace)
                .append(String.format("%s = %s" + DEFAULT_DIVIDER, "Categories", intent.getCategories()));
    
        Bundle extras = intent.getExtras();
        if (extras == null) {
            builder.append(contentSpace)
                    .append(String.format("%s = %s" + DEFAULT_DIVIDER, "Extras", "NULL"));
        } else {
            String prefix = "Extras -> ";
            builder.append(contentSpace);
            builder.append(prefix);
            builder.append(ObjectConverter.convert(null, extras, contentLevel + prefix.length()));
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
    
    private static String getFlags(int flags) {
        StringBuilder builder = new StringBuilder();
        for (int flagKey : flagMap.keySet()) {
            if ((flagKey & flags) == flagKey) {
                builder.append(flagMap.get(flagKey));
                builder.append(" | ");
            }
        }
        if (Utils.isEmpty(builder.toString())) {
            builder.append(flags);
        } else if (builder.indexOf("|") != -1) {
            builder.delete(builder.length() - 2, builder.length());
        }
        return builder.toString();
    }
}