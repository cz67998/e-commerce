package com.webank.dao;

import com.webank.entity.PayInfo;

public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer payInfoId);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer payInfoId);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);
}