package com.webank.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Cart {
    private Integer cartId;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;
}