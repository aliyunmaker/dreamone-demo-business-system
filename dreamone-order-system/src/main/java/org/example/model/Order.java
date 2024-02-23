package org.example.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author 恬裕
 * @date 2024/1/3
 */
@Data
@Builder
public class Order {
    private Long rowNumber;
    private Long orderKey;
    private Long custKey;
    private String orderStatus;
    private Double totalPrice;
    private Integer orderDate;
    private String orderPriority;
    private String clerk;
    private Integer shipPriority;
    private String comment;
}
