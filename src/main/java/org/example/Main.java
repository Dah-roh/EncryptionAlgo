package org.example;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        StringBuilder rand = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            rand.append(String.valueOf(random.nextInt(10)));
        }
    }
}