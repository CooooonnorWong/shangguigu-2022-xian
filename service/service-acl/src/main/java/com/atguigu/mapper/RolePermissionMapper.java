package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/21
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Long> findPermissionIdListByRoleId(Long roleId);

    void removeRolePermission(@Param("roleId") Long roleId, @Param("permissionIdList") List<Long> permissionIdList);

    RolePermission findByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    void deleteRolePermissionByRoleId(Long roleId);
}
