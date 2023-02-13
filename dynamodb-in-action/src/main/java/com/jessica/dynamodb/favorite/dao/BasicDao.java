package com.jessica.dynamodb.favorite.dao;

import java.util.List;
import java.util.Map;

import com.jessica.dynamodb.favorite.data.LazyLoadResult;

public interface BasicDao<T> {
	/**
	 * create or update dto
	 * 
	 * @param dto
	 */
	void save(T dto);

	/**
	 * load dto
	 * 
	 * @param keyDto, pk and sk related fields must set
	 * @return
	 */
	T load(T keyDto);

	/**
	 * delete dto
	 * 
	 * @param keyDto, pk and sk related fields must set
	 */
	void delete(T keyDto);

	/**
	 * create or update dtos
	 * 
	 * @param dtos
	 */
	void batchSave(List<T> dtos);

	/**
	 * load dtos, result list sequence may not be same with keyDtos
	 * 
	 * @param keyDtos, pk and sk related fields must set
	 * @return
	 */
	List<T> batchLoad(List<T> keyDtos);

	/**
	 * load dto map
	 * 
	 * @param keyDtos, pk and sk related fields must set
	 * @return key is pk#sk
	 */
	Map<String, T> batchLoadMap(List<T> keyDtos);

	/**
	 * delete dtos
	 * 
	 * @param keyDtos, pk and sk related fields must set
	 * @return
	 */
	void batchDelete(List<T> keyDtos);

	/**
	 * query with pk in given order
	 * 
	 * @param clazz
	 * @param hashKeyDto
	 * @param asc
	 * @param lastLoadSk
	 * @param size
	 * @return
	 */
	LazyLoadResult<T> query(Class<T> clazz, T hashKeyDto, boolean asc, String lastLoadSk, Integer size);

	/**
	 * query index with pk in given order
	 * 
	 * @param clazz
	 * @param indexName
	 * @param indexSkName
	 * @param isGlobalIndex
	 * @param hashKeyDto
	 * @param asc
	 * @param lastLoadSk
	 * @param size
	 * @return
	 */
	LazyLoadResult<T> queryIndex(Class<T> clazz, String indexName, String indexSkName, boolean isGlobalIndex,
			T hashKeyDto, boolean asc, String lastLoadSk, Integer size);
}
