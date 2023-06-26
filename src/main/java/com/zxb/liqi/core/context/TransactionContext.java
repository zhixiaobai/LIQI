package com.zxb.liqi.core.context;

/**
 * @author Mr.M
 * @date 2023/3/6
 * @Description
 */
public final class TransactionContext {
    private static final ThreadLocal<Boolean>
            TRANSACTION_SWITCH_CONTEXT = ThreadLocal.withInitial(() -> false);

    /**
     * 开启事务
     */
    public static void openTransaction() {
        TRANSACTION_SWITCH_CONTEXT.set(true);
    }

    /**
     * 关闭事务
     */
    public static void closeTransaction() {
        TRANSACTION_SWITCH_CONTEXT.set(false);
    }

    /**
     * 判断事务是否开启
     * @return boolean
     */
    public static Boolean isOpenTransaction() {
        return TRANSACTION_SWITCH_CONTEXT.get();
    }

    /**
     * 移除
     */
    public static void removeTransaction() {
        TRANSACTION_SWITCH_CONTEXT.remove();
    }
}
