package com.jessica.social.network.serverless.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.jessica.social.network.serverless.dao.BasicOperationDao;
import com.jessica.social.network.serverless.item.BasicItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BasicOperationDaoImpl<T extends BasicItem> implements BasicOperationDao<T> {
    @Autowired
    private DynamoDBMapper mapper;

    @Override
    public void save(T item) {
        if (item == null) {
            return;
        }
        this.mapper.save(item);
    }

    @Override
    public void batchSave(List<T> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        this.mapper.batchSave(items);
    }

    @Override
    public void delete(T keyItem) {
        if (keyItem == null) {
            return;
        }
        this.mapper.delete(keyItem);
    }

    @Override
    public void batchDelete(List<T> keyItems) {
        if (CollectionUtils.isEmpty(keyItems)) {
            return;
        }
        this.mapper.batchDelete(keyItems);
    }

    @Override
    public void update(T item) {
        if (item == null) {
            return;
        }
        this.mapper.save(item);
    }

    @Override
    public void batchUpdate(List<T> items) {

        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        this.mapper.batchSave(items);
    }

    @Override
    public T load(T keyItem) {
        return this.mapper.load(keyItem);
    }

    @Override
    public List<T> batchLoad(List<T> keyItems) {
        return this.mapper.load(keyItems);
    }
}
