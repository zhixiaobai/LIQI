package com.zxb.liqi.core.handler;

import com.zxb.liqi.core.executor.DeleteExecutor;
import com.zxb.liqi.core.executor.InsertExecutor;
import com.zxb.liqi.core.executor.SelectExecutor;
import com.zxb.liqi.core.executor.UpdateExecutor;
import com.zxb.liqi.enums.SqlOperationType;
import com.zxb.liqi.interfaces.BaseHandler;
import com.zxb.liqi.interfaces.BaseParser;

/**
 * @author Mr.M
 * @date 2023/6/15
 * @Description 统一处理器
 */
public class UnifiedHandler {

    public UnifiedHandler() {

    }

    public static BaseHandler getHandler(BaseParser parser) {
        SqlOperationType operationType = parser.getOperationType();
        if (operationType.getTypeClass() == SelectExecutor.class) {
            return new SelectHandler(parser);
        } else if (operationType.getTypeClass() == InsertExecutor.class) {
            return new InsertHandler(parser);
        } else if (operationType.getTypeClass() == UpdateExecutor.class) {
            return new SelectHandler(parser);
        } else if (operationType.getTypeClass() == DeleteExecutor.class) {
            return new SelectHandler(parser);
        } else {
            throw new RuntimeException("");
        }
    }
}
