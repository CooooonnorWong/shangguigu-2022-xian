package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.Role;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/11
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查找全部
     * @return List<Role>
     */
    List<Role> getAll();

}
