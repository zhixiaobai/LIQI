package com.zxb.liqi.core.factory.ds;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/3/7
 * @Description
 */
public class DsFactory {
    private final static Map<String, DsInterface> dsInterfaceMap = new HashMap<>();

    static {
        dsInterfaceMap.put("druid", new DruidDsFactory());
        dsInterfaceMap.put("hikari", new HikariDsFactory());
    }

    public static DsInterface getDs(String type) {
        return dsInterfaceMap.get(type);
    }
}
