package com.atguigu.service;

import com.atguigu.common.BaseService;
import com.atguigu.entity.Role;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/11
 */
public interface RoleService extends BaseService<Role> {
    /**
     * 查找全部
     *
     * @return List<Role>
     */
    List<Role> getAll();

}
