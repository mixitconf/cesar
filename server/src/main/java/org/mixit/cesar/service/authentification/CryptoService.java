package org.mixit.cesar.service.authentification;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Cryptography utils
 */
@Service
@Transactional
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

    public String generateRandomPassword(){
        Random random = new Random();
        return random.ints(48,122)
                .filter(i-> (i<57 || i>65) && (i <90 || i>97))
                .mapToObj(i -> (char) i)
                .limit(9)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
