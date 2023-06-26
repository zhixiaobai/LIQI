package com.zxb.liqi.core.executor;

import com.zxb.liqi.core.MultiDataSource;
import com.zxb.liqi.core.context.DbContext;
import com.zxb.liqi.interfaces.BaseExecutor;
import com.zxb.liqi.interfaces.BaseHandler;
import com.zxb.liqi.interfaces.BaseParser;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.Objects;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 默认执行器
 */
public class DefaultExecutor implements BaseExecutor {
    private final BaseHandler handler;
    private final Class<?> entityClass;
    private Connection connection;

    public DefaultExecutor(BaseHandler handler, Class<?> entityClass) {
        this.handler = handler;
        this.entityClass = entityClass;
        try {
            this.connection = MultiDataSource.DATA_SOURCE_MAP
                    .get(DbContext.getCurDb()).getConnection();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public Object executor() {
        PreparedStatement preparedStatement = null;
        try {
            String prepareSql = this.handler.getPrepareSql();
            preparedStatement = this.connection.prepareStatement(prepareSql);
            Class<?> typeClass = this.handler.getBaseParser().getOperationType().getTypeClass();
            BaseExecutor baseExecutor = null;
            try {
                Constructor<?> constructor = typeClass.getConstructor(BaseHandler.class, Class.class, Connection.class, PreparedStatement.class);
                baseExecutor = (BaseExecutor) constructor.newInstance(this.handler, entityClass, this.connection, preparedStatement);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Objects.isNull(baseExecutor)) {
                throw new RuntimeException(typeClass + " creating constructor error");
            }
            return baseExecutor.executor();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        } finally {
            try {
                if (Objects.nonNull(preparedStatement)) {
                    preparedStatement.close();
                }
                if (Objects.nonNull(this.connection)) {
                    this.connection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
