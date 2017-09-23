package com.github.naturs.logger.internal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtilTest {
    Object[][] objArray2 = new Object[][]{
            {1, 3, "ABC", 9999},
            {53434, true, "N", new Throwable()},
            {new Object(), "ooo", 0, null},
            {Long.MAX_VALUE, Math.PI, 0.5f, (char) 99},
    };

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

    int[][][] intArray3 = new int[][][]{{{1,2,3},{32},{23234},{3},{34}},{{4},{4},{5},{6},{8}}};

    @Test
    public void test_parseArray() {

        System.out.println(ArrayUtil.parseArray(objArray2, 4));

        System.out.println(ArrayUtil.parseArray(objArray3, 4));

        System.out.println(ArrayUtil.parseArray(intArray3, 4));
    }

    @Test
    public void test_printFormattedArray() {
        List<String> list = new ArrayList<>();
        list.add("[1,  3,  ABC,  9999]");
        list.add("[53434,  true,  N,  Throwable]");
        list.add("[Object,  ooo,  0,  NULL]");
        list.add("[ArrayList,  3,  ABC,  2147483647]");
        list.add("[53434,  true,  N,  Throwable]");
        list.add("[Object,  ooo,  0,  NULL]");
        list.add("[0,  1]");

        ArrayUtil.printFormattedArray(list);
    }

    @Test
    public void test_parseArrayToList() {
        List<String> list = ArrayUtil.parseArrayToList(intArray3, 0, 4);

        for (String s : list) {
            System.out.print(s);
        }

        System.out.println("\n------");

        int index = 0;
        for (String s : list) {
            if (index > 0) {
                if ("\n".equals(s) || "".equals(s)) {
                    System.out.print(s);
                } else {
                    System.out.print("    " + s);
                }
            } else {
                System.out.print(s);
            }
            index ++;
        }
    }
}
