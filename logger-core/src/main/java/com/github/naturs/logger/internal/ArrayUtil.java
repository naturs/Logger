package com.github.naturs.logger.internal;

import java.util.*;

/**
 * From:
 * https://github.com/pengwei1024/LogUtils/blob/master/library/src/main/java/com/apkfuns/logutils/utils/ArrayUtil.java
 */
public final class ArrayUtil {

    private static final String BR = "\n";

    private ArrayUtil() {
    }

    /**
     * 获取数组的纬度
     */
    public static int getArrayDimension(Object object) {
        int dim = 0;
        for (int i = 0; i < object.toString().length(); ++i) {
            if (object.toString().charAt(i) == '[') {
                ++dim;
            } else {
                break;
            }
        }
        return dim;
    }

    /**
     * 是否为数组
     */
    public static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    /**
     * 获取数组类型
     *
     * @param object 如L为int型
     */
    public static char getType(Object object) {
        if (isArray(object)) {
            String str = object.toString();
            return str.substring(str.lastIndexOf("[") + 1, str.lastIndexOf("[") + 2).charAt(0);
        }
        return 0;
    }

    private static void traverseArray(List<String> list, Object array, int level, int indent) {
        if (isArray(array)) {
            if (getArrayDimension(array) == 1) {
                StringBuilder result = new StringBuilder();
                result.append(Utils.getSpace(level));
                switch (getType(array)) {
                    case 'I':
                        result.append(Arrays.toString((int[]) array));
                        break;
                    case 'D':
                        result.append(Arrays.toString((double[]) array));
                        break;
                    case 'Z':
                        result.append(Arrays.toString((boolean[]) array));
                        break;
                    case 'B':
                        result.append(Arrays.toString((byte[]) array));
                        break;
                    case 'S':
                        result.append(Arrays.toString((short[]) array));
                        break;
                    case 'J':
                        result.append(Arrays.toString((long[]) array));
                        break;
                    case 'F':
                        result.append(Arrays.toString((float[]) array));
                        break;
                    case 'C':
                        result.append(Arrays.toString((char[]) array));
                        break;
                    case 'L':
                        Object[] objects = (Object[]) array;
                        // [A, B, C, D...]
                        result.append("[");
                        for (int i = 0; i < objects.length; ++i) {
                            result.append(objToString(objects[i]));
                            if (i != objects.length - 1) {
                                result.append(",  ");
                            }
                        }
                        result.append("]");
                        break;
                    default:
                        result.append(Arrays.toString((Object[]) array));
                        break;
                }
                list.add(result.toString());
            } else {
                list.add(Utils.getSpace(level) + "[");
                list.add(BR);
                for (int i = 0; i < ((Object[]) array).length; i++) {
                    traverseArray(list, ((Object[]) array)[i], level + indent, indent);
                    if (i != ((Object[]) array).length - 1) {
                        list.add(BR);
                    }
                }
                list.add(BR);
                if (level > 0) {
                    list.add(Utils.getSpace(level));
                }
                list.add("]");
            }
        } else {
            list.add("not a array!!");
        }
    }

    private static String objToString(Object obj) {
        if (obj == null) {
            return "NULL";
        }
        if (isPrimitiveClass(obj.getClass()) || obj instanceof CharSequence) {
            return obj.toString();
        }
        return obj.getClass().getSimpleName();
    }

    /**
     *
     * @param clz
     * @return
     */
    public static boolean isPrimitiveClass(Class clz) {
        if (clz.isPrimitive()) {
            return true;
        }
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    static void printFormattedArray(List<String> list) {
        int lastMaxDividerIndex = 0;
        for (;;) {

            int maxDividerIndex = -1;
            Map<Integer, Integer> dividerIndexMap = new HashMap<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                String item = list.get(i);
                int dividerIndex = item.indexOf(',', lastMaxDividerIndex + 1);
                if (dividerIndex == -1) {
                    continue;
                }
                dividerIndexMap.put(i, dividerIndex);
                maxDividerIndex = Math.max(maxDividerIndex, dividerIndex);
            }

            if (dividerIndexMap.size() <= 1) {
                break;
            }

            lastMaxDividerIndex = maxDividerIndex;

            for (Map.Entry<Integer, Integer> entry : dividerIndexMap.entrySet()) {
                int tempIndex = entry.getKey();
                int tempDividerIndex = entry.getValue();

                if (tempDividerIndex < maxDividerIndex) {
                    StringBuilder builder = new StringBuilder(list.get(tempIndex));
                    builder.insert(tempDividerIndex + 1, Utils.getSpace(maxDividerIndex - tempDividerIndex));
                    list.set(tempIndex, builder.toString());
                }
            }
        }
    }

    public static String parseArray(Object array, int indent) {
        return parseArray(array, 0, indent);
    }

    /**
     * 将数组内容转化为字符串
     */
    public static String parseArray(Object array, int level, int indent) {
        StringBuilder result = new StringBuilder();
        List<String> list = parseArrayToList(array, level, indent);

        for (String s : list) {
            result.append(s);
        }
        return result.toString();
    }

    public static List<String> parseArrayToList(Object array, int level, int indent) {
        List<String> list = new ArrayList<>();
        traverseArray(list, array, level, indent);

        printFormattedArray(list);

        return list;
    }
}