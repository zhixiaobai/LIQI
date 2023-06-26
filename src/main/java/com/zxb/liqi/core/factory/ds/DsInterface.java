package com.zxb.liqi.core.factory.ds;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/3/7
 * @Description
 */
public interface DsInterface {
    /**
     * 构造数据源
     * @param dsName
     * @param map
     * @return
     */
    DataSource buildDs(String dsName, Map<String, String> map);
}
