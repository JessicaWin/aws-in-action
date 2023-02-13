package com.jessica.dynamodb.favorite.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.jessica.dynamodb.constant.DynamoDBConstant;
import com.jessica.dynamodb.favorite.dao.impl.BasicDaoImpl;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.TagDto;

public class BasicDaoImplTest {

	BasicDao<TagDto> basicDao = new BasicDaoImpl<>();

	@Test
	public void testSaveLoadDelete() {
		// prepare data
		String userId = "userId1";
		String tagId = UUID.randomUUID().toString();
		String tagName = "firstTag";
		Date now = new Date();
		TagDto newTagDto = TagDto.builder().userId(userId).tagId(tagId).tagName(tagName).createTime(now.getTime())
				.lastAccessTime(now.getTime()).build();
		// run test
		basicDao.save(newTagDto);
		TagDto loadedDto = basicDao.load(newTagDto);
		assertNotNull(loadedDto);

		// clean data
		basicDao.delete(newTagDto);
		loadedDto = basicDao.load(newTagDto);
		assertNull(loadedDto);
	}

	@Test
	public void testBatchSaveLoadDelete() {
		// prepare data
		String userId1 = "userId1";
		String tagId1 = UUID.randomUUID().toString();
		String tagName1 = "firstTag";
		Date date1 = new Date();
		String userId2 = "userId2";
		String tagId2 = UUID.randomUUID().toString();
		String tagName2 = "secondTag";
		Date date2 = new Date();
		TagDto newTagDto1 = TagDto.builder().userId(userId1).tagId(tagId1).tagName(tagName1).createTime(date1.getTime())
				.lastAccessTime(date1.getTime()).build();
		TagDto newTagDto2 = TagDto.builder().userId(userId2).tagId(tagId2).tagName(tagName2).createTime(date2.getTime())
				.lastAccessTime(date2.getTime()).build();
		List<TagDto> dtos = Arrays.asList(newTagDto1, newTagDto2);
		basicDao.batchSave(dtos);

		// run test
		List<TagDto> loadedDtos = basicDao.batchLoad(dtos);
		assertEquals(2, loadedDtos.size());

		// clean data
		basicDao.batchDelete(dtos);

		// run test
		loadedDtos = basicDao.batchLoad(dtos);
		assertEquals(0, loadedDtos.size());
	}

	@Test
	public void testQuery() {
		// prepare data
		String userId1 = "userId1";
		String tagId1 = "firstTag";
		String tagName1 = "firstTag";
		Date date = new Date();
		String tagId2 = "secondTag";
		String tagName2 = "secondTag";
		String tagId3 = "thirdTag";
		String tagName3 = "thirdTag";
		String tagId4 = "fouthTag";
		String tagName4 = "fouthTag";
		String userId2 = "userId2";
		String tagId5 = "fifthTag";
		String tagName5 = "fifthTag";
		TagDto newTagDto1 = TagDto.builder().userId(userId1).tagId(tagId1).tagName(tagName1)
				.createTime(date.getTime() - 10000).lastAccessTime(date.getTime() - 10000).build();
		TagDto newTagDto2 = TagDto.builder().userId(userId1).tagId(tagId2).tagName(tagName2)
				.createTime(date.getTime() - 5000).lastAccessTime(date.getTime() - 5000).build();
		TagDto newTagDto3 = TagDto.builder().userId(userId1).tagId(tagId3).tagName(tagName3)
				.createTime(date.getTime() - 1000).lastAccessTime(date.getTime() - 1000).build();
		TagDto newTagDto4 = TagDto.builder().userId(userId1).tagId(tagId4).tagName(tagName4).createTime(date.getTime())
				.lastAccessTime(date.getTime()).build();
		TagDto newTagDto5 = TagDto.builder().userId(userId2).tagId(tagId5).tagName(tagName5).createTime(date.getTime())
				.lastAccessTime(date.getTime()).build();
		List<TagDto> dtos = Arrays.asList(newTagDto1, newTagDto2, newTagDto3, newTagDto4, newTagDto5);
		basicDao.batchSave(dtos);

		try {
			// test sk desc order with size
			LazyLoadResult<TagDto> lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, false, null, 2);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, false,
					lazyLoadResult.getLoadedDtos().get(1).getSk(), 3);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(1).getTagId());

			// test sk aes order with size
			lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, true, null, 2);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, true,
					lazyLoadResult.getLoadedDtos().get(1).getSk(), 3);
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(1).getTagId());

			// test sk aes order without size
			lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, true, null, null);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(3).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(3).getTagId());

			lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, true, newTagDto4.getSk(), null);
			assertEquals(2, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(1).getTagId());

			// test sk desc order without size
			lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, false, null, null);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(3).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(3).getTagId());

			lazyLoadResult = basicDao.query(TagDto.class, newTagDto1, false, newTagDto3.getSk(), null);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(2).getTagId());
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			basicDao.batchDelete(dtos);
		}
	}

	@Test
	public void testQueryIndex() {
		// prepare data
		String userId1 = "userId1";
		String tagId1 = UUID.randomUUID().toString();
		String tagName1 = "firstTag";
		Date date = new Date();
		String tagId2 = UUID.randomUUID().toString();
		String tagName2 = "secondTag";
		String tagId3 = UUID.randomUUID().toString();
		String tagName3 = "thirdTag";
		String tagId4 = UUID.randomUUID().toString();
		String tagName4 = "fouthTag";
		TagDto newTagDto1 = TagDto.builder().userId(userId1).tagId(tagId1).tagName(tagName1)
				.createTime(date.getTime() - 10000).lastAccessTime(date.getTime() - 10000).build();
		basicDao.save(newTagDto1);
		TagDto newTagDto2 = TagDto.builder().userId(userId1).tagId(tagId2).tagName(tagName2)
				.createTime(date.getTime() - 5000).lastAccessTime(date.getTime() - 5000).build();
		basicDao.save(newTagDto2);
		TagDto newTagDto3 = TagDto.builder().userId(userId1).tagId(tagId3).tagName(tagName3)
				.createTime(date.getTime() - 1000).lastAccessTime(date.getTime() - 1000).build();
		basicDao.save(newTagDto3);
		TagDto newTagDto4 = TagDto.builder().userId(userId1).tagId(tagId4).tagName(tagName4).createTime(date.getTime())
				.lastAccessTime(date.getTime()).build();
		List<TagDto> dtos = Arrays.asList(newTagDto1, newTagDto2, newTagDto3, newTagDto4);
		basicDao.batchSave(dtos);
		try {
			// test tag name desc order
			LazyLoadResult<TagDto> lazyLoadResult = basicDao.queryIndex(TagDto.class, DynamoDBConstant.GSI_ONE_NAME,
					DynamoDBConstant.GSI_ONE_RANGE_KEY, true, TagDto.builder().userId(userId1).build(), false, null, 3);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = basicDao.queryIndex(TagDto.class, DynamoDBConstant.GSI_ONE_NAME,
					DynamoDBConstant.GSI_ONE_RANGE_KEY, true, TagDto.builder().userId(userId1).build(), false,
					lazyLoadResult.getLastLoadPos(), 3);
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());

			// test load all
			lazyLoadResult = basicDao.queryIndex(TagDto.class, DynamoDBConstant.GSI_ONE_NAME,
					DynamoDBConstant.GSI_ONE_RANGE_KEY, true, TagDto.builder().userId(userId1).build(), false, null,
					null);
			assertEquals(4, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(3).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(3).getTagId());

			// test tag name aes order
			lazyLoadResult = basicDao.queryIndex(TagDto.class, DynamoDBConstant.GSI_ONE_NAME,
					DynamoDBConstant.GSI_ONE_RANGE_KEY, true, TagDto.builder().userId(userId1).build(), true, null, 3);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = basicDao.queryIndex(TagDto.class, DynamoDBConstant.GSI_ONE_NAME,
					DynamoDBConstant.GSI_ONE_RANGE_KEY, true, TagDto.builder().userId(userId1).build(), true,
					lazyLoadResult.getLastLoadPos(), 3);
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(0).getTagId());
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			basicDao.batchDelete(dtos);
		}
	}
}
