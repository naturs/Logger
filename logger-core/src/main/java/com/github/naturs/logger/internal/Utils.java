package com.github.naturs.logger.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static com.github.naturs.logger.Logger.*;

/**
 * Provides convenient methods to some common operations
 */
public final class Utils {
    private static final String SUFFIX = ".java";
    
    private Utils() {
        // Hidden constructor.
    }
    
    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * return true if the object array is null or 0-length.
     * @param objects the object to be examined
     * @return true if object is null or zero length
     */
    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     * <p>
     * NOTE: Logic slightly change due to strict policy on CI -
     * "Inner assignments should be avoided"
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        if (a != null && b != null) {
            int length = a.length();
            if (length == b.length()) {
                if (a instanceof String && b instanceof String) {
                    return a.equals(b);
                } else {
                    for (int i = 0; i < length; i++) {
                        if (a.charAt(i) != b.charAt(i)) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
     * in unit tests.
     *
     * @return Stack trace in form of String
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString().trim();
    }

    public static StackTraceElement getStackTraceElement(Class<?> invokeClass) {
        if (invokeClass == null) {
            return null;
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = getStackTraceElementIndex(stackTrace, invokeClass);
        if (index == -1) {
            return null;
        }

        return stackTrace[index];
    }

    public static StackTraceElement[] getStackTraceElement(Class<?> invokeClass, int methodCount, int methodOffset) {
        if (invokeClass == null) {
            return null;
        }

        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final int stackLength = stackTrace.length;

        int index = getStackTraceElementIndex(stackTrace, invokeClass);
        if (index == -1) {
            return null;
        }

        int offset1 = Math.min(stackLength - 1, index + methodOffset);

        // corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + offset1 - 1 > stackLength) {
            methodCount = stackLength - offset1 - 1;
        }
        offset1 = Math.max(0, Math.min(stackLength - 1, offset1));

        int offset2 = Math.max(0, Math.min(stackLength - 1, methodCount + offset1 - 1));

        int startIndex = Math.max(offset1, offset2);
        int endIndex = Math.min(offset1, offset2);

        List<StackTraceElement> targetElements = new ArrayList<>();

        for (int i = startIndex; i >= endIndex; i--) {
            targetElements.add(stackTrace[i]);
        }

        return targetElements.toArray(new StackTraceElement[targetElements.size()]);
    }

    /**
     * get stacktrace info for specified classes.
     */
    public static StackTraceElement[] getStackTraceElement(Class<?>[] invokeClass, int methodCount, int methodOffset) {
        if (isEmpty(invokeClass)) {
            return null;
        }

        for (Class<?> clazz : invokeClass) {
            StackTraceElement[] targetElements = getStackTraceElement(clazz, methodCount, methodOffset);
            if (targetElements != null) {
                return targetElements;
            }
        }

        return null;
    }

    public static StackTraceElement getStackTraceElement(Class<?>[] invokeClass) {
        if (isEmpty(invokeClass)) {
            return null;
        }

        for (Class<?> clazz : invokeClass) {
            StackTraceElement targetElement = getStackTraceElement(clazz);
            if (targetElement != null) {
                return targetElement;
            }
        }

        return null;
    }

    /**
     * get stacktrace index for invoke class.
     * @param stackTrace invoke stack
     * @param invokeClass invoke class
     * @return -1 if not found
     */
    public static int getStackTraceElementIndex(StackTraceElement[] stackTrace, Class<?> invokeClass) {
        if (isEmpty(stackTrace) || invokeClass == null) {
            return -1;
        }

        int targetIndex = -1;

        boolean shouldTrace = false;
        for (int i = 0; i < stackTrace.length; i ++) {
            StackTraceElement element = stackTrace[i];
            boolean isInvokeMethod = element.getClassName().equals(invokeClass.getName());
            if (shouldTrace && !isInvokeMethod) {
                targetIndex = i;
                break;
            }
            shouldTrace = isInvokeMethod;
        }
        return targetIndex;
    }

    public static String getMethodInfo(StackTraceElement element) {
        String fileName;
        if (element.getFileName() != null) {
            fileName = element.getFileName();
            
        } else {
            String className = element.getClassName();
            
            String[] classNameInfo = className.split("\\.");
            if (classNameInfo.length > 0) {
                className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
            }
            
            if (className.contains("$")) {
                className = className.split("\\$")[0] + SUFFIX;
            }
            
            fileName = className;
        }
        return "[(" + fileName + ":" + element.getLineNumber() + ")#" + element.getMethodName() + "]";
    }

    public static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    public static String concat(@Nullable String str1, String str2, String divider) {
        if (str1 == null) {
            return str2;
        }
        return str1 + divider + str2;
    }

    public static String logLevel(int value) {
        switch (value) {
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARN:
                return "WARN";
            case ERROR:
                return "ERROR";
            case ASSERT:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
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
