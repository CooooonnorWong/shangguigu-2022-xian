package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.common.BaseMapper;
import com.atguigu.common.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.AdminService;
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
 * 日期:2022/6/13
 */
@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    protected BaseMapper<Admin> getEntityMapper() {
        return adminMapper;
    }

    @Override
    public void insert(Admin admin) {
        Admin adminById = adminMapper.getById(admin.getId());
        if (adminById != null) {
            throw new RuntimeException();
        }
        super.insert(admin);
    }

    @Override
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }

    @Override
    public void deleteAdminRoleByRoleId(Long roleId) {
        adminRoleMapper.deleteAdminRoleByRoleId(roleId);
    }

    @Override
    public Admin getByUsername(String username) {
        return adminMapper.getByUsername(username);
    }

    @Override
    public Map<String, List<Role>> findRolesByAdminId(Long adminId) {
        //找出所有角色集合
        List<Role> allRoleList = roleMapper.getAll();
        //找出该admin已分配的所有角色的id集合
        List<Long> roleIdList = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //获取已分配的角色集合
        List<Role> assignedRoleList = allRoleList.stream()
                .filter(role -> roleIdList.contains(role.getId()))
                .collect(Collectors.toList());
        //获取未分配的角色集合
        List<Role> unAssignedRoleList = allRoleList.stream()
                .filter(role -> !roleIdList.contains(role.getId()))
                .collect(Collectors.toList());
        //将两个集合各自放入map中并返回
        Map<String, List<Role>> map = new HashMap<>();
        map.put("assignRoleList", assignedRoleList);
        map.put("unAssignRoleList", unAssignedRoleList);
        return map;
    }

    @Override
    public void saveAdminRole(Long adminId, List<Long> roleIds) {
        //根据adminId找出用户已分配的所有角色的roleId集合
        List<Long> assignedRoleIds = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //原来已分配，这次要添加->不动
        //原来已分配,这次要删除->将is_deleted = 1
        List<Long> removeRoleIds = assignedRoleIds.stream()
                .filter(roleId -> !roleIds.contains(roleId))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(removeRoleIds)) {
            adminRoleMapper.removeAdminRole(adminId, removeRoleIds);
        }
        //遍历roleIds，根据adminId和roleId查找AdminRole
        roleIds.forEach(roleId -> {
            AdminRole adminRole = adminRoleMapper.findByAdminIdAndRoleId(adminId, roleId);
            if (adminRole == null) {
                //从来没有分配过，这次要添加->添加数据到acl_admin_role表
                adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);
                adminRoleMapper.insert(adminRole);
            }
            //原来已分配,但是已删除，这次要添加->将is_deleted = 0
            adminRole.setIsDeleted(0);
            adminRoleMapper.update(adminRole);
        });
    }

}
