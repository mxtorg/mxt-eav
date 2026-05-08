package com.mxt.eav.model;

/**
 * 属性数据类型枚举
 * 定义EAV模型支持的所有数据类型
 */
public enum AttributeType {
    
    /**
     * 字符串类型 */
    STRING,
    
    /**
     * 整数类型 */
    INTEGER,
    
    /**
     * 小数类型 */
    DECIMAL,
    
    /**
     * 布尔类型 */
    BOOLEAN,
    
    /**
     * 日期类型 (yyyy-MM-dd) */
    DATE,
    
    /**
     * 日期时间类型 (yyyy-MM-dd HH:mm:ss) */
    DATETIME
}
