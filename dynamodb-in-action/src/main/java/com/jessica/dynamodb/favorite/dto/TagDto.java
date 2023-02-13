package com.jessica.dynamodb.favorite.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
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
public class TagDto extends AbstractDto {
	private static final String DTO_NAME = "Tag";

	@DynamoDBIgnore
	private String userId;
	@DynamoDBIgnore
	private String tagId;
	@DynamoDBAttribute
	private String tagName;
	@DynamoDBAttribute
	private long createTime;
	@DynamoDBAttribute
	private long lastAccessTime;

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
		return KeyGenerator.createRangeKey(tagId);
	}

	@Override
	public void setSk(String rangeKey) {
		String[] keys = KeyGenerator.parseRangeKey(rangeKey);
		this.tagId = keys[0];
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = DynamoDBConstant.GSI_ONE_NAME)
	public String getGsiOnePk() {
		return this.getPk();
	}

	public void setGsiOnePk(String hashKey) {
		// no need to set any field as userId is set by setPk
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = DynamoDBConstant.GSI_ONE_NAME)
	public String getGsiOneSk() {
		return KeyGenerator.createRangeKey(tagName);
	}

	public void setGsiOneSk(String rangeKey) {
		// no need to set any field as tagName is an attribute, is set by dynamodb
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = DynamoDBConstant.GSI_TWO_NAME)
	public String getGsiTwoPk() {
		return this.getPk();
	}

	public void setGsiTwoPk(String hashKey) {
		// no need to set any field as userId is set by setPk
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = DynamoDBConstant.GSI_TWO_NAME)
	public String getGsiTwoSk() {
		return KeyGenerator.createRangeKey(String.valueOf(createTime), tagId);
	}

	public void setGsiTwoSk(String rangeKey) {
		// no need to set any field as tagId is set by setSk, createTime is an
		// attribute, is set by dynamodb
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = DynamoDBConstant.GSI_THREE_NAME)
	public String getGsiThreePk() {
		return this.getPk();
	}

	public void setGsiThreePk(String hashKey) {
		// no need to set any field as userId is set by setPk
	}

	@DynamoDBIndexRangeKey(globalSecondaryIndexName = DynamoDBConstant.GSI_THREE_NAME)
	public String getGsiThreeSk() {
		return KeyGenerator.createRangeKey(String.valueOf(lastAccessTime), tagId);
	}

	public void setGsiThreeSk(String lsiThreeSk) {
		// no need to set any field as tagId is set by setSk, lastAccessTime is an
		// attribute, is set by dynamodb
	}

}
