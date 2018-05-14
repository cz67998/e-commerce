package com.webank.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Category {
    private Integer categoryId;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
}