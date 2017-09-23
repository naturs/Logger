package com.github.naturs.logger;

import com.github.naturs.logger.internal.ArrayUtil;
import com.github.naturs.logger.internal.ObjectConverter;
import com.github.naturs.logger.internal.Utils;
import com.github.naturs.logger.strategy.converter.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilsTest {

    @Before
    public void setup() {
        ObjectConverter.add(new PrimaryConverterStrategy()); // int、boolean
        ObjectConverter.add(new ArrayConverterStrategy()); // int[]、boolean[]
        ObjectConverter.add(new CollectionConverterStrategy());
        ObjectConverter.add(new MapConverterStrategy()); // map
        ObjectConverter.add(new ThrowableConverterStrategy());
        ObjectConverter.add(new JsonConverterStrategy());
        ObjectConverter.add(new XmlConverterStrategy());
        ObjectConverter.add(new StringConverterStrategy());
    }

    @Test
    public void test_getStackTraceString() {
        Throwable t = new Throwable();
        String stackTraceString = Utils.getStackTraceString(t);
        System.out.println(stackTraceString);

        String[] lines = stackTraceString.split("\n");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void test_Array() {
        Object[][] a = new Object[4][4];
        a[0][0] = 1231;
        a[0][1] = 1;
        a[0][2] = "sd";
        a[0][3] = "sd";
        List list = new ArrayList();
        list.add("LIST");
        list.add("LIST_LIST");
        a[1][0] = list;

        int[][][] b = new int[][][]{{{1,2,3},{32},{23234},{3},{34}},{{4},{4},{5},{6},{8}}};

        System.out.println(ArrayUtil.parseArray(a, 4));
        System.out.println(Arrays.deepToString(a));
    }

}
