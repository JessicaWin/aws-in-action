package com.jessica.dynamodb.favorite.dto;

public enum FavoriteDataType {
	IMAGE("image"), LINK("link"), FILE("file"), POSITION("position");

	private String value;

	private FavoriteDataType(String strValue) {
		this.value = strValue;
	}

	public String getValue() {
		return value;
	}
}
