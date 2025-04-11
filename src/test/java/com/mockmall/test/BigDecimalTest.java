package com.mockmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @program: ShawnMall
 * @description:
 * @author: Shawn Li
 * @create: 2018-09-23 22:40
 **/

public class BigDecimalTest {

    @Test
    public void test1() {
        System.out.println(0.05+0.01);
        System.out.println(1.0-0.43);
        System.out.println(4.016*100);
        System.out.println(126.7/100);

//
//Result:
//        0.060000000000000005
//        0.5700000000000001
//        401.6
//        1.2670000000000001
//
//        Process finished with exit code 0
    }

    @Test
    public void test2() {
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);
        BigDecimal b3 = b1.add(b2);
        System.out.println(b3);

//Result:
//        0.06000000000000000298372437868010820238851010799407958984375
//
//        Process finished with exit code 0
    }

    @Test
    public void test3() {
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        BigDecimal b3 = b1.add(b2);
        System.out.println(b3);

//Result
//        0.06
//
//        Process finished with exit code 0
    }

}
