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
    BUYER_TOO_MANY_UNPAID_ORDERS("BUYER_TOO_MANY_UNPAID_ORDERS", "Buyer has too many unpaid orders.", 400),
    NO_ADDRESS("NO_ADDRESS", "Address is null.", 400),
    USING_PROMOTION_FAIL("USING_PROMOTION_FAIL", "Using promotion failed", 400);

    String code;

    String message;

    Integer httpStatusCode;

    @Override
    public String toString() {
        return String.format("{\"HttpStatusCode\":\"%s\",\"Code\":\"%s\",\"Message\":\"%s\"}", this.getHttpStatusCode(), this.getCode(), this.getMessage());
    }

}
