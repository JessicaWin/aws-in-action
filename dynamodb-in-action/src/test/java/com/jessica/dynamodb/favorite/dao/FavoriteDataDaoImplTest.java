package com.jessica.dynamodb.favorite.dao;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jessica.dynamodb.favorite.dao.impl.FavoriteDataDaoImpl;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.FavoriteDataDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataType;

public class FavoriteDataDaoImplTest {
	private FavoriteDataDao favoriteDataDao = new FavoriteDataDaoImpl();
	private String userId1 = "userId1";
	private String userId2 = "userId2";
	private String dataId1 = "dataId1";
	private String dataId2 = "dataId2";
	private String dataId3 = "dataId3";
	private String dataId4 = "dataId4";
	private String dataId5 = "dataId5";
	private String dataId6 = "dataId6";
	private FavoriteDataType dataType1 = FavoriteDataType.FILE;
	private FavoriteDataType dataType2 = FavoriteDataType.IMAGE;
	private Date date = new Date();

	@Test
	public void testGetFavoriteDataByUserIdAndType() {
		// prepare data
		List<FavoriteDataDto> dtos = this.prepareDataDtos();
		try {

			// test clip time desc order
			LazyLoadResult<FavoriteDataDto> lazyLoadResult = favoriteDataDao.getFavoriteData(userId1, dataType1, null,
					2, false);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(1).getDataId());
			lazyLoadResult = favoriteDataDao.getFavoriteData(userId1, dataType1, lazyLoadResult.getLastLoadPos(), 3,
					false);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(1).getDataId());

			// test clip time asc order
			lazyLoadResult = favoriteDataDao.getFavoriteData(userId1, dataType1, null, 2, true);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(1).getDataId());
			lazyLoadResult = favoriteDataDao.getFavoriteData(userId1, dataType1, lazyLoadResult.getLastLoadPos(), 3,
					true);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(1).getDataId());
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			favoriteDataDao.batchDelete(dtos);
		}
	}

	@Test
	public void testGetFavoriteDataByUserIdSortByDataId() {
		// prepare data
		List<FavoriteDataDto> dtos = this.prepareDataDtos();
		try {
			// test dataId desc order
			LazyLoadResult<FavoriteDataDto> lazyLoadResult = favoriteDataDao.getFavoriteDataSortByDataId(userId1, null,
					4, false);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId6, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId5, lazyLoadResult.getLoadedDtos().get(1).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(2).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(3).getUserId());
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(3).getDataId());
			lazyLoadResult = favoriteDataDao.getFavoriteDataSortByDataId(userId1, lazyLoadResult.getLastLoadPos(), 4,
					false);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(1).getDataId());

			// test dataId asc order
			lazyLoadResult = favoriteDataDao.getFavoriteDataSortByDataId(userId1, null, 4, true);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(1).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(2).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(3).getUserId());
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(3).getDataId());
			lazyLoadResult = favoriteDataDao.getFavoriteDataSortByDataId(userId1, lazyLoadResult.getLastLoadPos(), 4,
					true);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId5, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId6, lazyLoadResult.getLoadedDtos().get(1).getDataId());
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			favoriteDataDao.batchDelete(dtos);
		}
	}

	@Test
	public void testGetFavoriteDataByUserIdSortByCreateTime() {
		// prepare data
		List<FavoriteDataDto> dtos = this.prepareDataDtos();
		try {
			// test dataId desc order
			LazyLoadResult<FavoriteDataDto> lazyLoadResult = favoriteDataDao.getFavoriteDataSortByCreateTime(userId1,
					null, 4, false);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(1).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(2).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(3).getUserId());
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(3).getDataId());
			lazyLoadResult = favoriteDataDao.getFavoriteDataSortByCreateTime(userId1, lazyLoadResult.getLastLoadPos(),
					4, false);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId5, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId6, lazyLoadResult.getLoadedDtos().get(1).getDataId());
			// test dataId asc order
			lazyLoadResult = favoriteDataDao.getFavoriteDataSortByCreateTime(userId1, null, 4, true);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId6, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId5, lazyLoadResult.getLoadedDtos().get(1).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(dataId4, lazyLoadResult.getLoadedDtos().get(2).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(3).getUserId());
			assertEquals(dataId3, lazyLoadResult.getLoadedDtos().get(3).getDataId());
			lazyLoadResult = favoriteDataDao.getFavoriteDataSortByCreateTime(userId1, lazyLoadResult.getLastLoadPos(),
					4, true);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(dataId2, lazyLoadResult.getLoadedDtos().get(0).getDataId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(dataId1, lazyLoadResult.getLoadedDtos().get(1).getDataId());
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			favoriteDataDao.batchDelete(dtos);
		}
	}

	private List<FavoriteDataDto> prepareDataDtos() {
		FavoriteDataDto dataDto1 = FavoriteDataDto.builder().userId(userId1).dataId(dataId1)
				.clipTime(String.valueOf(date.getTime())).dataType(dataType1).build();
		FavoriteDataDto dataDto2 = FavoriteDataDto.builder().userId(userId1).dataId(dataId2)
				.clipTime(String.valueOf(date.getTime() - 2000)).dataType(dataType1).build();
		FavoriteDataDto dataDto3 = FavoriteDataDto.builder().userId(userId1).dataId(dataId3)
				.clipTime(String.valueOf(date.getTime() - 3000)).dataType(dataType1).build();
		FavoriteDataDto dataDto4 = FavoriteDataDto.builder().userId(userId1).dataId(dataId4)
				.clipTime(String.valueOf(date.getTime() - 4000)).dataType(dataType1).build();
		FavoriteDataDto dataDto5 = FavoriteDataDto.builder().userId(userId1).dataId(dataId5)
				.clipTime(String.valueOf(date.getTime() - 5000)).dataType(dataType2).build();
		FavoriteDataDto dataDto6 = FavoriteDataDto.builder().userId(userId1).dataId(dataId6)
				.clipTime(String.valueOf(date.getTime() - 6000)).dataType(dataType2).build();
		FavoriteDataDto dataDto7 = FavoriteDataDto.builder().userId(userId2).dataId(dataId5)
				.clipTime(String.valueOf(date.getTime() - 7000)).dataType(dataType1).build();
		FavoriteDataDto dataDto8 = FavoriteDataDto.builder().userId(userId2).dataId(dataId6)
				.clipTime(String.valueOf(date.getTime() - 8000)).dataType(dataType2).build();
		List<FavoriteDataDto> dtos = Arrays.asList(dataDto1, dataDto2, dataDto3, dataDto4, dataDto5, dataDto6, dataDto7,
				dataDto8);
		favoriteDataDao.batchSave(dtos);
		return dtos;
	}
}
