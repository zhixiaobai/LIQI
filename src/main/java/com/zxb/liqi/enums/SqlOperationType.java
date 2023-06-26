package com.zxb.liqi.enums;

import com.zxb.liqi.core.executor.DeleteExecutor;
import com.zxb.liqi.core.executor.InsertExecutor;
import com.zxb.liqi.core.executor.SelectExecutor;
import com.zxb.liqi.core.executor.UpdateExecutor;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description sql操作类型
 */
public enum SqlOperationType {
    /**
     *
     */
    SELECT("Select", "SELECT %s FROM %s WHERE %s;", SelectExecutor.class),
    INSERT("Insert", "INSERT INTO %s (%s) VALUES (%s);", InsertExecutor.class),
    UPDATE("Update", "UPDATE %s SET %s WHERE %s", UpdateExecutor.class),
    DELETE("Delete", "DELETE FROM %s WHERE %s;", DeleteExecutor.class),
    ;

    private final String operationType;

    private final String operationFormat;

    private final Class<?> typeClass;

    SqlOperationType(String operationType, String operationFormat, Class<?> typeClass) {
        this.operationType = operationType;
        this.operationFormat = operationFormat;
        this.typeClass = typeClass;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getOperationFormat() {
        return operationFormat;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }
}
