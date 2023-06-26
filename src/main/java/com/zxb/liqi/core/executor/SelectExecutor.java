package com.zxb.liqi.core.executor;

import com.zxb.liqi.core.cache.Cache;
import com.zxb.liqi.interfaces.BaseExecutor;
import com.zxb.liqi.interfaces.BaseHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @author Mr.M
 * @date 2023/6/13
 * @Description select 执行器
 */
public class SelectExecutor implements BaseExecutor {
    private final PreparedStatement preparedStatement;
    private final Class<?> entityClass;
    private final BaseHandler handler;
    private final Connection connection;

    public SelectExecutor(BaseHandler handler, Class<?> entityClass, Connection connection, PreparedStatement preparedStatement) {
        this.handler = handler;
        this.entityClass = entityClass;
        this.connection = connection;
        this.preparedStatement = preparedStatement;
    }

    @Override
    public Object executor() {
        ResultSet resultSet = null;
        try {
            int parameterIndex = 1;
            for (Object obj : this.handler.getRealDataMap().values()) {
                preparedStatement.setObject(parameterIndex, obj);
                parameterIndex ++;
            }
            resultSet = preparedStatement.executeQuery();
            List<Object> objects = new LinkedList<>();
            Map<String, String> map = Cache.lruCache.get(this.entityClass);
            List<String> columnNames = new ArrayList<>();
            if (Objects.isNull(map)) {
                throw new RuntimeException("稍候处理");
            }
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames.add(columnName);
            }
            while (resultSet.next()) {
                Constructor<?> constructor = this.entityClass.getConstructor();
                constructor.setAccessible(true);
                Object entity = constructor.newInstance();
                Field[] declaredFields = this.entityClass.getDeclaredFields();
                for (Field field: declaredFields) {
                    field.setAccessible(true);
                    String fieldName = map.get(field.getName());
                    if (Objects.nonNull(fieldName) && columnNames.contains(fieldName)) {
                        field.set(entity, resultSet.getObject(fieldName));
                    }
                }
                objects.add(entity);
            }
            int size = objects.size();
            if (size == 0) { return null; }
            if (!this.handler.getBaseParser().getMethodReturnType().isAssignableFrom(List.class)) {
                if (size == 1) {
                    return objects.get(0);
                }
                throw new SQLException("The current SQL queries to the " + size + " results, But the method returns the parameter type error");
            }
            return objects;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.toString());
        } finally {
            try {
                if (Objects.nonNull(resultSet)) {
                    resultSet.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}