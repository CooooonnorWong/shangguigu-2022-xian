package com.atguigu.mapper;

import com.atguigu.common.BaseMapper;
import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/21
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    List<Long> findRoleIdListByAdminId(Long adminId);

    void removeAdminRole(@Param("adminId") Long adminId, @Param("roleIds") List<Long> roleIds);

    AdminRole findByAdminIdAndRoleId(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    void deleteAdminRoleByRoleId(Long roleId);
}
