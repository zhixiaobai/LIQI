package com.zxb.liqi.core.table.service;

import com.zxb.liqi.core.table.Table;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description function
 */
public interface TableService<T> {
    /**
     *
     * @param table
     * @return
     */
    T getOne(Table<T> table);

    /**
     *
     * @param id
     * @return
     */
    T getById(Serializable id);

    /**
     *
     * @param table
     * @return
     */
    List<T> list(Table<T> table);

    /**
     *
     * @param table
     * @return
     */
    int update(Table<T> table);

    /**
     *
     * @param t
     * @return
     */
    int updateById(T t);

    /**
     *
     * @param table
     * @param batchList
     * @return
     */
    int updateBatch(Table<T> table, List<T> batchList);

    /**
     *
     * @param table
     * @return
     */
    int save(Table<T> table);

    /**
     *
     * @param t
     * @return
     */
    int save(T t);

    /**
     *
     * @param batchList
     * @return
     */
    int saveBatch(List<T> batchList);

    /**
     *
     * @param table
     * @return
     */
    int remove(Table<T> table);

    /**
     *
     * @param t
     * @return
     */
    int removeById(T t);
}
