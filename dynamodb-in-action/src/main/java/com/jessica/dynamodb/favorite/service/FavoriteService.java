package com.jessica.dynamodb.favorite.service;

import com.jessica.dynamodb.favorite.dto.FavoriteDataDto;

public interface FavoriteService {

	/**
	 * 
	 * @param favoriteDataDto
	 */
	void clip(FavoriteDataDto favoriteDataDto);

	/**
	 * 
	 * @param userId
	 * @param dataId
	 */
	void unclip(String userId, String dataId);

	/**
	 * 
	 * @param userId
	 * @param dataId
	 */
	void undoUnclip(String userId, String dataId);

}
