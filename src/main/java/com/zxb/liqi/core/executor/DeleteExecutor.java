package com.zxb.liqi.core.executor;

import com.zxb.liqi.interfaces.BaseExecutor;
import com.zxb.liqi.interfaces.BaseParser;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Mr.M
 * @date 2023/6/13
 * @Description
 */
public class DeleteExecutor implements BaseExecutor {
    private final BaseParser parser;
    private final Class<?> entityClass;
    private final PreparedStatement preparedStatement;

    public DeleteExecutor(BaseParser parser, Class<?> entityClass, PreparedStatement preparedStatement) {
        this.entityClass = entityClass;
        this.parser = parser;
        this.preparedStatement = preparedStatement;
    }
    @Override
    public Object executor() {
        try {
            int mysqlAffectedRows = this.preparedStatement.executeUpdate();
            if (mysqlAffectedRows == 0) {
                throw new SQLException("The affected rows is zero, the delete operation failure");
            }
            return mysqlAffectedRows;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
    }
}
