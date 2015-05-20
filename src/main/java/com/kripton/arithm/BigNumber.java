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
        return a.length > b.length ? multiply(result, 0, a, 0, a.length - 1, b, 0, b.length - 1) : multiply(result, 0, b, 0, b.length - 1, a, 0, a.length - 1);
    }

    public int[] multiply(int[] result, int resultStart, int[] a, int aStart, int aEnd, int[] b, int bStart, int bEnd) {
        long multiplyBuf = 0;
        long addBuf = 0;

        int positionCounter = 0;

        for (int i = bStart; i <= bEnd; i++) {
            for (int j = aStart, shift = i + resultStart; j <= aEnd; j++) {
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
        return result;
    }

    public BigNumber karazuba(BigNumber b) {
        int[] bDigits = b.getDigits();
        int[] result = new int[bDigits.length + digits.length + 1];
        int m = 0;
        if (bDigits.length > digits.length) {
            m = digits.length / 2;
        }

        return null;
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

        System.out.println(bigNumber);
        System.out.println(bigNumber2);

        System.out.println(bigNumber.sub(bigNumber2));
        System.out.println("-----------------------");
//
//        System.out.println(bigNumber);
        BigInteger bigInteger = new BigInteger(new StringBuilder("89000044567000756865002346345000000234002342134021341423002134231000123423").reverse().toString());
        BigInteger bigInteger2 = new BigInteger(new StringBuilder("89000044567000756865002346345000000234002342134021341423002134231000123423").reverse().toString());
        System.out.println(bigInteger);
        System.out.println(bigInteger2);
        System.out.println(bigInteger2.subtract(bigInteger));
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