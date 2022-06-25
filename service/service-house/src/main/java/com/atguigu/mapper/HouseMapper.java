package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/15
 */
public interface HouseMapper extends BaseMapper<House> {

    void publish(@Param("id") Long id, @Param("status") Integer status);

    Page<HouseVo> findListPage(HouseQueryBo houseQueryBo);
}
