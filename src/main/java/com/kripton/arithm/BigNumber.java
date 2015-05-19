package com.kripton.arithm;

import java.text.DecimalFormat;

/**
 * Created by paladii on 12.05.2015.
 */
public class BigNumber {
    public static final String FORMAT = "000000000";
    public static final int MAX_SIZE = Integer.MAX_VALUE;
    private int[] digits;
    public static int MOD = 1000000000;

    public BigNumber(int[] digits) {
        if(digits.length > MAX_SIZE - 1)
        this.digits = digits;
    }

    public int[] add(int[] result, int[] b,  int start, int end){
        int buf = 0;
        for (int i = start; i < end; ++i) {
            buf = digits[ i ] + b[ i ] + buf;
            result[ i ] = buf % MOD;
            buf /= MOD;
        }
        for (int i = end; i <= result.length && buf != 0; ++i) {
            buf = result[ i ] + buf;
            result[ i ] = buf % MOD;
            buf /= MOD;
        }
        return  result;
    }

    public BigNumber add(BigNumber b){

        int smallerSize;
        int size;
        int[] bDigits = b.getDigits();
        int[] result;

        if (bDigits.length > digits.length){
            size = bDigits.length;
            smallerSize = digits.length;
        } else{
            size = digits.length;
            smallerSize = bDigits.length;
        }

        if(size == Integer.MAX_VALUE){
            throw new IllegalArgumentException("Impossible to add, result might be too large");
        }
        result = new int[ size + 1 ];
        return new BigNumber(add(result, bDigits, 0, smallerSize));
    }

    public BigNumber multiply(BigNumber b){
        if((long)digits.length + b.digits.length > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Impossible to add, result will be too large");
        }
        int[] bDigits = b.getDigits();

        int[] result = new int[ bDigits.length + digits.length ];

        return null;
    }
    public int[] multiply(int[] result, int[] a, int[] b) {
        return a.length > b.length ? multiply(result, 0, a, 0, b, 0) : multiply(result, 0, b, 0, a, 0);
    }

    public int[] multiply(int[] result, int resultStart, int[] a, int aStart,int[] b,  int bStart){
        System.out.println( "dd");
        return result;
    }


    private int[] getDigits() {
        return digits;
    }
    public int getLength(){
        return digits.length;
    }

    public static void main(String[] args) {
//    BigNumber bigNumber = new BigNumber(new int[]{123423,2134231,21341423,2342134,234,2346345,756865,44567,89});
//    BigNumber bigNumber2 = new BigNumber(new int[]{123423,2134231,21341423,2342134,234,2346345,756865,44567,89});
//
//        System.out.println(bigNumber);
//        BigInteger bigInteger = new BigInteger("89000044567000756865002346345000000234002342134021341423002134231000123423");
//        System.out.println(bigInteger.add(bigInteger));
//        System.out.println(bigNumber.add(bigNumber2));
//        System.out.println(bigInteger.add(bigInteger).equals(new BigInteger(bigNumber.add(bigNumber2).toString())));
//        int[] gg = new int[];
//        System.out.println((long)Integer.MAX_VALUE + Integer.MAX_VALUE);
        System.out.println(new BigNumber(new int[]{2}).multiply(new int[]{2},new int[]{2},new int[]{2}));
    }

    @Override
    public String toString() {
        DecimalFormat myFormatter = new DecimalFormat(FORMAT);
        StringBuilder stringBuilder = new StringBuilder();
        int start = digits.length - 1;
        while (digits[ start ] == 0){
            --start;
        }
        stringBuilder.append(digits[ start ]);
        for (int i = start - 1; i >= 0 ; --i ) {
            stringBuilder.append(myFormatter.format(digits[ i ]));
        }
        return stringBuilder.toString();
    }
}
