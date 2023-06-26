package com.zxb.liqi.core.parser;

import com.zxb.liqi.enums.SqlOperationType;
import com.zxb.liqi.interfaces.BaseParser;

import java.util.List;
import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
public class DefaultParser implements BaseParser {

    @Override
    public String getParseSql() {
        return null;
    }

    @Override
    public SqlOperationType getOperationType() {
        return null;
    }

    @Override
    public Class<?> getMethodReturnType() {
        return null;
    }

    @Override
    public Map<String, Object> getParamMap() {
        return null;
    }

    @Override
    public int getBatchSize() {
        return 0;
    }
}
