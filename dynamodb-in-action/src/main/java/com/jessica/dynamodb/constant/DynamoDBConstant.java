package com.jessica.dynamodb.constant;

public class DynamoDBConstant {
	private DynamoDBConstant() {

	}

	public static final String HASH_KEY = "pk";
	public static final String RANGE_KEY = "sk";
	public static final String GSI_ONE_NAME = "gsiOne";
	public static final String GSI_ONE_HASH_KEY = "gsiOnePk";
	public static final String GSI_ONE_RANGE_KEY = "gsiOneSk";
	public static final String GSI_TWO_NAME = "gsiTwo";
	public static final String GSI_TWO_HASH_KEY = "gsiTwoPk";
	public static final String GSI_TWO_RANGE_KEY = "gsiTwoSk";
	public static final String GSI_THREE_NAME = "gsiThree";
	public static final String GSI_THREE_HASH_KEY = "gsiThreePk";
	public static final String GSI_THREE_RANGE_KEY = "gsiThreeSk";
	public static final String LSI_ONE_NAME = "lsiOne";
	public static final String LSI_ONE_RANGE_KEY = "lsiOneSk";
	public static final String LSI_TWO_NAME = "lsiTwo";
	public static final String LSI_TWO_RANGE_KEY = "lsiTwoSk";
}
