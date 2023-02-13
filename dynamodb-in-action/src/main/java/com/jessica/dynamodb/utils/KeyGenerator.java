package com.jessica.dynamodb.utils;

public class KeyGenerator {
	public static final String SEPARATOR = "#";

	private KeyGenerator() {
	}

	public static String createHashKey(String itemName, String... hashKeys) {
		StringBuilder hashKeyBuilder = new StringBuilder(itemName);
		for (String hashKey : hashKeys) {
			hashKeyBuilder.append(SEPARATOR).append(hashKey);
		}
		return hashKeyBuilder.toString();
	}

	public static String createRangeKey(String... rangeKeys) {
		StringBuilder rangeKeyBuilder = new StringBuilder();
		for (int i = 0; i < rangeKeys.length; i++) {
			rangeKeyBuilder.append(rangeKeys[i]);
			if (i < rangeKeys.length - 1) {
				rangeKeyBuilder.append(SEPARATOR);
			}
		}
		return rangeKeyBuilder.toString();
	}

	public static String[] parseHashKey(String itemName, String hashKey) {
		String keySubStr = hashKey.substring(itemName.length() + 1);
		return keySubStr.split(SEPARATOR);
	}

	public static String[] parseRangeKey(String rangeKey) {
		return rangeKey.split(SEPARATOR);
	}
}
