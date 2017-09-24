package com.github.naturs.logger.android.strategy.converter;

import com.github.naturs.logger.strategy.converter.DefaultLogConverter;

/**
 * Default converter contains all available ConverterStrategy for android platform.
 */
public class AndroidLogConverter extends DefaultLogConverter {

    @Override
    protected void addDefaultConverter() {
        super.addDefaultConverter();
        add(new BundleConverterStrategy());
        add(new IntentConverterStrategy());
    }
}
