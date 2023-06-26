package com.zxb.liqi.enums;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description 逻辑运算符
 */
public enum LogicalOperator {
    /**
     * 逻辑运算符
     */
    OR("OR"),
    AND("AND");

    private final String logicalOperator;

    LogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }
}
