/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsa;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author glabg
 */
public class DSA {

    private final int primeCenterie = 20;
    //params
    private BigInteger q;
    private BigInteger p;
    private BigInteger g;

    //public key
    private BigInteger y;
    //private key
    private BigInteger x;

    //random per-message value k
    private BigInteger k;
    private Random rand = new Random();

    DSA() {
        generateKey();
    }

    public BigInteger getPublicKeyY() {
        return y;
    }

    public BigInteger getPrivateKeyX() {
        return x;
    }


    public HashMap<String, BigInteger> sign(final byte[] data) {
        HashMap<String, BigInteger> result = new HashMap<String, BigInteger>();
        BigInteger r = generateR();
        BigInteger s = generateS(r, data);
        result.put("r", r);
        result.put("s", s);
        return result;
    }

    private void generateKey() {
        q = new BigInteger(160, primeCenterie, rand);
        p = generateP(q, 512);
        g = generateG(p, q);
        do {
            x = new BigInteger(q.bitCount(), rand);
        } while (x.compareTo(BigInteger.ZERO) != 1 && x.compareTo(q) != -1);
        y = g.modPow(x, p);
    }

    private BigInteger generateP(final BigInteger q, final int l) {
        if (l % 64 != 0) {
            throw new IllegalArgumentException("L value is wrong");
        }
        BigInteger pTemp;
        BigInteger pTemp2;
        do {
            pTemp = new BigInteger(l, primeCenterie, rand);
            pTemp2 = pTemp.subtract(BigInteger.ONE);
            pTemp = pTemp.subtract(pTemp2.remainder(q));
        } while (!pTemp.isProbablePrime(primeCenterie) || pTemp.bitLength() != l);
        return pTemp;
    }

    private BigInteger generateG(final BigInteger p, final BigInteger q) {
        BigInteger aux = p.subtract(BigInteger.ONE);
        BigInteger pow = aux.divide(q);
        BigInteger gTemp;
        do {
            gTemp = new BigInteger(aux.bitLength(), rand);
        } while (gTemp.compareTo(aux) != -1 && gTemp.compareTo(BigInteger.ONE) != 1);
        return gTemp.modPow(pow, p);
    }

    private BigInteger generateK(final BigInteger q) {
        BigInteger tempK;
        do {
            tempK = new BigInteger(q.bitLength(), rand);
        } while (tempK.compareTo(q) != -1 && tempK.compareTo(BigInteger.ZERO) != 1);
        return tempK;
    }

    private BigInteger generateR() {
        k = generateK(q);
        BigInteger r = g.modPow(k, p).mod(q);
        return r;
    }

    private BigInteger generateS(final BigInteger r, final byte[] data) {
        MessageDigest md;
        BigInteger s = BigInteger.ONE;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(data);
            BigInteger hash = new BigInteger(md.digest());
            s = (k.modInverse(q).multiply(hash.add(x.multiply(r)))).mod(q);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public boolean verify(final byte[] data, final HashMap<String, BigInteger> signature) {
        BigInteger r = signature.get("r");
        BigInteger s = signature.get("s");
        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0) {
            return false;
        }
        if (s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(q) >= 0) {
            return false;
        }
        MessageDigest md;
        BigInteger v = BigInteger.ZERO;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(data);
            BigInteger hash = new BigInteger(md.digest());
            BigInteger w = s.modInverse(q);
            BigInteger u1 = hash.multiply(w).mod(q);
            BigInteger u2 = r.multiply(w).mod(q);
            v = ((g.modPow(u1, p).multiply(y.modPow(u2, p))).mod(p)).mod(q);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v.compareTo(r) == 0;
    }
}
