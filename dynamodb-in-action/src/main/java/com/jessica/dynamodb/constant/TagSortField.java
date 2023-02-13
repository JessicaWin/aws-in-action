package com.jessica.dynamodb.constant;

public enum TagSortField {
	TAG_NAME(DynamoDBConstant.GSI_ONE_NAME, DynamoDBConstant.GSI_ONE_RANGE_KEY),
	CREATE_TIME(DynamoDBConstant.GSI_TWO_NAME, DynamoDBConstant.GSI_TWO_RANGE_KEY),
	LAST_ACCESS_TIME(DynamoDBConstant.GSI_THREE_NAME, DynamoDBConstant.GSI_THREE_RANGE_KEY);

	private String gsiName;
	private String gsiSkName;

	private TagSortField(String gsiName, String gsiSkName) {
		this.gsiName = gsiName;
		this.gsiSkName = gsiSkName;
	}

	public String getGsiName() {
		return gsiName;
	}

	public String getGsiSkName() {
		return gsiSkName;
	}
}
