package com.github.naturs.logger;

import com.github.naturs.logger.adapter.LogAdapter;
import com.github.naturs.logger.strategy.format.FormatStrategy;
import org.jetbrains.annotations.Nullable;

public interface Printer {
    
    @Deprecated
    Printer t(String tag);
    
    /**
     * set tag
     */
    Printer tag(String tag);
    
    Printer strategy(FormatStrategy strategy);

    Printer invokeClass(Class clazz);

    void d(String message, Object... args);
    
    void e(String message, Object... args);
    
    void e(Throwable throwable, String message, Object... args);
    
    void w(String message, Object... args);
    
    void i(String message, Object... args);
    
    void v(String message, Object... args);
    
    void wtf(String message, Object... args);

    void json(String json);

    /**
     * Formats the given json content and print it
     */
    void json(String message, String json);

    void xml(String xml);

    /**
     * Formats the given xml content and print it
     */
    void xml(String message, String xml);
    
    void obj(Object object);

    void obj(String message, Object object);

    void obj(int priority, String message, Object object);

    void log(int priority, String tag, String message, Throwable throwable,
             @Nullable FormatStrategy strategy, @Nullable Class invokeClass);
    
    /**
     * add adapter to process logs
     */
    void addAdapter(LogAdapter adapter);
    
    void clearLogAdapters();

}
