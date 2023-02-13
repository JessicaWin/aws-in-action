package com.jessica.dynamodb.favorite.dao.impl;

import com.jessica.dynamodb.constant.DynamoDBConstant;
import com.jessica.dynamodb.favorite.dao.FavoriteDataDao;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.FavoriteDataDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataType;

public class FavoriteDataDaoImpl extends BasicDaoImpl<FavoriteDataDto> implements FavoriteDataDao {

	@Override
	public LazyLoadResult<FavoriteDataDto> getFavoriteDataSortByDataId(String userId, String lastLoadSk, Integer size,
			boolean asc) {
		return this.query(FavoriteDataDto.class, FavoriteDataDto.builder().userId(userId).build(), asc, lastLoadSk,
				size);
	}

	@Override
	public LazyLoadResult<FavoriteDataDto> getFavoriteData(String userId, FavoriteDataType dataType, String lastLoadSk,
			Integer size, boolean asc) {
		return this.queryIndex(FavoriteDataDto.class, DynamoDBConstant.GSI_ONE_NAME, DynamoDBConstant.GSI_ONE_RANGE_KEY,
				true, FavoriteDataDto.builder().userId(userId).dataType(dataType).build(), asc, lastLoadSk, size);
	}

	@Override
	public LazyLoadResult<FavoriteDataDto> getFavoriteDataSortByCreateTime(String userId, String lastLoadSk,
			Integer size, boolean asc) {
		return this.queryIndex(FavoriteDataDto.class, DynamoDBConstant.LSI_ONE_NAME, DynamoDBConstant.LSI_ONE_RANGE_KEY,
				false, FavoriteDataDto.builder().userId(userId).build(), asc, lastLoadSk, size);
	}

}
