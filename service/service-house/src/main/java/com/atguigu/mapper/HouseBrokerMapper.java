package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/16
 */
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {

    List<HouseBroker> findHouseBrokerList(Long houseId);

    void updateBrokerHeadUrl(HouseBroker houseBroker);
}
