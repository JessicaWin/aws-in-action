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
public class FavoriteDataDto extends AbstractDto {
	private static final String DTO_NAME = "FavoriteData";

	@DynamoDBIgnore
	private String userId;
	@DynamoDBIgnore
	private String dataId;
	@DynamoDBAttribute
	private String creatorId;
	@DynamoDBAttribute
	private String title;
	@DynamoDBAttribute
	private String thumbnailUrl;
	@DynamoDBAttribute
	private String contentUrl;
	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute
	private FavoriteDataType dataType;
	@DynamoDBAttribute
	private String clipTime;

	@DynamoDBHashKey
	@Override
	public String getPk() {
		return KeyGenerator.createHashKey(DTO_NAME, userId);
	}

	@Override
	public void setPk(String hashKey) {
		String[] keys = KeyGenerator.parseHashKey(DTO_NAME, hashKey);
		this.userId = keys[0];
	}

	@DynamoDBRangeKey
	@Override
	public String getSk() {
		return KeyGenerator.createRangeKey(dataId);
	}

	@Override
	public void setSk(String rangeKey) {
		String[] keys = KeyGenerator.parseRangeKey(rangeKey);
		this.dataId = keys[0];
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = DynamoDBConstant.GSI_ONE_NAME)
	public String getGsiOnePk() {
		// dataType will not set when query main table or lsi one
		return KeyGenerator.createHashKey(DTO_NAME, userId, dataType == null ? null : dataType.getValue());
	}

	public void setGsiOnePk(String hashKey) {
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = DynamoDBConstant.GSI_ONE_NAME)
	public String getGsiOneSk() {
		return KeyGenerator.createRangeKey(clipTime, dataId);
	}

	public void setGsiOneSk(String rangeKey) {
	}

	@DynamoDBIndexRangeKey(localSecondaryIndexName = DynamoDBConstant.LSI_ONE_NAME)
	public String getLsiOneSk() {
		return KeyGenerator.createRangeKey(clipTime, dataId);
	}

	public void setLsiOneSk(String rangeKey) {
	}
}
