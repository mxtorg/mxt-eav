package com.mxt.eav.service;

import com.mxt.eav.exception.EavValidationException;
import com.mxt.eav.model.Attribute;
import com.mxt.eav.model.AttributeType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 属性验证服务
 * 负责验证属性值的类型和约束
 */
@Service
public class AttributeValidationService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 验证属性值
     * @param attribute 属性定义
     * @param value 属性值
     */
    public void validate(Attribute attribute, Object value) {
        if (value == null) {
            if (Boolean.TRUE.equals(attribute.getRequired())) {
                throw new EavValidationException(
                    String.format("属性[%s]是必填项", attribute.getName())
                );
            }
            return;
        }
        
        validateType(attribute, value);
        validateConstraints(attribute, value);
    }
    
    /**
     * 验证数据类型
     */
    private void validateType(Attribute attribute, Object value) {
        AttributeType type = attribute.getDataType();
        
        try {
            switch (type) {
                case STRING:
                    validateString(attribute, value);
                    break;
                case INTEGER:
                    validateInteger(attribute, value);
                    break;
                case DECIMAL:
                    validateDecimal(attribute, value);
                    break;
                case BOOLEAN:
                    validateBoolean(attribute, value);
                    break;
                case DATE:
                    validateDate(attribute, value);
                    break;
                case DATETIME:
                    validateDateTime(attribute, value);
                    break;
            }
        } catch (Exception e) {
            throw new EavValidationException(
                String.format("属性[%s]类型验证失败，期望类型: %s", attribute.getName(), type),
                e
            );
        }
    }
    
    /**
     * 验证字符串类型
     */
    private void validateString(Attribute attribute, Object value) {
        if (!(value instanceof String)) {
            throw new EavValidationException(
                String.format("属性[%s]期望字符串类型", attribute.getName())
            );
        }
    }
    
    /**
     * 验证整数类型
     */
    private void validateInteger(Attribute attribute, Object value) {
        if (value instanceof Integer || value instanceof Long) {
            return;
        }
        if (value instanceof String) {
            try {
                Long.parseLong((String) value);
                return;
            } catch (NumberFormatException e) {
                // 继续抛出异常
            }
        }
        throw new EavValidationException(
            String.format("属性[%s]期望整数类型", attribute.getName())
        );
    }
    
    /**
     * 验证小数类型
     */
    private void validateDecimal(Attribute attribute, Object value) {
        if (value instanceof Number) {
            return;
        }
        if (value instanceof String) {
            try {
                new BigDecimal((String) value);
                return;
            } catch (NumberFormatException e) {
                // 继续抛出异常
            }
        }
        throw new EavValidationException(
            String.format("属性[%s]期望数值类型", attribute.getName())
        );
    }
    
    /**
     * 验证布尔类型
     */
    private void validateBoolean(Attribute attribute, Object value) {
        if (value instanceof Boolean) {
            return;
        }
        if (value instanceof String) {
            String str = ((String) value).toLowerCase();
            if ("true".equals(str) || "false".equals(str)) {
                return;
            }
        }
        throw new EavValidationException(
            String.format("属性[%s]期望布尔类型", attribute.getName())
        );
    }
    
    /**
     * 验证日期类型
     */
    private void validateDate(Attribute attribute, Object value) {
        if (value instanceof LocalDate) {
            return;
        }
        if (value instanceof String) {
            try {
                LocalDate.parse((String) value, DATE_FORMATTER);
                return;
            } catch (DateTimeParseException e) {
                // 继续抛出异常
            }
        }
        throw new EavValidationException(
            String.format("属性[%s]期望日期类型(yyyy-MM-dd)", attribute.getName())
        );
    }
    
    /**
     * 验证日期时间类型
     */
    private void validateDateTime(Attribute attribute, Object value) {
        if (value instanceof LocalDateTime) {
            return;
        }
        if (value instanceof String) {
            try {
                LocalDateTime.parse((String) value, DATETIME_FORMATTER);
                return;
            } catch (DateTimeParseException e) {
                // 继续抛出异常
            }
        }
        throw new EavValidationException(
            String.format("属性[%s]期望日期时间类型(yyyy-MM-dd HH:mm:ss)", attribute.getName())
        );
    }
    
    /**
     * 验证约束条件
     */
    private void validateConstraints(Attribute attribute, Object value) {
        AttributeType type = attribute.getDataType();
        
        // 验证字符串长度
        if (type == AttributeType.STRING && attribute.getMaxLength() != null) {
            String strValue = value.toString();
            if (strValue.length() > attribute.getMaxLength()) {
                throw new EavValidationException(
                    String.format("属性[%s]长度不能超过%d", attribute.getName(), attribute.getMaxLength())
                );
            }
        }
        
        // 验证数值范围
        if ((type == AttributeType.INTEGER || type == AttributeType.DECIMAL) 
            && (attribute.getMinValue() != null || attribute.getMaxValue() != null)) {
            
            double numValue = convertToDouble(value);
            
            if (attribute.getMinValue() != null && numValue < attribute.getMinValue()) {
                throw new EavValidationException(
                    String.format("属性[%s]值不能小于%.2f", attribute.getName(), attribute.getMinValue())
                );
            }
            
            if (attribute.getMaxValue() != null && numValue > attribute.getMaxValue()) {
                throw new EavValidationException(
                    String.format("属性[%s]值不能大于%.2f", attribute.getName(), attribute.getMaxValue())
                );
            }
        }
    }
    
    /**
     * 将值转换为double
     */
    private double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        throw new EavValidationException("无法将值转换为数值类型");
    }
}
