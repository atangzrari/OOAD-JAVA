package com.banking.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public final class PasswordHasher {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    private PasswordHasher() {
    }

    public static String hash(String rawPassword) {
        byte[] salt = generateSalt();
        byte[] hash = digest(rawPassword, salt);
        byte[] combined = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hash, 0, combined, salt.length, hash.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static boolean matches(String rawPassword, String storedHash) {
        byte[] combined = Base64.getDecoder().decode(storedHash);
        byte[] salt = new byte[SALT_LENGTH];
        byte[] hash = new byte[combined.length - SALT_LENGTH];
        System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
        System.arraycopy(combined, SALT_LENGTH, hash, 0, hash.length);
        byte[] candidate = digest(rawPassword, salt);
        return constantTimeEquals(hash, candidate);
    }

    private static byte[] digest(String rawPassword, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt);
            return digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Missing hashing algorithm", e);
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}

