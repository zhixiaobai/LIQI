package com.zxb.liqi.core;

import com.zxb.liqi.config.DbConfig;
import com.zxb.liqi.core.connection.ZmConnection;
import com.zxb.liqi.core.context.DbContext;
import com.zxb.liqi.core.context.TransactionContext;
import com.zxb.liqi.exception.ConnectionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.M
 * @date 2023/3/6
 * @Description
 */
public class MultiDataSource extends AbstractDataSource implements InitializingBean {
    /**
     * 存储多个数据源
     */
    public static final Map<String, DataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    // 初始化
    /**
     * 当前事务 连接
     */
    public static final ThreadLocal<List<ZmConnection>>
            MULTI_TRAN_CONNECTION = ThreadLocal.withInitial(ArrayList::new);


    /**
     * 默认数据源
     */
    private DataSource defaultTargetDataSource;

    @Resource
    private DbConfig dbConfig;


    @Override
    public Connection getConnection() {
        try {
            ZmConnection zmConnection = new ZmConnection(getDataSource().getConnection());
            if (TransactionContext.isOpenTransaction()) {
                zmConnection.setAutoCommit(false);
                MULTI_TRAN_CONNECTION.get().add(zmConnection);
            }
            return zmConnection;
        } catch (Exception e) {
            throw new ConnectionException("Get connection error occurs, reason is " + e.toString());
        }

    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        ZmConnection zmConnection = new ZmConnection(getDataSource().getConnection(username, password));
        if (TransactionContext.isOpenTransaction()) {
            zmConnection.setAutoCommit(false);
            MULTI_TRAN_CONNECTION.get().add(zmConnection);
        }
        return zmConnection;
    }

    protected DataSource getDataSource() {
        DataSource dataSource;
        String dbName = DbContext.getCurDb();
        if ((dataSource = DATA_SOURCE_MAP.get(dbName)) == null) {
            synchronized (this) {
                if (DATA_SOURCE_MAP.get(dbName) == null) {
                    dataSource = dbConfig.buildDataSource(dbName);
                    DATA_SOURCE_MAP.put(dbName, dataSource);
                }
            }
        }
        return dataSource;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return getDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || getDataSource().isWrapperFor(iface));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DATA_SOURCE_MAP.put("main", defaultTargetDataSource);
    }

    public void setDefaultTargetDataSource(DataSource defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }
}
