package com.revworkforce.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** * Utility class used to hash user passwords.
 * Passwords are never stored in plain text in the database.*/
public class PasswordUtil {

    // Utility class – no object creation required
    private PasswordUtil() {
    }

    /** * Converts a plain password into a SHA-256 hashed value.
     * This method is called before saving or validating passwords. */
    public static String hashPassword(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }
            return result.toString();

        } catch (NoSuchAlgorithmException e) {
            // This should never occur since SHA-256 is supported by default
            throw new RuntimeException("Password hashing failed", e);
        }
    }
}