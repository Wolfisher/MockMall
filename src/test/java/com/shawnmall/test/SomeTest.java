package com.shawnmall.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: ShawnMall
 * @description:
 * @author: Shawn Li
 * @create: 2018-10-03 08:56
 **/

public class SomeTest {

    @Test
    public void pint() {

        String s1 = "hello";
        String s2 = "World";
        System.out.println(s1 + "--" +s2);
        this.change(s1, s2);
        System.out.println(s2.length());
        System.out.println(s2);
    }

    public void change(String s1, String s2) {
        s1 = s2;
        System.out.println(s1);
        s2 = s1 + s2;
        System.out.println(s2);
    }
}
