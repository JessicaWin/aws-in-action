package com.jessica.dynamodb.favorite.dao;

import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.FavoriteDataDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataType;

public interface FavoriteDataDao extends BasicDao<FavoriteDataDto> {
	/**
	 * get data for userId sort by dataId
	 * 
	 * @param userId
	 * @param lastLoadSk
	 * @param size
	 * @param asc
	 * @return
	 */
	LazyLoadResult<FavoriteDataDto> getFavoriteDataSortByDataId(String userId, String lastLoadSk, Integer size,
			boolean asc);

	/**
	 * get data for userId sort by data create time
	 * 
	 * @param userId
	 * @param lastLoadSk
	 * @param size
	 * @param asc
	 * @return
	 */
	LazyLoadResult<FavoriteDataDto> getFavoriteDataSortByCreateTime(String userId, String lastLoadSk, Integer size,
			boolean asc);

	/**
	 * get data for userId with given type
	 * 
	 * @param userId
	 * @param dataType
	 * @param lastLoadSk
	 * @param size
	 * @param asc
	 * @return
	 */
	LazyLoadResult<FavoriteDataDto> getFavoriteData(String userId, FavoriteDataType dataType, String lastLoadSk,
			Integer size, boolean asc);
}
