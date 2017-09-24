package com.github.naturs.logger;

import com.github.naturs.logger.internal.ObjectConverter;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ConverterStrategyTest {

    @Before
    public void setup() {
    }

    @Test
    public void test_MapConverterStrategy() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("keykey1", "value1");
        map1.put("key2", "value2");
        String result = ObjectConverter.convert("简单Map", map1, 0);
        System.out.println(result);

        List list = new LinkedList();
        list.add(9999);
        list.add("JACK");
        List subList = new ArrayList();
        subList.add("OK");
        subList.add("Http");
        list.add(subList);
        Map subMap = new LinkedHashMap();
        subMap.put(9, "io");
        subMap.put("mm", Integer.MAX_VALUE);
        list.add(subMap);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("key_afddfa1", map1);
        map2.put("key_s2", map1);
        map2.put("key3", "ABCD");
        map2.put("key_fsdfadfadfdf4", new Throwable());
        map2.put("key_sdss5", new int[]{1,2,15,5854});
        map2.put("key_233", list);
        result = ObjectConverter.convert("简单Map1", map2, 0);
        System.out.println(result);
    }

    @Test
    public void test_CollectionConverterStrategy() {
        List list = new ArrayList();
        list.add(1223);
        list.add("Logger");
        list.add(true);

        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("key1", map1);
        map2.put("key2", map1);
        map2.put("key3", "ABCD");
        map2.put("key5", new int[]{1,2,15,5854});

        list.add(map1);
        list.add(map2);
        list.add(false);
        list.add(new Throwable());
        list.add(map2);
        String result = ObjectConverter.convert("List", list, 0);

        System.out.println(result);
    }

}
