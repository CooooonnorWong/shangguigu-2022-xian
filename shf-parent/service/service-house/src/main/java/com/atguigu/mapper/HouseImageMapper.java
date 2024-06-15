package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/16
 */
public interface HouseImageMapper extends BaseMapper<HouseImage> {

    /**
     * 根据houseId和type查找HouseImage集合
     *
     * @param houseId
     * @param type
     * @return
     */
    List<HouseImage> findHouseImageList(@Param("houseId") Long houseId, @Param("type") Integer type);

}
