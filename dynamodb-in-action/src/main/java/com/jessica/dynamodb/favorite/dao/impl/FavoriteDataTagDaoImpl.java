package com.jessica.dynamodb.favorite.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.jessica.dynamodb.constant.DynamoDBConstant;
import com.jessica.dynamodb.favorite.dao.FavoriteDataTagDao;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.FavoriteDataTagDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataType;

public class FavoriteDataTagDaoImpl extends BasicDaoImpl<FavoriteDataTagDto> implements FavoriteDataTagDao {

	@Override
	public List<String> getTagIdsForData(String userId, String dataId, boolean asc) {
		LazyLoadResult<FavoriteDataTagDto> result = this.query(FavoriteDataTagDto.class,
				FavoriteDataTagDto.builder().userId(userId).dataId(dataId).build(), asc, null, null);
		return result.getLoadedDtos().stream().map(FavoriteDataTagDto::getTagId).collect(Collectors.toList());
	}

	@Override
	public LazyLoadResult<String> getFavoriteDataIds(String userId, String tagId, String lastLoadSk, Integer size,
			boolean asc) {
		LazyLoadResult<FavoriteDataTagDto> result = this.queryIndex(FavoriteDataTagDto.class,
				DynamoDBConstant.GSI_ONE_NAME, DynamoDBConstant.GSI_ONE_RANGE_KEY, true,
				FavoriteDataTagDto.builder().userId(userId).tagId(tagId).build(), asc, lastLoadSk, size);
		return convertToStringResult(result);
	}

	@Override
	public LazyLoadResult<String> getFavoriteDataIds(String userId, String tagId, FavoriteDataType dataType,
			String lastLoadSk, Integer size, boolean asc) {
		LazyLoadResult<FavoriteDataTagDto> result = this.queryIndex(FavoriteDataTagDto.class,
				DynamoDBConstant.GSI_TWO_NAME, DynamoDBConstant.GSI_TWO_RANGE_KEY, true,
				FavoriteDataTagDto.builder().userId(userId).tagId(tagId).dataType(dataType).build(), asc, lastLoadSk,
				size);
		return convertToStringResult(result);
	}

	private LazyLoadResult<String> convertToStringResult(LazyLoadResult<FavoriteDataTagDto> dtoLazyLoadResult) {
		return new LazyLoadResult<>(dtoLazyLoadResult.getLoadedDtos().stream().map(FavoriteDataTagDto::getDataId)
				.collect(Collectors.toList()), dtoLazyLoadResult.isHasMore(), dtoLazyLoadResult.getLastLoadPos());
	}
}
