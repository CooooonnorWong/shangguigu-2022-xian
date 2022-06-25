package com.atguigu.service;

import com.atguigu.common.BaseService;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/16
 */
public interface HouseUserService extends BaseService<HouseUser> {
    /**
     * 根据houseId查询HouseUser集合
     *
     * @param houseId
     * @return
     */
    List<HouseUser> findHouseUserList(Long houseId);
}
