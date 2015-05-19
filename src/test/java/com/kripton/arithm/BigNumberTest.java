package com.kripton.arithm;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class BigNumberTest {

    @Test
    public void testAdd() throws Exception {
        BigNumber a = new BigNumber(new int[]{4});
        BigNumber b = new BigNumber(new int[]{4});

        Assert.assertEquals("16", a.multiply(b).toString());

        a = new BigNumber(new int[]{4});
        b = new BigNumber(new int[]{-4});

        Assert.assertEquals("-16", a.multiply(b).toString());

        BigInteger big1 = new BigInteger(String.valueOf(Integer.MAX_VALUE));
        BigInteger big2 = new BigInteger("2");

        a = new BigNumber(new int[]{Integer.MAX_VALUE});
        b = new BigNumber(new int[]{2});

        Assert.assertEquals(big1.multiply(big2).toString(), a.multiply(b).toString());


        big1 = new BigInteger(String.valueOf(Integer.MAX_VALUE));
        big2 = new BigInteger(String.valueOf(Integer.MAX_VALUE));

        a = new BigNumber(new int[]{Integer.MAX_VALUE});
        b = new BigNumber(new int[]{Integer.MAX_VALUE});

        Assert.assertEquals(big1.multiply(big2).toString(), a.multiply(b).toString());

    }

    @Test
    public void testMultiply() throws Exception {

    }
}