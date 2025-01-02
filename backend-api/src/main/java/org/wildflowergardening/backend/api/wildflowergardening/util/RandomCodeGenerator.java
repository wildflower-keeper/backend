package org.wildflowergardening.backend.api.wildflowergardening.util;

import java.util.Random;

public class RandomCodeGenerator {
    private static final int LENGTH = 4;

    public static String generate() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int n = random.nextInt(10);
            sb.append(n);
        }

        return sb.toString();
    }
}
