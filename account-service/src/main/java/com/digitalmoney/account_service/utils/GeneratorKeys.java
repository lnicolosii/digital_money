package com.digitalmoney.account_service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorKeys {

    public static String generateAlias() {
        List<String> words = readFile();
        Random random = new Random();
        int index1 = random.nextInt(words.size());
        int index2 = random.nextInt(words.size());
        while (index2 == index1) {
            index2 = random.nextInt(words.size());
        }
        int index3 = random.nextInt(words.size());
        while (index3 == index1 || index3 == index2) {
            index3 = random.nextInt(words.size());
        }
        String combination = words.get(index1).concat(".")
                .concat(words.get(index2))
                .concat(".")
                .concat(words.get(index3));
        return combination.toLowerCase();
    }

    public static String generateCvu() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static List<String> readFile() {

        GeneratorKeys keysGenerator = new GeneratorKeys();

        List<String> words = new ArrayList<>();

        String path = "/words.txt";

        try (InputStream in = keysGenerator.getClass().getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            while (reader.readLine() != null) {
                words.add(reader.readLine());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return words;
    }
}
