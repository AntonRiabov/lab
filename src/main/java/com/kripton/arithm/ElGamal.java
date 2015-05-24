package com.kripton.arithm;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by paladii on 24.05.2015.
 */
public class ElGamal {

    private final Random random = new Random();
    private BigInteger p;

    static enum Numbers{G, Y, X, P, M, A, B};
    private BigInteger x;
    private BigInteger k;


    public Map<Numbers, BigInteger> exposePublicKey(){
        p = BigInteger.probablePrime(1024, random);
        BigInteger g = BigInteger.probablePrime(1024, random);;
        while (g.compareTo(p) != -1 && g.compareTo(BigInteger.ONE) != 1 )g = BigInteger.probablePrime(1024, random);
        x = BigInteger.probablePrime(1024, random);
        while (x.compareTo(p) != -1 && x.compareTo(BigInteger.ONE) != 1 )x = BigInteger.probablePrime(1024, random);

        BigInteger y = g.modPow(x, p);

        Map<Numbers, BigInteger> publicKey = new HashMap<Numbers, BigInteger>();
        publicKey.put(Numbers.G, g);
        publicKey.put(Numbers.Y, y);
        publicKey.put(Numbers.P, p);

        return publicKey;
    }

    public Map<Numbers, BigInteger> encrypt(BigInteger message, Map<Numbers, BigInteger> publicKey){
        k = BigInteger.probablePrime(1024, random);
        while (k.compareTo(publicKey.get(Numbers.P).subtract(BigInteger.ONE)) != -1
                && k.compareTo(BigInteger.ONE) != 1 ) {
            k = BigInteger.probablePrime(1024, random);
        }
        Map<Numbers, BigInteger> encryptedAnswer = new HashMap<Numbers, BigInteger>();

        BigInteger a = publicKey.get(Numbers.G)
                .modPow(k, publicKey.get(Numbers.P));
        encryptedAnswer.put(Numbers.A, a);

        BigInteger b = publicKey.get(Numbers.Y)
                .modPow(k, p)
                .multiply(message)
                .mod(publicKey.get(Numbers.P));
        encryptedAnswer.put(Numbers.B, b);

        return encryptedAnswer;
    }

    public BigInteger decrypt(Map<Numbers, BigInteger> encryptedAnswer){

        return encryptedAnswer.get(Numbers.A)
                .modPow(
                        p.subtract(BigInteger.ONE).subtract(x)
                        , p)
                .multiply(encryptedAnswer.get(Numbers.B))
                .mod(p);
    }

    public static void main(String[] args) {
        ElGamal elGamal = new ElGamal();
        BigInteger message = new BigInteger("12312242");
        System.out.println(elGamal.decrypt(elGamal.encrypt(message, elGamal.exposePublicKey())));
        System.out.println("12312242");

    }
}
