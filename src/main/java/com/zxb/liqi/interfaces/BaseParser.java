package com.zxb.liqi.interfaces;

import com.zxb.liqi.enums.SqlOperationType;

import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 基础解析器
 */
public interface BaseParser {
    /**
     * 获取解析结果
     * @return sql
     */
    String getParseSql();

    /**
     * 获取操作类型
     * @return SqlOperationType
     */
    SqlOperationType getOperationType();

    /**
     * 获取方法返回类型
     * @return Type
     */
    Class<?> getMethodReturnType();

    /**
     * 获取传入参数
     * @return Map<String, Object>
     */
    Map<String, Object> getParamMap();

    /**
     * 获取批量条数
     * @return int
     */
    int getBatchSize();

    /**
     * 获取传入参数 (List)
     * @return Map<String, Map<Class<?>, Object>>
     */
//    Map<String, Map<Class<?>, Object>> getParamListMap();
//    Map<String, List<?>> getParamListMap();

    /**
     * 判断是否是批量操作
     * @return boolean
     */
//    boolean isBatch();
}