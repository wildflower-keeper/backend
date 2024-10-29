package org.wildflowergardening.backend.api.wildflowergardening.util;

import java.util.Random;

public class RandomCodeGenerator {
    private static final int LENGTH = 10;

    public static String generate() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int n = random.nextInt(36);
            if (n > 25) sb.append(n - 25);
            else sb.append((char) ('A'+n));
        }

        return sb.toString();
    }
}
