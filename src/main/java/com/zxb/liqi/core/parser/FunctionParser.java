package com.zxb.liqi.core.parser;

import com.zxb.liqi.annotation.TableName;
import com.zxb.liqi.core.cache.Cache;
import com.zxb.liqi.core.executor.DeleteExecutor;
import com.zxb.liqi.core.executor.InsertExecutor;
import com.zxb.liqi.core.executor.SelectExecutor;
import com.zxb.liqi.core.executor.UpdateExecutor;
import com.zxb.liqi.core.table.Table;
import com.zxb.liqi.enums.SqlOperationType;
import com.zxb.liqi.interfaces.BaseParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description 方法解析器
 */
public class FunctionParser<T> implements BaseParser {

    private Table<T> table;
    private String parseSql;
    private final Class<T> entityClass;
    private T entity;
    private final SqlOperationType sqlOperationType;
    private final Map<String, String> entityParamMap;

    public FunctionParser(Table<T> table) {
        this.table = table;
        this.sqlOperationType = table.getSqlOperationType();
        this.entityClass = table.getTableEntityClass();
        this.entityParamMap = Cache.lruCache.get(entityClass);
        this.parseSqlByTable();
    }

    public FunctionParser(T entity, SqlOperationType sqlOperationType) {
        this.entity = entity;
        this.entityClass = (Class<T>) entity.getClass();
        this.sqlOperationType = sqlOperationType;
        this.entityParamMap = Cache.lruCache.get(entityClass);
        this.parseSqlByTable();
    }

    private void parseSqlByTable() {
        String operationFormat = this.sqlOperationType.getOperationFormat();
        Class<?> typeClass = this.sqlOperationType.getTypeClass();
        String tableName = this.getTableName();
        if (typeClass == SelectExecutor.class) {
            String selectFields = this.table.getSelectFields();
            String sqlStatements = this.table.getSqlStatements();
            sqlStatements = sqlStatements + " " + this.table.getGroupBy()
                    + " " + this.table.getOrderByAsc()
                    + " " + this.table.getOrderByDesc()
                    + " " + this.table.getLastSqlStatement();
            this.parseSql = String.format(operationFormat, selectFields, tableName, sqlStatements);
        } else if (typeClass == InsertExecutor.class) {
            StringJoiner val = new StringJoiner(",");
            StringJoiner placeholder = new StringJoiner(",");
            for (String value: this.entityParamMap.values()) {
                val.add(value);
                placeholder.add("#{" + value + "}");
            }
            this.parseSql = String.format(operationFormat, tableName, val, placeholder);
        } else if (typeClass == UpdateExecutor.class) {

        } else if (typeClass == DeleteExecutor.class) {

        } else {
            throw new RuntimeException("");
        }
    }

    /**
     * 获取表名
     * @return tableName
     */
    private String getTableName() {
        TableName tableName = this.entityClass.getAnnotation(TableName.class);
        if (Objects.isNull(tableName)) {
            throw new RuntimeException("TableName no exist");
        }
        return tableName.value();
    }

    @Override
    public String getParseSql() {
        return this.parseSql;
    }

    @Override
    public SqlOperationType getOperationType() {
        return this.sqlOperationType;
    }

    @Override
    public Class<?> getMethodReturnType() {
        return this.entityClass;
    }

    @Override
    public Map<String, Object> getParamMap() {
        return this.table.getParamMap();
    }

    @Override
    public int getBatchSize() {
        return 0;
    }
}
