package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.Community;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/15
 */
public interface CommunityMapper extends BaseMapper<Community> {

    List<Community> findAll();
}
