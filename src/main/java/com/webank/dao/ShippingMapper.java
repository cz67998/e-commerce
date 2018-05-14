package com.webank.dao;

import com.webank.entity.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer shippingId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer shippingId);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
}