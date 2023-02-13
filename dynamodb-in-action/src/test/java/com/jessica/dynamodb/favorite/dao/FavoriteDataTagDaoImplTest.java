package com.jessica.dynamodb.favorite.dao;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jessica.dynamodb.favorite.dao.impl.FavoriteDataTagDaoImpl;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.FavoriteDataTagDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataType;

public class FavoriteDataTagDaoImplTest {
	FavoriteDataTagDao favoriteDataTagDao = new FavoriteDataTagDaoImpl();
	String userId1 = "userId1";
	String userId2 = "userId2";
	String tagId1 = "tagId1";
	String tagId2 = "tagId2";
	String tagId3 = "tagId3";
	String dataId1 = "dataId1";
	String dataId2 = "dataId2";
	String dataId3 = "dataId3";
	String dataId4 = "dataId4";
	String dataId5 = "dataId5";
	String dataId6 = "dataId6";
	String dataId7 = "dataId7";
	String dataId8 = "dataId8";
	String dataId9 = "dataId9";
	String dataId10 = "dataId10";
	Date nowDate = new Date();
	FavoriteDataType dataType1 = FavoriteDataType.FILE;
	FavoriteDataType dataType2 = FavoriteDataType.IMAGE;

	@Test
	public void getTagIdsForData() {
		// prepare data
		List<FavoriteDataTagDto> dtos = this.prepareDataTagDtos();
		// run test
		try {
			// test clip time desc order
			List<String> tagIds = favoriteDataTagDao.getTagIdsForData(userId1, dataId1, false);
			assertEquals(3, tagIds.size());
			assertEquals(tagId3, tagIds.get(0));
			assertEquals(tagId2, tagIds.get(1));
			assertEquals(tagId1, tagIds.get(2));

			// test clip time asc order
			tagIds = favoriteDataTagDao.getTagIdsForData(userId1, dataId1, true);
			assertEquals(3, tagIds.size());
			assertEquals(tagId1, tagIds.get(0));
			assertEquals(tagId2, tagIds.get(1));
			assertEquals(tagId3, tagIds.get(2));
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			favoriteDataTagDao.batchDelete(dtos);
		}
	}

	@Test
	public void testGetFavoriteDataIdsByTagId() {
		// prepare data
		List<FavoriteDataTagDto> dtos = this.prepareDataTagDtos();
		// run test
		try {
			// test clip time desc order
			LazyLoadResult<String> lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, null, 4,
					false);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(1));
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(2));
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(3));
			lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, lazyLoadResult.getLastLoadPos(), 3,
					false);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(dataId5, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId6, lazyLoadResult.getLoadedDtos().get(1));

			// test clip time asc order
			lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, null, 4, true);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(dataId6, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId5, lazyLoadResult.getLoadedDtos().get(1));
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(2));
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(3));
			lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, lazyLoadResult.getLastLoadPos(), 3,
					true);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(1));
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			favoriteDataTagDao.batchDelete(dtos);
		}
	}

	@Test
	public void testGetFavoriteDataIdsByTagIdAndType() {
		// prepare data
		List<FavoriteDataTagDto> dtos = this.prepareDataTagDtos();
		// run test
		try {
			// test clip time desc order
			LazyLoadResult<String> lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, dataType1,
					null, 2, false);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(1));
			lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, dataType1,
					lazyLoadResult.getLastLoadPos(), 3, false);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(1));

			// test clip time asc order
			lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, dataType1, null, 2, true);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(1));
			lazyLoadResult = favoriteDataTagDao.getFavoriteDataIds(userId1, tagId1, dataType1,
					lazyLoadResult.getLastLoadPos(), 3, true);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(0));
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(1));
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			favoriteDataTagDao.batchDelete(dtos);
		}
	}

	private List<FavoriteDataTagDto> prepareDataTagDtos() {
		FavoriteDataTagDto dataTagDto1 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId1).dataId(dataId1)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime())).build();
		FavoriteDataTagDto dataTagDto1_1 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId2).dataId(dataId1)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 1000)).build();
		FavoriteDataTagDto dataTagDto1_2 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId3).dataId(dataId1)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 2000)).build();
		FavoriteDataTagDto dataTagDto2 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId1).dataId(dataId2)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 1000)).build();
		FavoriteDataTagDto dataTagDto3 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId1).dataId(dataId3)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 2000)).build();
		FavoriteDataTagDto dataTagDto4 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId1).dataId(dataId4)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 3000)).build();
		FavoriteDataTagDto dataTagDto5 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId1).dataId(dataId5)
				.dataType(dataType2).clipTime(String.valueOf(nowDate.getTime() - 4000)).build();
		FavoriteDataTagDto dataTagDto6 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId1).dataId(dataId6)
				.dataType(dataType2).clipTime(String.valueOf(nowDate.getTime() - 5000)).build();
		FavoriteDataTagDto dataTagDto7 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId2).dataId(dataId7)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 6000)).build();
		FavoriteDataTagDto dataTagDto8 = FavoriteDataTagDto.builder().userId(userId1).tagId(tagId2).dataId(dataId8)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 7000)).build();
		FavoriteDataTagDto dataTagDto9 = FavoriteDataTagDto.builder().userId(userId2).tagId(tagId1).dataId(dataId9)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 8000)).build();
		FavoriteDataTagDto dataTagDto10 = FavoriteDataTagDto.builder().userId(userId2).tagId(tagId1).dataId(dataId10)
				.dataType(dataType1).clipTime(String.valueOf(nowDate.getTime() - 9000)).build();
		List<FavoriteDataTagDto> dtos = Arrays.asList(dataTagDto1, dataTagDto1_1, dataTagDto1_2, dataTagDto2,
				dataTagDto3, dataTagDto4, dataTagDto5, dataTagDto6, dataTagDto7, dataTagDto8, dataTagDto9,
				dataTagDto10);
		favoriteDataTagDao.batchSave(dtos);
		return dtos;
	}
}
