package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.common.BaseMapper;
import com.atguigu.common.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.PermissionService;
import com.atguigu.util.MenuBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service.impl
 * 作者:Connor
 * 日期:2022/6/21
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    protected BaseMapper<Permission> getEntityMapper() {
        return permissionMapper;
    }

    @Override
    public void deleteRolePermissionByRoleId(Long roleId) {
        rolePermissionMapper.deleteRolePermissionByRoleId(roleId);
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> permissionList;
        if (adminId == 1L) {
            permissionList = permissionMapper.findAll();
        } else {
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }
        return MenuBuilder.buildMenu(permissionList);
    }

    @Override
    public List<Permission> findAllMenu() {
        return MenuBuilder.buildMenu(permissionMapper.findAll());
    }

    @Override
    public Integer findCountByParentId(Long parentId) {
        return permissionMapper.findCountByParentId(parentId);
    }

    @Override
    public List<String> findCodePermissionListByAdminId(Long adminId) {
        if (adminId == 1L) {
            return permissionMapper.findAllCodePermission();
        }
        return permissionMapper.findCodePermissionListByAdminId(adminId);
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        List<Long> permissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        List<Permission> allPermissionList = permissionMapper.findAll();
        return allPermissionList.stream()
                .map(permission -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", permission.getId());
                    map.put("pId", permission.getParentId());
                    map.put("name", permission.getName());
                    map.put("checked", permissionIdList != null && permissionIdList.contains(permission.getId()));
                    map.put("open", true);
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        List<Long> permissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        List<Long> removePermissionIdList = permissionIdList.stream()
                .filter(permissionId -> !permissionIds.contains(permissionId))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(removePermissionIdList)) {
            rolePermissionMapper.removeRolePermission(roleId, removePermissionIdList);
        }
        permissionIds.forEach(permissionId -> {
            RolePermission rolePermission = rolePermissionMapper.findByRoleIdAndPermissionId(roleId, permissionId);
            if (rolePermission == null) {
                rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermissionMapper.insert(rolePermission);
            } else {
                if (rolePermission.getIsDeleted() == 1) {
                    rolePermission.setIsDeleted(0);
                    rolePermissionMapper.update(rolePermission);
                }
            }
        });
    }
}
