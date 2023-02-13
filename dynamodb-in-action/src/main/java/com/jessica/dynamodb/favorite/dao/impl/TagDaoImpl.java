package com.jessica.dynamodb.favorite.dao.impl;

import com.jessica.dynamodb.constant.TagSortField;
import com.jessica.dynamodb.favorite.dao.TagDao;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.TagDto;

public class TagDaoImpl extends BasicDaoImpl<TagDto> implements TagDao {

	@Override
	public LazyLoadResult<TagDto> getTagsByUserId(String userId, TagSortField tagSortField, String lastLoadSk,
			Integer size, boolean asc) {
		return this.queryIndex(TagDto.class, tagSortField.getGsiName(), tagSortField.getGsiSkName(), true,
				TagDto.builder().userId(userId).build(), asc, lastLoadSk, size);
	}
}
