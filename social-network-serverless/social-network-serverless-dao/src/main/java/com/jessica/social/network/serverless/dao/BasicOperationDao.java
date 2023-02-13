package com.jessica.social.network.serverless.dao;

import com.jessica.social.network.serverless.item.BasicItem;

import java.util.List;

public interface BasicOperationDao<T extends BasicItem> {
    /**
     *
     * @param item
     */
    void save(T item);

    /**
     *
     * @param items
     */
    void batchSave(List<T> items);

    /**
     *
     * @param keyItem
     */
    void delete(T keyItem);

    /**
     *
     * @param keyItems
     */
    void batchDelete(List<T> keyItems);

    /**
     *
     * @param item
     */
    void update(T item);

    /**
     *
     * @param items
     */
    void batchUpdate(List<T> items);

    /**
     *
     * @param keyItem
     * @return
     */
    T load(T keyItem);

    /**
     *
     * @param keyItems
     * @return
     */
    List<T> batchLoad(List<T> keyItems);
}
