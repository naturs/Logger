package com.github.naturs.logger.strategy.converter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Default converter contains all available ConverterStrategy.
 */
public class DefaultLogConverter implements LogConverter {
    private final Map<Integer, ConverterStrategy> strategies;

    public DefaultLogConverter() {
        this.strategies = new TreeMap<>();
        addDefaultConverter();
    }

    protected void addDefaultConverter() {
        add(new PrimaryConverterStrategy()); // int、boolean
        add(new ArrayConverterStrategy()); // int[]、boolean[]
        add(new CollectionConverterStrategy());
        add(new MapConverterStrategy());
        add(new ThrowableConverterStrategy());
        add(new JsonConverterStrategy());
        add(new XmlConverterStrategy());
        add(new StringConverterStrategy());
    }

    @Override
    public void add(ConverterStrategy strategy) {
        if (strategy != null) {
            int priority = strategy.priority();
            if (strategies.containsKey(priority)) {
                throw new RuntimeException(
                        String.format("You must specify different priority for %s, the %s has the same priority.",
                                strategy.getClass().getSimpleName(), strategies.get(priority).getClass().getSimpleName()));
            }
            strategies.put(priority, strategy);
        }
    }

    @Override
    public ConverterStrategy remove(ConverterStrategy strategy) {
        if (strategy != null) {
            return strategies.remove(strategy.priority());
        }
        return null;
    }

    @Override
    public void clear() {
        strategies.clear();
    }

    @Override
    public String convert(@Nullable String message, @NotNull Object object, int level) {
        Collection<ConverterStrategy> strategies = this.strategies.values();
        for (ConverterStrategy strategy : strategies) {
            String str = strategy.convert(message, object, level);
            if (str != null) {
                return str;
            }
        }
        throw new RuntimeException("you should add StringConverterStrategy at least.");
    }
}
