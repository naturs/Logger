package com.github.naturs.logger.strategy.converter;

import com.github.naturs.logger.internal.ArrayUtil;
import com.github.naturs.logger.internal.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ArrayConverterStrategy implements ConverterStrategy {
    private static final int INDENT = 4;

    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        if (ArrayUtil.isArray(object)) {
            // 获取到格式化后的数组
            List<String> list = ArrayUtil.parseArrayToList(object, 0, INDENT);

            // 拼接空格
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.size(); i ++) {
                if (i > 0 && !Utils.isEmpty(list.get(i).trim())) {
                    builder.append(Utils.getSpace(level));
                }
                builder.append(list.get(i));
            }
            return Utils.concat(message, builder.toString(), DEFAULT_DIVIDER);
        }
        return null;
    }
}
