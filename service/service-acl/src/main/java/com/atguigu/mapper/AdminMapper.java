package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.Admin;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/13
 */
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> findAll();

    Admin getByUsername(String username);
}
