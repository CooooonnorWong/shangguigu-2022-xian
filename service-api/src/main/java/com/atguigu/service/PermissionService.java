package com.atguigu.service;

import com.atguigu.common.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/21
 */
public interface PermissionService extends BaseService<Permission> {
    /**
     * 根据roleId查找Permission集合并映射成Map集合
     *
     * @param roleId
     * @return
     */
    List<Map<String, Object>> findPermissionByRoleId(Long roleId);

    /**
     * 保存RolePermission
     *
     * @param roleId
     * @param permissionIds
     */
    void saveRolePermission(Long roleId, List<Long> permissionIds);

    /**
     * 根据roleId删除RolePermission
     * 联动删除
     *
     * @param roleId
     */
    void deleteRolePermissionByRoleId(Long roleId);

    /**
     * 根据adminId查询动态菜单
     *
     * @param adminId
     * @return
     */
    List<Permission> findMenuPermissionByAdminId(Long adminId);

    /**
     * 查询所有菜单
     *
     * @return
     */
    List<Permission> findAllMenu();

    /**
     * 根据parentId查询permission数量
     *
     * @param parentId
     * @return
     */
    Integer findCountByParentId(Long parentId);

    /**
     * 根据adminId查询acl_permission从表中的code字段
     *
     * @param adminId
     * @return
     */
    List<String> findCodePermissionListByAdminId(Long adminId);
}
