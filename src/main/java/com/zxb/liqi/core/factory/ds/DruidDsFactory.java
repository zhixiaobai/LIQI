package com.zxb.liqi.core.factory.ds;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/3/7
 * @Description
 */
public class DruidDsFactory implements DsInterface{

    @Override
    public DataSource buildDs(String dsName, Map<String, String> map) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(map.get("url"));
        dataSource.setUsername(map.get("username"));
        dataSource.setPassword(map.get("password"));
        dataSource.setDriverClassName(map.get("driverClass"));
        return dataSource;
    }
}
