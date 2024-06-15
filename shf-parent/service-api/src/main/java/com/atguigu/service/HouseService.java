package com.atguigu.service;

import com.atguigu.common.BaseService;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/15
 */
public interface HouseService extends BaseService<House> {

    void publish(Long id, Integer status);

    PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo);
}
