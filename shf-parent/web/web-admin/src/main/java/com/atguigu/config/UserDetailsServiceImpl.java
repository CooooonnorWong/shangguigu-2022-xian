package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目:shf-parent
 * 包:com.atguigu.config
 * 作者:Connor
 * 日期:2022/6/22
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        List<String> codePermissionList = permissionService.findCodePermissionListByAdminId(admin.getId());
        List<GrantedAuthority> grantedAuthorities = codePermissionList.stream()
                .filter(codePermission -> !StringUtils.isEmpty(codePermission))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        //校验密码,赋予操作权限
        return new User(username, admin.getPassword(), grantedAuthorities);
    }
}
