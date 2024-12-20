package org.example.constant;

/**
 * @author 恬裕
 * @date 2024/1/8
 */
public interface BaseErrorInfo {

    String getCode();

    String getMessage();

    Integer getHttpStatusCode();
}
