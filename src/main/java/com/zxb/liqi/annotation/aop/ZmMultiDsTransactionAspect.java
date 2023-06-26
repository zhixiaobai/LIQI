package com.zxb.liqi.annotation.aop;

import com.zxb.liqi.core.MultiDataSource;
import com.zxb.liqi.core.connection.ZmConnection;
import com.zxb.liqi.core.context.TransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Mr.M
 * @date 2023/3/7
 * @Description
 */
@Aspect
public class ZmMultiDsTransactionAspect {
    @Pointcut("@annotation(com.zxb.liqi.annotation.ZmMultiDsTransaction)")
    public void transactPoint() {}

    @Around("transactPoint()")
    public Object multiTranAop(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开启事务
        TransactionContext.openTransaction();
        try {
            // 执行业务
            Object proceed = joinPoint.proceed();
            // 提交事务
            for (ZmConnection connection : MultiDataSource.MULTI_TRAN_CONNECTION.get()) {
                connection.commitMultiDbTran();
                connection.closeMultiDbTran();
            }
            return proceed;
        } catch (Throwable t) {
            t.printStackTrace();
            for (ZmConnection connection : MultiDataSource.MULTI_TRAN_CONNECTION.get()) {
                // 事务回滚
                connection.rollback();
                connection.closeMultiDbTran();
            }
            throw t;
        } finally {
            // 清空 事务 连接，关闭当前事务
            MultiDataSource.MULTI_TRAN_CONNECTION.get().clear();
            TransactionContext.closeTransaction();
        }
    }
}
