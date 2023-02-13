package com.jessica.dynamodb.favorite.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.jessica.dynamodb.favorite.dao.FavoriteDataDao;
import com.jessica.dynamodb.favorite.dao.FavoriteDataHistoryDao;
import com.jessica.dynamodb.favorite.dao.FavoriteDataTagDao;
import com.jessica.dynamodb.favorite.dao.impl.FavoriteDataDaoImpl;
import com.jessica.dynamodb.favorite.dao.impl.FavoriteDataHistoryDaoImpl;
import com.jessica.dynamodb.favorite.dao.impl.FavoriteDataTagDaoImpl;
import com.jessica.dynamodb.favorite.dto.FavoriteDataDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataHistoryDto;
import com.jessica.dynamodb.favorite.dto.FavoriteDataTagDto;
import com.jessica.dynamodb.favorite.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
	private FavoriteDataHistoryDao favoriteDataHistoryDao = new FavoriteDataHistoryDaoImpl();
	private FavoriteDataDao favoriteDataDao = new FavoriteDataDaoImpl();
	private FavoriteDataTagDao dataTagDao = new FavoriteDataTagDaoImpl();

	@Override
	public void clip(FavoriteDataDto favoriteDataDto) {
		this.favoriteDataDao.save(favoriteDataDto);
	}

	@Override
	public void unclip(String userId, String dataId) {
		FavoriteDataDto favoritedataKeyDto = FavoriteDataDto.builder().userId(userId).dataId(dataId).build();
		FavoriteDataDto favoriteDataDto = favoriteDataDao.load(favoritedataKeyDto);
		List<String> tagIds = dataTagDao.getTagIdsForData(userId, dataId, true);
		List<FavoriteDataTagDto> dataTagKeyDtos = tagIds.stream()
				.map(tagId -> FavoriteDataTagDto.builder().userId(userId).dataId(dataId).tagId(tagId).build())
				.collect(Collectors.toList());
		List<FavoriteDataTagDto> dataTagDtos = dataTagDao.batchLoad(dataTagKeyDtos);
		long ttl = new Date().getTime() / 1000 + 60;
		favoriteDataDao.delete(favoritedataKeyDto);
		FavoriteDataHistoryDto historyDto = FavoriteDataHistoryDto.builder().userId(favoriteDataDto.getUserId())
				.dataId(favoriteDataDto.getDataId()).dataType(favoriteDataDto.getDataType())
				.creatorId(favoriteDataDto.getCreatorId()).title(favoriteDataDto.getTitle())
				.contentUrl(favoriteDataDto.getContentUrl()).thumbnailUrl(favoriteDataDto.getThumbnailUrl())
				.clipTime(favoriteDataDto.getClipTime()).ttl(ttl).build();
		favoriteDataHistoryDao.save(historyDto);
		dataTagDtos.forEach(dataTagDto -> dataTagDto.setTtl(ttl));
		dataTagDao.batchSave(dataTagDtos);
	}

	@Override
	public void undoUnclip(String userId, String dataId) {
		FavoriteDataHistoryDto historyKeyDto = FavoriteDataHistoryDto.builder().userId(userId).dataId(dataId).build();
		FavoriteDataHistoryDto historyDto = favoriteDataHistoryDao.load(historyKeyDto);
		List<String> tagIds = dataTagDao.getTagIdsForData(userId, dataId, true);
		List<FavoriteDataTagDto> dataTagKeyDtos = tagIds.stream()
				.map(tagId -> FavoriteDataTagDto.builder().userId(userId).dataId(dataId).tagId(tagId).build())
				.collect(Collectors.toList());
		List<FavoriteDataTagDto> dataTagDtos = dataTagDao.batchLoad(dataTagKeyDtos);
		favoriteDataHistoryDao.delete(historyKeyDto);
		FavoriteDataDto favoriteDataDto = FavoriteDataDto.builder().userId(historyDto.getUserId())
				.dataId(historyDto.getDataId()).dataType(historyDto.getDataType()).creatorId(historyDto.getCreatorId())
				.title(historyDto.getTitle()).contentUrl(historyDto.getContentUrl())
				.thumbnailUrl(historyDto.getThumbnailUrl()).clipTime(historyDto.getClipTime()).build();
		favoriteDataDao.save(favoriteDataDto);
		dataTagDtos.forEach(dataTagDto -> dataTagDto.setTtl(null));
		dataTagDao.batchSave(dataTagDtos);
	}

}
