package com.jessica.dynamodb.favorite.dto;

public abstract class AbstractDto {
	public abstract String getPk();

	public abstract void setPk(String hashKey);

	public abstract String getSk();

	public abstract void setSk(String rangeKey);

	/**
	 * return composite keys as unique key string
	 * 
	 * @return
	 */
	public String getCompositeKey() {
		return String.join("#", getPk(), getSk());
	}
}
