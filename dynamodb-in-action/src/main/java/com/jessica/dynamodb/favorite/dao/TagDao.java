package com.jessica.dynamodb.favorite.dao;

import com.jessica.dynamodb.constant.TagSortField;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.TagDto;

public interface TagDao extends BasicDao<TagDto> {
	/**
	 * 
	 * @param userId
	 * @param tagSortField
	 * @param lastLoadSk
	 * @param size
	 * @param asc
	 * @return
	 */
	LazyLoadResult<TagDto> getTagsByUserId(String userId, TagSortField tagSortField, String lastLoadSk, Integer size,
			boolean asc);
}
