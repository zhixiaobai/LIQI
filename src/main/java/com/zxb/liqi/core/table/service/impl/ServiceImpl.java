package com.zxb.liqi.core.table.service.impl;

import com.zxb.liqi.core.executor.DefaultExecutor;
import com.zxb.liqi.core.parser.FunctionParser;
import com.zxb.liqi.core.table.Table;
import com.zxb.liqi.core.table.service.TableService;
import com.zxb.liqi.enums.SqlOperationType;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description function 实现类
 */
public class ServiceImpl<T> implements TableService<T> {

    @Override
    public T getOne(Table<T> table) {
        FunctionParser<T> functionParser = new FunctionParser<>(table);
        return null;
//        DefaultExecutor defaultExecutor = new DefaultExecutor(functionParser, table.getTableEntityClass());
//        return (T) defaultExecutor.executor();
    }

    @Override
    public T getById(Serializable id) {
        return null;
    }

    @Override
    public List<T> list(Table<T> table) {
        return null;
    }

    @Override
    public int update(Table<T> table) {
        return 0;
    }

    @Override
    public int updateById(T t) {
        return 0;
    }

    @Override
    public int updateBatch(Table<T> table, List<T> batchList) {
        return 0;
    }

    @Override
    public int save(Table<T> table) {
        return 0;
    }

    @Override
    public int save(T t) {
        FunctionParser<T> functionParser = new FunctionParser<>(t, SqlOperationType.INSERT);

        return 0;
    }

    @Override
    public int saveBatch(List<T> batchList) {
        return 0;
    }

    @Override
    public int remove(Table<T> table) {
        return 0;
    }

    @Override
    public int removeById(T t) {
        return 0;
    }
}
