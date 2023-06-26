package com.zxb.liqi.config;

import com.zxb.liqi.core.MultiDataSource;
import com.zxb.liqi.core.factory.ds.DsFactory;
import com.zxb.liqi.core.factory.ds.DsInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/3/6
 * @Description
 */
public class DbConfig {

    private final Environment environment;

    @Value("${ds.enable}")
    private boolean isEnable;

    public DbConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    public DataSource buildMainDataSource() {
        MultiDataSource multiDataSource = new MultiDataSource();
        if (isEnable) {
            multiDataSource.setDefaultTargetDataSource(buildDataSource("main"));
        } else {
            multiDataSource.setDefaultTargetDataSource(buildDataSource(""));
        }
        return multiDataSource;
    }

    public DataSource buildDataSource(String dsName) {
        Map<String, String> map = new HashMap<>(4);
        dsName = "".equals(dsName) ? dsName : dsName + ".";
        map.put("url", environment.getProperty("spring.datasource." + dsName + "url"));
        map.put("username", environment.getProperty("spring.datasource." + dsName + "username"));
        map.put("password", environment.getProperty("spring.datasource." + dsName + "password"));
        map.put("driverClass", environment.getProperty("spring.datasource." + dsName + "driver-class-name"));
        String type = environment.getProperty("ds.pool.type");
        DsInterface dsInterface = DsFactory.getDs(type);
        return dsInterface.buildDs(dsName, map);
    }
}
