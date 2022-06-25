package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.common.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.controller
 * 作者:Connor
 * 日期:2022/6/22
 */
@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    private static final String PAGE_INDEX = "permission/index";
    private static final String PAGE_CREATE = "permission/create";
    private static final String LIST_ACTION = "redirect:/permission";
    private static final String PAGE_EDIT = "permission/edit";

    @Reference
    private PermissionService permissionService;

    @PreAuthorize("hasAnyAuthority('permission.show','permission.create','permission.delete','permission.edit')")
    @GetMapping
    public String index(Model model) {
        List<Permission> permissionList = permissionService.findAllMenu();
        model.addAttribute("list", permissionList);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('permission.create')")
    @GetMapping("/create")
    public String create(Permission permission, Model model) {
        model.addAttribute("permission", permission);
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('permission.create')")
    @PostMapping("/save")
    public String save(Permission permission, Model model) {
        permissionService.insert(permission);
        return successPage(model, "菜单添加成功");
    }

    @PreAuthorize("hasAuthority('permission.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (permissionService.findCountByParentId(id) > 0) {
            throw new RuntimeException("当前菜单有子菜单，不能删除");
        }
        permissionService.delete(id);
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('permission.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("permission", permissionService.getById(id));
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('permission.edit')")
    @PostMapping("/update")
    public String update(Permission permission, Model model) {
        permissionService.update(permission);
        return successPage(model, "菜单修改成功");
    }
}
