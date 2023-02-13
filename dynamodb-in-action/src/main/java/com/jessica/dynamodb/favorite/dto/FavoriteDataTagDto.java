package com.jessica.dynamodb.favorite.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.jessica.dynamodb.constant.DynamoDBConstant;
import com.jessica.dynamodb.utils.KeyGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "develop.Favorite")
@EqualsAndHashCode(callSuper = false)
public class FavoriteDataTagDto extends AbstractDto {
	private static final String DTO_NAME = "FavoriteDataTag";

	@DynamoDBIgnore
	private String userId;
	@DynamoDBIgnore
	private String dataId;
	@DynamoDBIgnore
	private String tagId;
	@DynamoDBAttribute
	private String clipTime;
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute
	private FavoriteDataType dataType;
	@DynamoDBAttribute
	private Long ttl;

	@DynamoDBHashKey
	@Override
	public String getPk() {
		return KeyGenerator.createHashKey(DTO_NAME, userId, dataId);
	}

	@Override
	public void setPk(String hashKey) {
		String[] keys = KeyGenerator.parseHashKey(DTO_NAME, hashKey);
		this.userId = keys[0];
		this.dataId = keys[1];
	}

	@DynamoDBRangeKey
	@Override
	public String getSk() {
		return KeyGenerator.createRangeKey(tagId);
	}

	@Override
	public void setSk(String rangeKey) {
		String[] keys = KeyGenerator.parseRangeKey(rangeKey);
		this.tagId = keys[0];
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = DynamoDBConstant.GSI_ONE_NAME)
	public String getGsiOnePk() {
		return KeyGenerator.createHashKey(DTO_NAME, userId, tagId);
	}

	public void setGsiOnePk(String hashKey) {
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = DynamoDBConstant.GSI_ONE_NAME)
	public String getGsiOneSk() {
		return KeyGenerator.createRangeKey(clipTime, dataId);
	}

	public void setGsiOneSk(String rangeKey) {
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = DynamoDBConstant.GSI_TWO_NAME)
	public String getGsiTwoPk() {
		// dataType will not set when query main table or gsi one
		return KeyGenerator.createHashKey(DTO_NAME, userId, tagId, dataType == null ? null : dataType.getValue());
	}

	public void setGsiTwoPk(String hashKey) {
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = DynamoDBConstant.GSI_TWO_NAME)
	public String getGsiTwoSk() {
		return KeyGenerator.createRangeKey(clipTime, dataId);
	}

	public void setGsiTwoSk(String rangeKey) {
	}
}
