package ua.khai.gorbatiuk.taskmanager.util.validator.secirity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    private final static String algorithm = "sha-512";
    private final static String salt = "salt";


    public String hash(String input){

        final short SHIFT = 256;

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.update((input + salt).getBytes());
        byte[] hashCode =digest.digest();
        StringBuilder result = new StringBuilder();

        for (byte number : hashCode) {
            result.append(String.format("%02X", (number + SHIFT) % SHIFT));
        }

        return result.toString();
    }
}
