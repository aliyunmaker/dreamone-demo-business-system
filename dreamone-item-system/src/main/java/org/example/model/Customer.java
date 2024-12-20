package org.example.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author 恬裕
 * @date 2024/1/2
 */
@Data
@Builder
public class Customer {
    private Long custKey;
    private String name;
    private String address;
    private Long nationKey;
    private String phone;
    private Double acctBal;
    private String mktSegment;
    private String comment;
}
