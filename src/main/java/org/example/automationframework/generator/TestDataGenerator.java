package org.example.automationframework.generator;

import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";

    public static String generateLetters(int length) {
        return generateFromSource(ALPHABET, length);
    }

    public static String generateDigits(int length) {
        return generateFromSource(DIGITS, length);
    }

    private static String generateFromSource(String source, int length) {
        if (length <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(source.length());
            sb.append(source.charAt(randomIndex));
        }

        return sb.toString();
    }
}