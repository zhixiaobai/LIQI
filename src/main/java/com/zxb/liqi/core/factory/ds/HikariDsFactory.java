package com.zxb.liqi.core.factory.ds;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/3/7
 * @Description
 */
public class HikariDsFactory implements DsInterface {
    @Override
    public DataSource buildDs(String dsName, Map<String, String> map) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(map.get("url"));
        dataSource.setUsername(map.get("username"));
        dataSource.setPassword(map.get("password"));
        dataSource.setDriverClassName(map.get("driverClass"));
        return dataSource;
    }
}
