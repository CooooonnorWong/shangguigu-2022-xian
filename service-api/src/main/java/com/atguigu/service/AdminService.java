package com.atguigu.service;

import com.atguigu.common.BaseService;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/13
 */
public interface AdminService extends BaseService<Admin> {
    /**
     * 获取所有Admin集合
     *
     * @return
     */
    List<Admin> findAll();

    /**
     * 根据adminId获取该admin已分配和未分配的角色（role）集合
     *
     * @param adminId
     * @return
     */
    Map<String, List<Role>> findRolesByAdminId(Long adminId);

    /**
     * 保存分配的角色到acl_admin_role表中
     *
     * @param adminId
     * @param roleIds
     */
    void saveAdminRole(Long adminId, List<Long> roleIds);

    /**
     * 根据roleId删除adminRole
     * 联动删除
     *
     * @param roleId
     */
    void deleteAdminRoleByRoleId(Long roleId);

    /**
     * 根绝username获取admin
     *
     * @param username
     * @return
     */
    Admin getByUsername(String username);
}
