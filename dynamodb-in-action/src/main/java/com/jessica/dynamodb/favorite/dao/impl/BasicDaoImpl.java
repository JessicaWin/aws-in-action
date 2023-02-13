package com.jessica.dynamodb.favorite.dao.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.jessica.dynamodb.constant.DynamoDBConstant;
import com.jessica.dynamodb.favorite.dao.BasicDao;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.AbstractDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BasicDaoImpl<T extends AbstractDto> implements BasicDao<T> {
	protected DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().build());

	@Override
	public void save(T dto) {
		this.dynamoDBMapper.save(dto);
	}

	@Override
	public T load(T keyDto) {
		return this.dynamoDBMapper.load(keyDto);
	}

	@Override
	public void delete(T keyDto) {
		this.dynamoDBMapper.delete(keyDto);
	}

	@Override
	public void batchSave(List<T> dtos) {
		this.dynamoDBMapper.batchSave(dtos);
	}

	@Override
	public List<T> batchLoad(List<T> keyDtos) {
		return this.dynamoDBMapper.batchLoad(keyDtos).values().stream().flatMap(Collection::stream)
				.map(object -> (T) object).collect(Collectors.toList());
	}

	@Override
	public Map<String, T> batchLoadMap(List<T> keyDtos) {
		return this.batchLoad(keyDtos).stream()
				.collect(Collectors.toMap(t -> ((AbstractDto) t).getCompositeKey(), Function.identity()));
	}

	@Override
	public void batchDelete(List<T> keyDtos) {
		this.dynamoDBMapper.batchDelete(keyDtos);
	}

	@Override
	public LazyLoadResult<T> query(Class<T> clazz, T hashKeyDto, boolean asc, String lastLoadSk, Integer size) {
		DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<T>().withHashKeyValues(hashKeyDto)
				.withScanIndexForward(asc);
		if (lastLoadSk != null) {
			Condition rangeCondition = new Condition()
					.withComparisonOperator(asc ? ComparisonOperator.GT : ComparisonOperator.LT)
					.withAttributeValueList(new AttributeValue(lastLoadSk));
			queryExpression.withRangeKeyCondition(DynamoDBConstant.RANGE_KEY, rangeCondition);
		}

		List<T> dtos;
		boolean hasLimit = size != null && size > 0;
		if (hasLimit) {
			queryExpression.setLimit(size);
			dtos = this.dynamoDBMapper.queryPage(clazz, queryExpression).getResults();
		} else {
			dtos = this.dynamoDBMapper.query(clazz, queryExpression);
		}

		if (dtos.size() == 0) {
			return new LazyLoadResult<>(new ArrayList<>(), false, null);
		}
		return new LazyLoadResult<>(dtos, hasLimit ? dtos.size() == size : false, dtos.get(dtos.size() - 1).getSk());
	}

	@Override
	public LazyLoadResult<T> queryIndex(Class<T> clazz, String indexName, String indexSkName, boolean isGlobalIndex,
			T hashKeyDto, boolean asc, String lastLoadSk, Integer size) {
		DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<T>().withIndexName(indexName)
				.withHashKeyValues(hashKeyDto).withScanIndexForward(asc).withConsistentRead(!isGlobalIndex);
		if (lastLoadSk != null) {
			Condition rangeCondition = new Condition()
					.withComparisonOperator(asc ? ComparisonOperator.GT : ComparisonOperator.LT)
					.withAttributeValueList(new AttributeValue(lastLoadSk));
			queryExpression.withRangeKeyCondition(indexSkName, rangeCondition);
		}

		List<T> dtos;
		boolean hasLimit = size != null && size > 0;
		if (hasLimit) {
			queryExpression.setLimit(size);
			dtos = this.dynamoDBMapper.queryPage(clazz, queryExpression).getResults();
		} else {
			dtos = this.dynamoDBMapper.query(clazz, queryExpression);
		}

		if (dtos.size() == 0) {
			return new LazyLoadResult<>(new ArrayList<>(), false, null);
		}
		T lastLoadDto = dtos.get(dtos.size() - 1);
		try {
			Method method = clazz.getMethod("get" + upperCaseFirstLatter(indexSkName));
			return new LazyLoadResult<T>(dtos, hasLimit ? dtos.size() == size : false,
					(String) method.invoke(lastLoadDto));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private String upperCaseFirstLatter(String str) {
		char[] strChar = str.toCharArray();
		strChar[0] -= 32;
		return String.valueOf(strChar);
	}
}
