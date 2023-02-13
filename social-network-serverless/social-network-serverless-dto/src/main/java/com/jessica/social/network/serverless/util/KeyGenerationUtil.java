package com.jessica.social.network.serverless.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeyGenerationUtil {
    public static final String SEPARATOR = "#";
    private KeyGenerationUtil() {

    }

    public static String generateHashKey(Class clazz, String ... args) {
        if (clazz == null) {
            throw new RuntimeException("class must provided when generate hash key");
        }

        return join(clazz.getSimpleName(), args);
    }

    public static List<String> parseHashKey(String hashKey) {
        List<String> keys = new ArrayList<>();
        if (hashKey == null) {
            throw new RuntimeException("Hash key can not be null");
        }
        String[] keyAry= hashKey.split(SEPARATOR);
        if (keyAry.length < 2) {
            return keys;
        }
        keys = Stream.of(keyAry).collect(Collectors.toList());
        keys.remove(0);
        return keys;
    }

    public static String generateRangeKey(String firstKeyPart, String ... args) {
        if (firstKeyPart == null) {
            throw new RuntimeException("must provide a valid first string as start of range key");
        }
        return join(firstKeyPart, args);
    }

    public static List<String> parseRangeKey(String rangeKey) {
        List<String> keys = new ArrayList<>();
        if (rangeKey == null) {
            throw new RuntimeException("Range key can not be null");
        }
        return Stream.of(rangeKey.split(SEPARATOR)).collect(Collectors.toList());
    }

    private static String join(String first, String ... leftArgs) {
        StringBuilder result = new StringBuilder(first);
        Iterator iterator = Arrays.asList(leftArgs).iterator();

        while(iterator.hasNext()) {
            result.append(SEPARATOR);
            String value = Objects.toString(iterator.next(), "");
            result.append(value);
        }
        return result.toString();
    }
}
