/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsa;

import java.math.BigInteger;
import java.util.HashMap;

/**
 *
 * @author glabg
 */
public class TEST {
    
    public static void main(String[] args) {
        byte[] data = "gglab".getBytes();
        DSA instance = new DSA();
        HashMap<String, BigInteger> signature = instance.sign(data);
        boolean isValid = instance.verify(data, signature);
        System.out.println("public key: " + instance.getPublicKeyY());
        System.out.println("private key: " + instance.getPrivateKeyX());
        System.out.println(new String(data));
        System.out.println("signature r = " + signature.get("r"));
        System.out.println("signature s = " + signature.get("s"));
        System.out.println("signature versified = " + isValid);
    }
    
}
