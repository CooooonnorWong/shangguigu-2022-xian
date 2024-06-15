package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.Permission;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/21
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> findAll();

    List<Permission> findPermissionListByAdminId(Long adminId);

    Integer findCountByParentId(Long parentId);

    List<String> findAllCodePermission();

    List<String> findCodePermissionListByAdminId(Long adminId);
}
