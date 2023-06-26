package com.zxb.liqi.core.context;

/**
 * @author Mr.M
 * @date 2023/3/6
 * @Description
 */
public final class DbContext {
    private static final ThreadLocal<String> CUR_DB = ThreadLocal.withInitial(() -> "main");

    public static void switchDb(String dbName) {
        CUR_DB.set(dbName);
    }

    public static String getCurDb() {
        return CUR_DB.get();
    }

    public static void resetDb() {
        CUR_DB.set("main");
    }

    private static void remove() {
        CUR_DB.remove();
    }
}
