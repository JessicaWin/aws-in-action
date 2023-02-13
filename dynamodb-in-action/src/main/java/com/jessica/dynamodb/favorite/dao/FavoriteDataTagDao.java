package com.jessica.dynamodb.favorite.dao;

import java.util.List;

import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.FavoriteDataTagDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataType;

public interface FavoriteDataTagDao extends BasicDao<FavoriteDataTagDto> {

	/**
	 * 
	 * get all tag ids for given tagId in tagId order
	 * 
	 * @param userId
	 * @param dataId
	 * @param asc
	 * @return
	 */
	List<String> getTagIdsForData(String userId, String dataId, boolean asc);

	/**
	 * 
	 * get data ids for userId with given tagId
	 * 
	 * @param userId
	 * @param tagId
	 * @param lastLoadSk
	 * @param size
	 * @param asc
	 * @return
	 */
	LazyLoadResult<String> getFavoriteDataIds(String userId, String tagId, String lastLoadSk, Integer size,
			boolean asc);

	/**
	 * get data ids for userId with given tagId and type
	 * 
	 * @param userId
	 * @param tagId
	 * @param dataType
	 * @param lastLoadSk
	 * @param size
	 * @param asc
	 * @return
	 */
	LazyLoadResult<String> getFavoriteDataIds(String userId, String tagId, FavoriteDataType dataType, String lastLoadSk,
			Integer size, boolean asc);
}
