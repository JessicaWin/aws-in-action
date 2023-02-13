package com.jessica.dynamodb.favorite.dao;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.jessica.dynamodb.constant.TagSortField;
import com.jessica.dynamodb.favorite.dao.impl.TagDaoImpl;
import com.jessica.dynamodb.favorite.data.LazyLoadResult;
import com.jessica.dynamodb.favorite.dto.TagDto;

public class TagDaoImplTest {

	TagDao tagDao = new TagDaoImpl();

	@Test
	public void testGetTagsByUserId() {
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
		tagDao.save(newTagDto1);
		TagDto newTagDto2 = TagDto.builder().userId(userId1).tagId(tagId2).tagName(tagName2)
				.createTime(date.getTime() - 5000).lastAccessTime(date.getTime() - 5000).build();
		tagDao.save(newTagDto2);
		TagDto newTagDto3 = TagDto.builder().userId(userId1).tagId(tagId3).tagName(tagName3)
				.createTime(date.getTime() - 1000).lastAccessTime(date.getTime() - 1000).build();
		tagDao.save(newTagDto3);
		TagDto newTagDto4 = TagDto.builder().userId(userId1).tagId(tagId4).tagName(tagName4).createTime(date.getTime())
				.lastAccessTime(date.getTime()).build();
		List<TagDto> dtos = Arrays.asList(newTagDto1, newTagDto2, newTagDto3, newTagDto4);
		tagDao.batchSave(dtos);
		try {

			// test tag name desc order
			LazyLoadResult<TagDto> lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.TAG_NAME, null, 3,
					false);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.TAG_NAME, lazyLoadResult.getLastLoadPos(), 3,
					false);
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());

			// test tag name aes order
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.TAG_NAME, null, 3, true);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.TAG_NAME, lazyLoadResult.getLastLoadPos(), 3,
					true);
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(0).getTagId());

			// test create time desc order
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.CREATE_TIME, null, 3, false);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.CREATE_TIME, lazyLoadResult.getLastLoadPos(),
					3, false);
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());

			// test create time aes order
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.CREATE_TIME, null, 3, true);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.CREATE_TIME, lazyLoadResult.getLastLoadPos(),
					3, true);
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(0).getTagId());

			// test last access time desc order
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.LAST_ACCESS_TIME, null, 3, false);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.LAST_ACCESS_TIME,
					lazyLoadResult.getLastLoadPos(), 3, false);
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());

			// test last access time aes order
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.LAST_ACCESS_TIME, null, 3, true);
			assertEquals(3, lazyLoadResult.getLoadedDtos().size());
			assertEquals(true, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId1, lazyLoadResult.getLoadedDtos().get(0).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(1).getUserId());
			assertEquals(tagId2, lazyLoadResult.getLoadedDtos().get(1).getTagId());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(2).getUserId());
			assertEquals(tagId3, lazyLoadResult.getLoadedDtos().get(2).getTagId());
			lazyLoadResult = tagDao.getTagsByUserId(userId1, TagSortField.LAST_ACCESS_TIME,
					lazyLoadResult.getLastLoadPos(), 3, true);
			assertEquals(1, lazyLoadResult.getLoadedDtos().size());
			assertEquals(false, lazyLoadResult.isHasMore());
			assertEquals(userId1, lazyLoadResult.getLoadedDtos().get(0).getUserId());
			assertEquals(tagId4, lazyLoadResult.getLoadedDtos().get(0).getTagId());
		} catch (Exception e) {
			throw e;
		} finally {
			// clean data
			tagDao.batchDelete(dtos);
		}
	}
}