package com.zxb.liqi.core.executor;

import com.zxb.liqi.interfaces.BaseExecutor;
import com.zxb.liqi.interfaces.BaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mr.M
 * @date 2023/6/13
 * @Description
 */
public class InsertExecutor implements BaseExecutor {
    private final BaseHandler handler;
    private final Class<?> entityClass;
    private final PreparedStatement preparedStatement;
    private final Connection connection;

    public InsertExecutor(BaseHandler handler, Class<?> entityClass, Connection connection, PreparedStatement preparedStatement) {
        this.entityClass = entityClass;
        this.handler = handler;
        this.connection = connection;
        this.preparedStatement = preparedStatement;
    }

    private int[] mergeArrays(int[] target, int[] source) {
        return IntStream.concat(Arrays.stream(target), Arrays.stream(source))
                .toArray();
    }

    @Override
    public Object executor() {
        try {
            this.connection.setAutoCommit(false);
            int[] mysqlAffectedRows = new int[0];
            for (int i = 0; i < this.handler.getSqlCount(); i++) {
                int parameterIndex = 1;
                for (Object obj : this.handler.getRealDataMap().values()) {
                    if (obj instanceof List) {
                        preparedStatement.setObject(parameterIndex, ((List<?>) obj).get(i));
                    } else {
                        preparedStatement.setObject(parameterIndex, obj);
                    }
                    parameterIndex ++;
                }
                preparedStatement.addBatch();
                if (i % this.handler.getBaseParser().getBatchSize() == 0) {
                    mysqlAffectedRows = mergeArrays(mysqlAffectedRows, preparedStatement.executeBatch());
                    this.connection.commit();
                    preparedStatement.clearBatch();
                }
            }
            mysqlAffectedRows = mergeArrays(mysqlAffectedRows, preparedStatement.executeBatch());
            this.connection.commit();
            int mysqlAffectedRow = 0;
            for (int row: mysqlAffectedRows) {
                if (row >= 0) {
                    mysqlAffectedRow = mysqlAffectedRow + row;
                }
                if (row == -2) {
                    mysqlAffectedRow = mysqlAffectedRow + 1;
                }
            }
            return mysqlAffectedRow;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}