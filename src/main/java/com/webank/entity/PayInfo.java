package com.webank.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PayInfo {
    private Integer payInfoId;

    private Integer userId;

    private Long orderNo;

    private Integer payPlatform;

    private String platformNumber;

    private String platformStatus;

    private Date createTime;

    private Date updateTime;
}