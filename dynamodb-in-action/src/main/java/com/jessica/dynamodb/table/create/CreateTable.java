package com.jessica.dynamodb.table.create;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.ContributorInsightsAction;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.PointInTimeRecoverySpecification;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.SSESpecification;
import com.amazonaws.services.dynamodbv2.model.Tag;
import com.amazonaws.services.dynamodbv2.model.TimeToLiveSpecification;
import com.amazonaws.services.dynamodbv2.model.UpdateContinuousBackupsRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateContributorInsightsRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateTimeToLiveRequest;
import com.jessica.dynamodb.constant.DynamoDBConstant;

public class CreateTable {
	static AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();

	static String tableName = "develop.TestTableJava";

	static DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);

	public static void main(String[] args) throws Exception {
		createTable();
		updateTableTTL();
		updateTableBackeUpPolicy();
		updateTableContributorInsights();
	}

	static void createTable() {
		try {
			// attributes
			List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions
					.add(new AttributeDefinition().withAttributeName(DynamoDBConstant.HASH_KEY).withAttributeType("S"));
			attributeDefinitions.add(
					new AttributeDefinition().withAttributeName(DynamoDBConstant.RANGE_KEY).withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName(DynamoDBConstant.GSI_ONE_HASH_KEY)
					.withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName(DynamoDBConstant.GSI_ONE_RANGE_KEY)
					.withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName(DynamoDBConstant.GSI_TWO_HASH_KEY)
					.withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName(DynamoDBConstant.GSI_TWO_RANGE_KEY)
					.withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName(DynamoDBConstant.LSI_ONE_RANGE_KEY)
					.withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName(DynamoDBConstant.LSI_TWO_RANGE_KEY)
					.withAttributeType("S"));

			// table key schema
			List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema
					.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.HASH_KEY).withKeyType(KeyType.HASH));
			keySchema.add(
					new KeySchemaElement().withAttributeName(DynamoDBConstant.RANGE_KEY).withKeyType(KeyType.RANGE));

			// provisioned Throughput
			ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(1L)
					.withWriteCapacityUnits(1L);

			// local secondary index
			List<LocalSecondaryIndex> localSecondaryIndexs = new ArrayList<>();
			List<KeySchemaElement> lsiOneKeySchema = new ArrayList<KeySchemaElement>();
			lsiOneKeySchema
					.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.HASH_KEY).withKeyType(KeyType.HASH));
			lsiOneKeySchema.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.LSI_ONE_RANGE_KEY)
					.withKeyType(KeyType.RANGE));
			List<KeySchemaElement> lsiTwoKeySchema = new ArrayList<KeySchemaElement>();
			lsiTwoKeySchema
					.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.HASH_KEY).withKeyType(KeyType.HASH));
			lsiTwoKeySchema.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.LSI_TWO_RANGE_KEY)
					.withKeyType(KeyType.RANGE));
			localSecondaryIndexs.add(new LocalSecondaryIndex().withIndexName(DynamoDBConstant.LSI_ONE_NAME)
					.withKeySchema(lsiOneKeySchema)
					.withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY)));
			localSecondaryIndexs.add(new LocalSecondaryIndex().withIndexName(DynamoDBConstant.LSI_TWO_NAME)
					.withKeySchema(lsiTwoKeySchema)
					.withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY)));

			// global secondary index
			List<GlobalSecondaryIndex> globalSecondaryIndexs = new ArrayList<>();
			List<KeySchemaElement> gsiOneKeySchema = new ArrayList<KeySchemaElement>();
			gsiOneKeySchema.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.GSI_ONE_HASH_KEY)
					.withKeyType(KeyType.HASH));
			gsiOneKeySchema.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.GSI_ONE_RANGE_KEY)
					.withKeyType(KeyType.RANGE));
			List<KeySchemaElement> gsiTwoKeySchema = new ArrayList<KeySchemaElement>();
			gsiTwoKeySchema.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.GSI_TWO_HASH_KEY)
					.withKeyType(KeyType.HASH));
			gsiTwoKeySchema.add(new KeySchemaElement().withAttributeName(DynamoDBConstant.GSI_TWO_RANGE_KEY)
					.withKeyType(KeyType.RANGE));
			globalSecondaryIndexs.add(new GlobalSecondaryIndex().withIndexName(DynamoDBConstant.GSI_ONE_NAME)
					.withKeySchema(gsiOneKeySchema)
					.withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY))
					.withProvisionedThroughput(provisionedThroughput));
			globalSecondaryIndexs.add(new GlobalSecondaryIndex().withIndexName(DynamoDBConstant.GSI_TWO_NAME)
					.withKeySchema(gsiTwoKeySchema)
					.withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY))
					.withProvisionedThroughput(provisionedThroughput));

			SSESpecification sseSpecification = new SSESpecification().withEnabled(false);

			Tag tag = new Tag().withKey("product").withValue("create-table");

			CreateTableRequest request = new CreateTableRequest().withTableName(tableName)
					.withAttributeDefinitions(attributeDefinitions).withKeySchema(keySchema)
					.withBillingMode(BillingMode.PROVISIONED).withProvisionedThroughput(provisionedThroughput)
					.withLocalSecondaryIndexes(localSecondaryIndexs).withGlobalSecondaryIndexes(globalSecondaryIndexs)
					.withSSESpecification(sseSpecification).withTags(tag);

			System.out.println("Issuing CreateTable request for " + tableName);
			Table table = dynamoDB.createTable(request);
			System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
			table.waitForActive();
			System.out.println("Create " + tableName + " success");
		} catch (Exception e) {
			System.err.println("CreateTable request failed for " + tableName);
			System.err.println(e.getMessage());
		}

	}

	static void updateTableTTL() {
		TimeToLiveSpecification timeToLiveSpecification = new TimeToLiveSpecification().withAttributeName("ttl")
				.withEnabled(true);

		UpdateTimeToLiveRequest updateTimeToLiveRequest = new UpdateTimeToLiveRequest().withTableName(tableName)
				.withTimeToLiveSpecification(timeToLiveSpecification);
		System.out.println("Update time to live for " + tableName);
		try {
			dynamoDBClient.updateTimeToLive(updateTimeToLiveRequest);
			System.out.println("Waiting for " + tableName + " to update TimeToLive");
			dynamoDB.getTable(tableName).waitForActive();
			System.out.println("Update " + tableName + " TimeToLive success");
		} catch (Exception e) {
			System.err.println("Update time to live request failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}

	static void updateTableBackeUpPolicy() {
		PointInTimeRecoverySpecification pointInTimeRecoverySpecification = new PointInTimeRecoverySpecification()
				.withPointInTimeRecoveryEnabled(true);
		UpdateContinuousBackupsRequest updateContinuousBackupsRequest = new UpdateContinuousBackupsRequest()
				.withTableName(tableName).withPointInTimeRecoverySpecification(pointInTimeRecoverySpecification);
		try {
			System.out.println("Update table back up policy for " + tableName);
			dynamoDBClient.updateContinuousBackups(updateContinuousBackupsRequest);
			System.out.println("Waiting for " + tableName + " to update ContinuousBackups");
			dynamoDB.getTable(tableName).waitForActive();
			System.out.println("Update " + tableName + " ContinuousBackups success");
		} catch (Exception e) {
			System.err.println("Update table back up policy failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}

	static void updateTableContributorInsights() {
		UpdateContributorInsightsRequest updateContributorInsightsRequest = new UpdateContributorInsightsRequest()
				.withTableName(tableName).withContributorInsightsAction(ContributorInsightsAction.ENABLE);
		try {
			System.out.println("Update table contributor insights for " + tableName);
			dynamoDBClient.updateContributorInsights(updateContributorInsightsRequest);
			System.out.println("Waiting for " + tableName + " to update ContributorInsights");
			dynamoDB.getTable(tableName).waitForActive();
			System.out.println("Update " + tableName + " ContributorInsights success");
		} catch (Exception e) {
			System.err.println("Update table contributor insights failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}

}
