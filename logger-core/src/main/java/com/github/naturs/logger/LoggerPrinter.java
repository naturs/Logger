package com.github.naturs.logger;

import com.github.naturs.logger.adapter.LogAdapter;
import com.github.naturs.logger.internal.ObjectConverter;
import com.github.naturs.logger.internal.Utils;
import com.github.naturs.logger.strategy.format.FormatStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.naturs.logger.Logger.*;

public final class LoggerPrinter implements Printer {
    
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
    
    private static final int MAX_TAG_LENGTH = 23;
    
    private static final String DEFAULT_LOG_TAG = "Logger";
    
    /**
     * Provides one-time used tag for the log message
     */
    private final ThreadLocal<String> explicitTag = new ThreadLocal<>();
    
    private final ThreadLocal<FormatStrategy> explicitStrategy = new ThreadLocal<>();

    private final ThreadLocal<Class> explicitInvokeClass = new ThreadLocal<>();

    private final LoggerAdapter logAdapters = new LoggerAdapter();
    
    @Override
    public Printer t(String tag) {
        return tag(tag);
    }
    
    @Override
    public Printer tag(String tag) {
        if (tag != null) {
            explicitTag.set(tag);
        }
        return this;
    }
    
    @Override
    public Printer strategy(FormatStrategy strategy) {
        if (strategy != null) {
            explicitStrategy.set(strategy);
        }
        return this;
    }

    @Override
    public Printer invokeClass(Class clazz) {
        if (clazz != null) {
            explicitInvokeClass.set(clazz);
        }
        return this;
    }

    @Override
    public void d(String message, Object... args) {
        log(DEBUG, null, message, args);
    }

    @Override
    public void e(String message, Object... args) {
        e(null, message, args);
    }
    
    @Override
    public void e(Throwable throwable, String message, Object... args) {
        log(ERROR, throwable, message, args);
    }
    
    @Override
    public void w(String message, Object... args) {
        log(WARN, null, message, args);
    }
    
    @Override
    public void i(String message, Object... args) {
        log(INFO, null, message, args);
    }
    
    @Override
    public void v(String message, Object... args) {
        log(VERBOSE, null, message, args);
    }
    
    @Override
    public void wtf(String message, Object... args) {
        log(ASSERT, null, message, args);
    }
    
    @Override
    public void json(String json) {
        json(null, json);
    }

    @Override
    public void json(String message, String json) {
        if (Utils.isEmpty(json)) {
            return;
        }
        obj(message, json);
    }

    @Override
    public void xml(String xml) {
        xml(null, xml);
    }

    @Override
    public void xml(String message, String xml) {
        if (Utils.isEmpty(xml)) {
            return;
        }
        obj(message, xml);
    }

    @Override
    public void obj(Object object) {
        obj(DEBUG, null, object);
    }

    @Override
    public void obj(String message, Object object) {
        obj(DEBUG, message, object);
    }

    @Override
    public void obj(int priority, String message, Object object) {
        log(priority, null, ObjectConverter.convert(message, object));
    }

    @Override
    public synchronized void log(int priority, String tag, String message, Throwable throwable,
                                 @Nullable FormatStrategy strategy, @Nullable Class invokeClass) {
        if (throwable != null && message != null) {
            message += " : " + Utils.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = Utils.getStackTraceString(throwable);
        }
        if (Utils.isEmpty(message)) {
            return;
        }

        logAdapters.log(priority, tag, message, strategy, getCompleteInvokeClass(invokeClass));
    }

    @Override
    public void addAdapter(LogAdapter adapter) {
        logAdapters.add(adapter);
    }
    
    @Override
    public void clearLogAdapters() {
        logAdapters.clear();
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private synchronized void log(int priority, Throwable throwable, String msg, Object... args) {
        Class invokeClass = getInvokeClass();
        String tag = getTag(invokeClass);
        FormatStrategy strategy = getFormatStrategy();
        String message = createMessage(msg, args);
        log(priority, tag, message, throwable, strategy, invokeClass);
    }
    
    /**
     * @return the appropriate tag based on local or global
     */
    private String getTag(Class invokeClass) {
        String tag = explicitTag.get();
        if (tag != null) {
            explicitTag.remove();
            return tag;
        }
        StackTraceElement element = Utils.getStackTraceElement(getCompleteInvokeClass(invokeClass));
        if (element != null) {
            return createStackElementTag(element);
        }
        return DEFAULT_LOG_TAG;
    }
    
    private FormatStrategy getFormatStrategy() {
        FormatStrategy strategy = explicitStrategy.get();
        if (strategy != null) {
            explicitTag.remove();
            return strategy;
        }
        return null;
    }

    private Class getInvokeClass() {
        Class clazz = explicitInvokeClass.get();
        if (clazz != null) {
            explicitInvokeClass.remove();
            return clazz;
        }
        return null;
    }
    
    private String createMessage(String message, Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }
    
    /**
     * Extract the tag which should be used for the message from the {@code element}. By default
     * this will use the class name without any anonymous class suffixes (e.g., {@code Foo$1}
     * becomes {@code Foo}).
     * <p>
     * Note: This will not be called if a {@linkplain #tag(String) manual tag} was specified.
     */
    protected String createStackElementTag(StackTraceElement element) {
        String tag = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1);
        return tag.length() > MAX_TAG_LENGTH ? tag.substring(0, MAX_TAG_LENGTH) : tag;
    }

    private Class[] getCompleteInvokeClass(Class invokeClass) {
        return new Class[]{
                // attention the order.
                invokeClass, Logger.class, LoggerPrinter.class
        };
    }

    /**
     * manage all adapters.
     */
    private static class LoggerAdapter {
        private final List<LogAdapter> logAdapters = new ArrayList<>();

        public void add(LogAdapter adapter) {
            if (adapter != null) {
                logAdapters.add(adapter);
            }
        }

        public void remove(LogAdapter adapter) {
            if (adapter != null) {
                logAdapters.remove(adapter);
            }
        }

        public void clear() {
            logAdapters.clear();
        }

        public void log(int priority, String tag, String message,
                        @Nullable FormatStrategy strategy, @NotNull Class[] invokeClass) {
            for (LogAdapter adapter : logAdapters) {
                if (adapter.isLoggable(priority, tag)) {
                    adapter.log(priority, tag, message, strategy, invokeClass);
                }
            }
        }
    }
}