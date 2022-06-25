package com.atguigu.service;

import com.atguigu.common.BaseService;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/16
 */
public interface HouseBrokerService extends BaseService<HouseBroker> {
    /**
     * 根据houseId查询HouseBroker集合
     *
     * @param houseId
     * @return
     */
    List<HouseBroker> findHouseBrokerList(Long houseId);

    void updateBrokerHeadUrl(HouseBroker houseBroker);
}
