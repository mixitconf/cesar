package org.mixit.cesar.service.authentification;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;

/**
 * Cryptography utils
 */
public class CryptoService {

    @Value("${cesar.crypto.salt}")
    private String salt;


    /**
     * Create a password hash using specific hashing algorithm
     * @return The password hash
     */
    public String passwordHash(String password) {
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            byte[] out = m.digest((password+salt).getBytes());
            return new String(Base64.getEncoder().encode(out));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
