package com.atguigu.util;

import com.atguigu.entity.Permission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目:shf-parent
 * 包:com.atguigu.util
 * 作者:Connor
 * 日期:2022/6/22
 */
public class MenuBuilder {
    public static List<Permission> buildMenu(List<Permission> originalPermissionList) {
        List<Permission> parentPermissionList = originalPermissionList.stream()
                .filter(permission -> permission.getParentId() == 0L)
                .collect(Collectors.toList());
        parentPermissionList.forEach(treeNode -> {
            treeNode.setLevel(1);
            treeNode.setChildren(getChildren(treeNode, originalPermissionList));
        });
        return parentPermissionList;
    }

    private static List<Permission> getChildren(Permission treeNode, List<Permission> originalPermissionList) {
        List<Permission> childPermissionList = originalPermissionList.stream()
                .filter(permission -> treeNode.getId().longValue() == permission.getParentId().longValue())
                .collect(Collectors.toList());
        childPermissionList.forEach(permission -> {
            permission.setParentName(treeNode.getName());
            permission.setLevel(treeNode.getLevel() + 1);
            permission.setChildren(getChildren(permission, originalPermissionList));
        });
        return childPermissionList;
    }

}
