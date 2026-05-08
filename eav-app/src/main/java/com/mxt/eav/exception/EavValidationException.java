package com.mxt.eav.exception;

/**
 * EAV验证异常
 * 当属性值验证失败时抛出
 */
public class EavValidationException extends RuntimeException {
    
    public EavValidationException(String message) {
        super(message);
    }
    
    public EavValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
