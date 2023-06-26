package com.zxb.liqi.core.table;

import com.zxb.liqi.core.cache.Cache;
import com.zxb.liqi.enums.ConditionalOperator;
import com.zxb.liqi.enums.LogicalOperator;
import com.zxb.liqi.enums.SqlKeyWordEnum;
import com.zxb.liqi.enums.SqlOperationType;
import com.zxb.liqi.interfaces.IGetter;
import com.zxb.liqi.utils.BeanUtil;
import com.zxb.liqi.utils.StringUtil;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 表对象
 */
public class Table<T> {
    private Class<T> tableEntityClass;

    private String selectFields = "*";

    private SqlOperationType sqlOperationType;

    private final LinkedHashMap<String, Object> paramMap = new LinkedHashMap<>();

    private final List<String> sqlStatements = new CopyOnWriteArrayList<>();

    private String groupBy = "";

    private String orderByDesc = "";

    private String orderByAsc = "";

    private String lastSqlStatement = "";

    private Map<String, String> entityParamFieldMap;

    public Table<T> from(Class<T> tableEntityClass) {
        this.tableEntityClass = tableEntityClass;
        this.entityParamFieldMap = Cache.lruCache.get(tableEntityClass);
        if (Objects.isNull(this.entityParamFieldMap)) {
            throw new RuntimeException("稍后处理");
        }
        return this;
    }

    @SafeVarargs
    public final Table<T> select(IGetter<T>... columns) {
        this.selectFields = Arrays.stream(columns).map((column) -> {
            String fieldName = BeanUtil.convertToFieldName(column);
            String columnName = this.entityParamFieldMap.get(fieldName);
            if (Objects.isNull(columnName)) {
                return fieldName;
            }
            return columnName;
        }).collect(Collectors.joining(","));
        return this;
    }

    public Table<T> or() {
        this.sqlStatements.add(LogicalOperator.OR.getLogicalOperator());
        return this;
    }

    public Table<T> or(Function<Table<T>, Table<T>> function) {
        Table<T> table = function.apply(new Table<>());
        String sqlStatement = table.getSqlStatements();
        this.sqlStatements.add(LogicalOperator.OR.getLogicalOperator());
        this.sqlStatements.add(sqlStatement);
        return this;
    }

    public Table<T> and() {
        this.sqlStatements.add(LogicalOperator.AND.getLogicalOperator());
        return this;
    }

    public Table<T> and(Function<Table<T>, Table<T>> function) {
        Table<T> table = function.apply(new Table<>());
        String sqlStatement = table.getSqlStatements();
        this.sqlStatements.add(LogicalOperator.AND.getLogicalOperator());
        this.sqlStatements.add(sqlStatement);
        return this;
    }

    public Table<T> conditionEq(IGetter<T> column, Object val) {
        return this.condition(true, column, ConditionalOperator.EQ, val);
    }

    public Table<T> condition(IGetter<T> column, ConditionalOperator condition, Object val) {
        return this.condition(true, column, condition, val);
    }

    public Table<T> condition(boolean isCondition, IGetter<T> column, ConditionalOperator condition, Object val) {
        if (isCondition) {
            String fieldName = BeanUtil.convertToFieldName(column);
            String columnName = this.entityParamFieldMap.get(fieldName);
            if (Objects.isNull(columnName)) {
                this.sqlStatements.add(StringUtil.spliceCondition(fieldName, fieldName, condition));
            } else {
                this.sqlStatements.add(StringUtil.spliceCondition(columnName, fieldName, condition));
            }
            this.paramMap.put(fieldName, val);
        }
        return this;
    }

    public Table<T> setUpdate(IGetter<T> column) {
        this.setUpdate(true, column);
        return this;
    }

    public Table<T> setUpdate(boolean isCondition, IGetter<T> column) {
        return this;
    }

    public Table<T> in(IGetter<T> column, List<?> list) {
        return this.in(true, column, list);
    }

    public Table<T> in(boolean isCondition, IGetter<T> column, List<?> list) {
        if (isCondition) {
            String columnName = BeanUtil.convertToFieldName(column);
            columnName = this.entityParamFieldMap.get(columnName);
            this.sqlStatements.add(StringUtil.spliceConditionIn(columnName, list, SqlKeyWordEnum.IN));
        }
        return this;
    }

    public Table<T> in(IGetter<T> column, Object... contents) {
        return this.in(true, column, contents);
    }

    public Table<T> in(boolean isCondition, IGetter<T> column, Object... contents) {
        if (isCondition) {
            String columnName = BeanUtil.convertToFieldName(column);
            columnName = this.entityParamFieldMap.get(columnName);
            this.sqlStatements.add(StringUtil.spliceConditionIn(columnName, Arrays.asList(contents), SqlKeyWordEnum.IN));
        }
        return this;
    }

    public Table<T> like(IGetter<T> column, String likeContent) {
        return this.like(true, column, likeContent);
    }

    public Table<T> like(boolean isCondition, IGetter<T> column, String likeContent) {
        if (isCondition) {
            String fieldName = BeanUtil.convertToFieldName(column);
            String columnName = this.entityParamFieldMap.get(fieldName);
            this.sqlStatements.add(StringUtil.spliceCondition(columnName, fieldName, SqlKeyWordEnum.LIKE));
            this.paramMap.put(fieldName, likeContent);
        }
        return this;
    }

    @SafeVarargs
    public final Table<T> groupBy(IGetter<T>... columns) {
        this.groupBy = SqlKeyWordEnum.GROUP_BY.getSqlKeyWord() +
                Arrays.stream(columns).map((column) -> {
                    String fieldName = BeanUtil.convertToFieldName(column);
                    String columnName = this.entityParamFieldMap.get(fieldName);
                    if (Objects.isNull(columnName)) {
                        return fieldName;
                    }
                    return columnName;
                }).collect(Collectors.joining(","));
        return this;
    }

    @SafeVarargs
    public final Table<T> orderByDesc(IGetter<T>... columns) {
        this.orderByDesc = SqlKeyWordEnum.ORDER_BY_DESC.getSqlKeyWord() +
                Arrays.stream(columns).map((column) -> {
                    String fieldName = BeanUtil.convertToFieldName(column);
                    String columnName = this.entityParamFieldMap.get(fieldName);
                    if (Objects.isNull(columnName)) {
                        return fieldName;
                    }
                    return columnName;
                }).collect(Collectors.joining(","));
        return this;
    }

    @SafeVarargs
    public final Table<T> orderByAsc(IGetter<T>... columns) {
        this.orderByAsc = SqlKeyWordEnum.ORDER_BY_ASC.getSqlKeyWord() +
                Arrays.stream(columns).map((column) -> {
                    String fieldName = BeanUtil.convertToFieldName(column);
                    String columnName = this.entityParamFieldMap.get(fieldName);
                    if (Objects.isNull(columnName)) {
                        return fieldName;
                    }
                    return columnName;
                }).collect(Collectors.joining(","));
        return this;
    }

    public Table<T> lastSql(String sqlStatement) {
        this.lastSqlStatement = sqlStatement;
        return this;
    }

    public void query() {
        this.sqlOperationType = SqlOperationType.SELECT;
    }

    public void insert() {
        this.sqlOperationType = SqlOperationType.INSERT;
    }

    public void update() {
        this.sqlOperationType = SqlOperationType.UPDATE;
    }

    public void delete() {
        this.sqlOperationType = SqlOperationType.DELETE;
    }

    public String getSelectFields() {
        return this.selectFields;
    }

    public Class<T> getTableEntityClass() {
        return this.tableEntityClass;
    }

    public SqlOperationType getSqlOperationType() {
        return this.sqlOperationType;
    }

    public String getSqlStatements() {
        StringBuilder sqlStatements = new StringBuilder();
        String or = LogicalOperator.OR.getLogicalOperator();
        String and = LogicalOperator.AND.getLogicalOperator();
        int size = this.sqlStatements.size();
        for (int i = 0; i < size; i++) {
            String sqlStatement = this.sqlStatements.get(i);
            sqlStatements.append(sqlStatement).append(" ");
            if (i != size - 1) {
                String nextSqlStatement = this.sqlStatements.get(i + 1);
                if (!sqlStatement.equals(or) && !sqlStatement.equals(and)
                        && !nextSqlStatement.equals(or) && !nextSqlStatement.equals(and)) {
                    sqlStatements.append(and).append(" ");
                }
            }
        }
        return "(" + sqlStatements.toString() + ")";
    }

    public LinkedHashMap<String, Object> getParamMap() {
        return this.paramMap;
    }

    public String getGroupBy() {
        return this.groupBy;
    }

    public String getOrderByDesc() {
        return this.orderByDesc;
    }

    public String getOrderByAsc() {
        return this.orderByAsc;
    }

    public String getLastSqlStatement() {
        return this.lastSqlStatement;
    }
}
