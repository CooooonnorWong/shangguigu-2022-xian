package com.atguigu.service;

import com.atguigu.common.BaseService;
import com.atguigu.entity.HouseImage;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/16
 */
public interface HouseImageService extends BaseService<HouseImage> {
    /**
     * 根据houseId和type查找HouseImage集合
     *
     * @param houseId
     * @param type
     * @return
     */
    List<HouseImage> findHouseImageList(Long houseId, Integer type);

}
