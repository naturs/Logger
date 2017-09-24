package com.github.naturs.logger.sample;

import com.github.naturs.logger.Logger;
import com.github.naturs.logger.adapter.DefaultLogAdapter;
import com.github.naturs.logger.strategy.converter.DefaultLogConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static {
        Logger.addLogAdapter(new DefaultLogAdapter("Main."));
        Logger.setLogConverter(new DefaultLogConverter());
    }

    public static void main(String[] args) {
        Logger.d("d");
        Logger.tag("a").d("d");
        LogUtils.d("Log with LogUtils.java");
        Logger.obj("Optimize for single line.");

        Logger.tag("CustomTag").obj("I have a custom tag.");

        String json = "{\"name\":\"abc\",\"age\":18,\"other\":{\"other1\":\"otherValue1\",\"other2\":otherValue2}}";
        Logger.i(json);
        Logger.json(json);
        Logger.obj("这是一个格式化显示的Json", json);

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<resources>" +
                "<item name=\"k1\" messageType=\"a\">value1</item>" +
                "<item name=\"k2\" messageType=\"b\">value2</item>" +
                "<integer name=\"k3\">5</integer>" +
                "</resources>";
        Logger.xml(xml);
        Logger.xml("I'm not a xml.");

        int[] intArray = new int[]{1, 90293, 2332, 232, 232};
        Logger.obj("我是一个int数组", intArray);

        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("key1", map1);
        map2.put("key2", map1);
        map2.put("key3", "ABCD");
        map2.put("key4", new Throwable());
        map2.put("key5", new int[]{1,2,15,5854});

        Object[][] objArray2 = new Object[][]{
                {1, 3, "ABC", 9999},
                {53434, true, "N", new Throwable()},
                {new Object(), "ooo", 0, null},
                {Long.MAX_VALUE, Math.PI, 0.5f, (char) 99},
        };

        map2.put("key_objArray2", objArray2);

        Object[][][] objArray3 = new Object[][][] {
                {
                        {1, 3, "ABC", 9999},
                        {53434, true, "N", new Throwable()},
                        {new Object(), "ooo", 0, null}
                },
                {
                        {new ArrayList<>(), 3, "ABC", Integer.MAX_VALUE}
                },
                {
                        {53434, true, "N", new Throwable()},
                        {new Object(), "ooo", 0, null},
                        {0, 1}
                }
        };

        map2.put("key_objArray3", objArray3);

        int[][][] b = new int[][][]{{{1,2,3},{32},{23234},{3},{34}},{{4},{4},{5},{6},{8}}};

        map2.put("key_int[][][]", b);

        Logger.obj("我是一个复杂Map", map2);

        LogUtils.d("Log with LogUtils.java");
    }

}
