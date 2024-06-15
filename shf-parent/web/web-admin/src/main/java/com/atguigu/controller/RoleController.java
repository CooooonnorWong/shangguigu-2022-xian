package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.common.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.controller
 * 作者:Connor
 * 日期:2022/6/11
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static final String LIST_ACTION = "redirect:/role";
    private static final String PAGE_INDEX = "role/index";
    private static final String PAGE_EDIT = "role/edit";
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";
    private static final String PAGE_CREATE = "role/create";

    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;
    @Reference
    private AdminService adminService;

    @PreAuthorize("hasAnyAuthority('role.show','role.create','role.edit','role.delete','role.assign')")
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize", 10);
        }
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        model.addAttribute("page", pageInfo);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('role.create')")
    @PostMapping("/save")
    public String save(Role role, Model model) {

        roleService.insert(role);
        return successPage(model, "角色信息添加成功");
    }

    @PreAuthorize("hasAuthority('role.create')")
    @GetMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('role.edit')")
    @PostMapping("/update")
    public String update(Role role, Model model) {
        roleService.update(role);
        return successPage(model, "角色信息修改成功");
    }

    @PreAuthorize("hasAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        //删除acl_role表中的数据
        roleService.delete(id);
        //联动删除
        //删除acl_role_permission表中role_id为id的数据
        permissionService.deleteRolePermissionByRoleId(id);
        //删除acl_admin_role表中role_id为id的数据
        adminService.deleteAdminRoleByRoleId(id);
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('role.assign')")
    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id, Model model) {
        model.addAttribute("roleId", id);

        List<Map<String, Object>> permissionList = permissionService.findPermissionByRoleId(id);

        model.addAttribute("zNodes", JSON.toJSONString(permissionList));

        return PAGE_ASSIGN_SHOW;
    }

    @PreAuthorize("hasAuthority('role.assign')")
    @PostMapping("/assignPermission")
    public String assignPermission(Model model,
                                   @RequestParam("roleId") Long roleId,
                                   @RequestParam("permissionIds") List<Long> permissionIds) {
        permissionService.saveRolePermission(roleId, permissionIds);
        return successPage(model, "角色权限修改成功");
    }
}
