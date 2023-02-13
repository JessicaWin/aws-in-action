package com.jessica.dynamodb.local;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.rules.ExternalResource;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

public class LocalDynamoDBCreationRule extends ExternalResource {

	private DynamoDBProxyServer server;
	private DynamoDBMapper dynamoDBMapper;
	private DynamoDB dynamoDB;;

	public LocalDynamoDBCreationRule() {
		// This one should be copied during test-compile time. If project's basedir does
		// not contains a folder
		// named 'native-libs' please try '$ mvn clean install' from command line first
		System.setProperty("sqlite4java.library.path", "target/native-libs");
	}

	@Override
	protected void before() throws Throwable {

		try {
			final String port = getAvailablePort();
			this.server = ServerRunner.createServerFromCommandLineArgs(new String[] { "-inMemory", "-port", port });
			server.start();
			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
					.withEndpointConfiguration(new EndpointConfiguration("http://localhost:" + port, "ap-southeast-1"))
					.build();
			dynamoDBMapper = new DynamoDBMapper(client);
			dynamoDB = new DynamoDB(client);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void after() {

		if (server == null) {
			return;
		}

		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DynamoDBMapper getDynamoDBMapper() {
		return dynamoDBMapper;
	}

	public void crateTable(Class<?> clazz) {
		CreateTableRequest request = this.getDynamoDBMapper().generateCreateTableRequest(clazz);
		try {
			// provisioned Throughput
			ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(1L)
					.withWriteCapacityUnits(1L);
			request.withBillingMode(BillingMode.PROVISIONED).withProvisionedThroughput(provisionedThroughput);
			Projection projection = new Projection().withProjectionType(ProjectionType.ALL);
			if (request.getGlobalSecondaryIndexes() != null) {
				request.getGlobalSecondaryIndexes().forEach(globalSendaryIndex -> {
					globalSendaryIndex.setProjection(projection);
					globalSendaryIndex.setProvisionedThroughput(provisionedThroughput);
				});
			}
			if (request.getLocalSecondaryIndexes() != null) {
				request.getLocalSecondaryIndexes().forEach(localSendaryIndex -> {
					localSendaryIndex.setProjection(projection);
				});
			}
			System.out.println("Issuing CreateTable request for " + request.getTableName());
			Table table = dynamoDB.createTable(request);
			System.out.println("Waiting for " + request.getTableName() + " to be created...this may take a while...");
			table.waitForActive();
			System.out.println("Create " + request.getTableName() + " success");
		} catch (Exception e) {
			System.err.println("CreateTable request failed for " + request.getTableName());
			System.err.println(e.getMessage());
		}
	}

	private String getAvailablePort() {
		try (final ServerSocket serverSocket = new ServerSocket(0)) {
			return String.valueOf(serverSocket.getLocalPort());
		} catch (IOException e) {
			throw new RuntimeException("Available port was not found", e);
		}
	}
}
