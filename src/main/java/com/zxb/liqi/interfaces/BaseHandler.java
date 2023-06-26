package com.zxb.liqi.interfaces;

import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/6/15
 * @Description
 */
public interface BaseHandler {
    /**
     * 处理方法
     */
    void handle();

    /**
     * 获取真实数据
     * @return Map<String, Object>
     */
    Map<String, Object> getRealDataMap();

    /**
     * 获取预加载sql
     * @return String
     */
    String getPrepareSql();

    /**
     * 获取解析器
     * @return BaseParser
     */
    BaseParser getBaseParser();

    /**
     * 获取真实sql数量
     * @return int
     */
    int getSqlCount();
}
