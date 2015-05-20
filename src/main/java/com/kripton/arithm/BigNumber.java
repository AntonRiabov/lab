package com.kripton.arithm;

import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Created by paladii on 12.05.2015.
 */
public class BigNumber {
    public static final String FORMAT = "000000000";
    public static final int MAX_SIZE = Integer.MAX_VALUE;
    public static final String PARSE_EXP = "(?<=\\G.........)";
    private int[] digits;
    public static int MOD = 1000000000;

    public BigNumber(int[] digits) {
        this.digits = digits;
    }

    public int[] add(int[] result, int[] b, int start, int end) {
        int buf = 0;
        for (int i = end; i < digits.length; i++) {
            result[ i ] = digits[ i ];
        }
        for (int i = start; i < end; ++i) {
            buf = digits[i] + b[i] + buf;
            result[i] = buf % MOD;
            buf /= MOD;
        }
        for (int i = end; i <= result.length && buf != 0; ++i) {
            buf = result[i] + buf;
            result[i] = buf % MOD;
            buf /= MOD;
        }
        return result;
    }

    public BigNumber add(BigNumber b) {

        int smallerSize;
        int size;
        int[] bDigits = b.getDigits();
        int[] result;

        if (bDigits.length > digits.length) {
            size = bDigits.length;
            smallerSize = digits.length;
        } else {
            size = digits.length;
            smallerSize = bDigits.length;
        }

        if (size == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Impossible to add, result might be too large");
        }
        result = new int[size + 1];
        return new BigNumber(add(result, bDigits, 0, smallerSize));
    }

    public BigNumber multiply(BigNumber b) {
        if ((long) digits.length + b.digits.length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Impossible to add, result will be too large");
        }
        int[] bDigits = b.getDigits();

        int[] result = new int[bDigits.length + digits.length];

        return new BigNumber(multiply(result, digits, bDigits));
    }

    public int[] multiply(int[] result, int[] a, int[] b) {
        return a.length > b.length ? multiply(result, 0, a, 0, a.length, b, 0, b.length ) : multiply(result, 0, b, 0, b.length, a, 0, a.length);
    }

    public int[] multiply(int[] result, int resultStart, int[] a, int aStart, int aEnd, int[] b, int bStart, int bEnd) {
        long multiplyBuf = 0;
        long addBuf = 0;

        int positionCounter = 0;

        for (int i = bStart; i < bEnd; i++) {
            for (int j = aStart, shift = i + resultStart; j < aEnd; j++) {
                multiplyBuf = (long) a[j] * b[i];
                addBuf = multiplyBuf % MOD;

                addBuf += result[shift + j];
                result[shift + j] = (int) (addBuf % MOD);

                addBuf /= MOD;
                addBuf += multiplyBuf / MOD;

                positionCounter = 1;
                while (addBuf != 0) {
                    addBuf += result[shift + j + positionCounter];
                    result[shift + j + positionCounter] = (int) (addBuf % MOD);
                    addBuf = addBuf / MOD;
                    ++positionCounter;
                }
            }
        }

        return result;
    }

    public BigNumber sub(BigNumber b) {
        int[] bDigits = b.getDigits();
        int[] result = null;
        if (bDigits.length > digits.length) {
            result = new int[bDigits.length];
            sub(result, 0, bDigits, 0, bDigits.length, digits, 0, digits.length);
        } else if (bDigits.length < digits.length) {
            result = new int[digits.length];
            sub(result, 0, digits, 0, digits.length, bDigits, 0, bDigits.length);
        } else {
            for (int i = digits.length - 1; i >= 0; i--) {
                if (digits[i] > bDigits[i]) {
                    result = new int[digits.length];
                    sub(result, 0, digits, 0, digits.length, bDigits, 0, bDigits.length);
                    break;
                } else if (digits[i] < bDigits[i]) {
                    result = new int[bDigits.length];
                    sub(result, 0, bDigits, 0, bDigits.length, digits, 0, digits.length);
                    break;
                }
                if (i == 0 && result == null) result = new int[2];
            }
        }
        return new BigNumber(result);
    }

//    public int[] sub(int[] result, int resultStart, int[] a,  int[] b) {


    //    }
    public int[] sub(int[] result, int resultStart, int[] a, int aStart, int aEnd, int[] b, int bStart, int bEnd) {
        for (int i = 0; i < bEnd - bStart; i++) {
            int aPosition = aStart + i;
            int bPosition = bStart + i;
            if (b[bPosition] > a[aPosition] + result[resultStart + resultStart + i]) {
                for (int k = aStart + i + 1; k < aEnd; k++) {
                    if (a[k] != 0) {
                        for (int l = k; l > i + aStart; l--) {
                            --result[l - aStart + resultStart];
                            result[l - aStart + resultStart - 1] += MOD;
                        }
                        break;
                    }
                }
            }
            result[resultStart + i] += a[aPosition] - b[bPosition];
        }
        for (int i = bEnd - bStart; i < aEnd ; i++) {
            result[ i ] += a[ i ];
        }
        return result;
    }

//    A * B = A0 * B0 + (( A0 + A1 ) * ( B0 + B1 ) — A0 * B0 — A1 * B1 ) * BASEm + A1 * B1 * BASE2 * m
    public BigNumber karazuba(BigNumber b) {
        int[] bDigits = b.getDigits();
        int[] aDigits = digits;
        int[] result = new int[bDigits.length + aDigits.length + 1];
        boolean aBigger = false;
        int m = 0;

        if (bDigits.length > aDigits.length) {
            m = aDigits.length / 2;
            int[] swap = aDigits;
            aDigits = bDigits;
            bDigits = aDigits;

        } else {
            m = bDigits.length / 2;
        }

        //    A * B = A0 * B0 + (( A0 + A1 ) * ( B0 + B1 ) — A0 * B0 — A1 * B1 ) * BASEm + A1 * B1 * BASE2 * m

        BigNumber A0plA1 ;
        BigNumber B0plB1;
        BigNumber A1xB1;
        BigNumber A0xB0;
//        int[] A0xB0 = new int[ m * 2 ];
//        this.multiply(A0xB0, 0, aDigits, 0, m, bDigits, 0, m);
//        add(result, )
        int[] A0arr = new int[m];
        int[] B0arr = new int[m];
        int[] A1arr = new int[aDigits.length - m];
        int[] B1arr = new int[bDigits.length - m];
        for (int i = 0; i < m; i++) {
            A0arr[ i ] = aDigits[ i ];
            B0arr[ i ] = bDigits[ i ];
        }
        BigNumber A0 = new BigNumber(A0arr);
        BigNumber B0 = new BigNumber(B0arr);
        for (int i = 0; i < aDigits.length - m; i++) {
            A1arr[ i ] = aDigits[ i + m];
        }
        for (int i = 0; i < bDigits.length - m; i++) {
            B1arr[ i ] = bDigits[ i + m];
        }
        BigNumber A1 = new BigNumber(A1arr);
        BigNumber B1 = new BigNumber(B1arr);

        A0xB0 = A0.multiply(B0);
        A1xB1 = A1.multiply(B1);
        A0plA1 = A1.add(A0);
        B0plB1 = B1.add(B0);
        BigNumber A0plA1xB0plB1 = A0plA1.multiply(B0plB1);
        int[] m11 = new int[m + A0plA1xB0plB1.digits.length];
        for (int i = m; i < m11.length; i++) {
            m11[ i ] = A0plA1xB0plB1.digits[ i - m];
        }
        BigNumber A0xB0plA1xB1 = A1xB1.add(A0xB0);
        int[] m12 = new int[m + A0xB0plA1xB1.digits.length];
        for (int i = m; i < m12.length; i++) {
            m12[ i ] = A0xB0plA1xB1.digits[ i - m];
        }
        BigNumber M11 = new BigNumber(m11);
        BigNumber M12 = new BigNumber(m12);


//        BigNumber A0plA1xB0plB1_minus_A0xB0plA1xB1 = A0plA1xB0plB1.sub(A0xB0plA1xB1);
//        int[] m1 = new int[m + A0plA1xB0plB1_minus_A0xB0plA1xB1.digits.length];
//        for (int i = m; i < m1.length; i++) {
//            m1[ i ] = A0plA1xB0plB1_minus_A0xB0plA1xB1.digits[ i - m];
//        }
        int[] m2 = new int[m * 2 + A1xB1.digits.length];
        for (int i = m*2; i < m2.length; i++) {
            m2[ i ] = A1xB1.digits[ i - m*2];
        }
        BigNumber M2 = new BigNumber(m2);
//        BigNumber M1 = new BigNumber(m1);
        return M2.add(A0xB0).add(M11).sub(M12);
    }

    private int[] getDigits() {
        return digits;
    }

    public int getLength() {
        return digits.length;
    }

    public static void main(String[] args) {
//        BigNumber bigNumber = new BigNumber(new int[]{123423, 2134231, 21341423, 2342134, 234, 2346345, 756865, 445677, 89});
//        BigNumber bigNumber2 = new BigNumber(new int[]{123423, 2134231, 21341423, 2342134, 234, 2346345, 756865, 445677, 89});
        BigNumber bigNumber = new BigNumber("89000044567000756865002346345000000234002342134021341423002134231000123423");
        BigNumber bigNumber2 = new BigNumber("89000044567000756865002346345000000234002342134021341423002134231000123423");

//        System.out.println(bigNumber);
//        System.out.println(bigNumber2);

        System.out.println(bigNumber.karazuba(bigNumber2));
        System.out.println("-----------------------");
//
//        System.out.println(bigNumber);
        BigInteger bigInteger = new BigInteger(new StringBuilder("89000044567000756865002346345000000234002342134021341423002134231000123423").reverse().toString());
        BigInteger bigInteger2 = new BigInteger(new StringBuilder("89000044567000756865002346345000000234002342134021341423002134231000123423").reverse().toString());
//        System.out.println(bigInteger);
//        System.out.println(bigInteger2);
        System.out.println(bigInteger2.multiply(bigInteger));
//        System.out.println(bigNumber.add(bigNumber2));
//        System.out.println(bigInteger.add(bigInteger).equals(new BigInteger(bigNumber.add(bigNumber2).toString())));
//        int[] gg = new int[];
//        System.out.println((long)Integer.MAX_VALUE + Integer.MAX_VALUE);
        System.out.println(new BigNumber(new int[]{2}).multiply(new int[]{2}, new int[]{2}, new int[]{2}));
    }

    @Override
    public String toString() {
        DecimalFormat myFormatter = new DecimalFormat(FORMAT);
        StringBuilder stringBuilder = new StringBuilder();
        int start = digits.length - 1;
        while (digits[start] == 0 && start > 0) {
            --start;
        }
        stringBuilder.append(digits[start]);
        for (int i = start - 1; i >= 0; --i) {
            stringBuilder.append(myFormatter.format(digits[i]));
        }
        return stringBuilder.toString();
    }

    public BigNumber(String input) {
        String[] arr = input.split("(?<=\\G.........)");
        digits = new int[arr.length];
//        StringBuilder builder = new StringBuilder();
//        herf kbwj
        for (int i = 0; i < arr.length; i++) {
            digits[i] = Integer.parseInt(new StringBuilder(arr[i]).reverse().toString());
        }
    }
}