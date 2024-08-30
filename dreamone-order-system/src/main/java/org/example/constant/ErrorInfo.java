package org.example.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 恬裕
 * @date 2024/1/8
 */
@Getter
@AllArgsConstructor
public enum ErrorInfo implements BaseErrorInfo{

    OK("OK", "Request success.", 200),
    SYSTEM_ERROR("InternalError", "A system error occurred.", 500),
    INVALID_PARAMETER("InvalidParameter", "One of parameters is not valid.", 400),
    NO_PERMISSION("NoPermission", "You are not authorized to perform this operation.", 403);

    String code;

    String message;

    Integer httpStatusCode;

    @Override
    public String toString() {
        return String.format("{\"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\"}", this.getHttpStatusCode(), this.getCode(), this.getMessage());
    }

}
