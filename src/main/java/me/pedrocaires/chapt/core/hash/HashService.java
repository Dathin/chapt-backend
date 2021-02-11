package me.pedrocaires.chapt.core.hash;

import me.pedrocaires.chapt.core.exception.UnexpectedException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class HashService {

    private HashService() {

    }

    public static String getHash(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedException();
        }
    }

    public static String getHash(String string, String salt) {
        return getHash(string + salt);
    }

    public static String getSalt() {
        return UUID.randomUUID().toString();
    }


}
