package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.common.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.controller
 * 作者:Connor
 * 日期:2022/6/13
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String LIST_ACTION = "redirect:/admin";
    private static final String PAGE_UPLOAD = "admin/upload";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";
    private static final String PAGE_CREATE = "admin/create";

    @Reference
    private AdminService adminService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyAuthority('admin.show','admin.create','admin.edit','admin.delete','admin.assign')")
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        model.addAttribute("page", pageInfo);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('admin.create')")
    @GetMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('admin.create')")
    @PostMapping("/save")
    public String save(Admin admin, Model model) {
        try {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            adminService.insert(admin);
        } catch (RuntimeException e) {
            return successPage(model, "用户账号重复，添加失败");
        }
        return successPage(model, "添加用户信息成功");
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Admin byId = adminService.getById(id);
        model.addAttribute("admin", byId);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @PostMapping("/update")
    public String update(Admin admin, Model model) {
        adminService.update(admin);
        return successPage(model, "修改用户信息成功");
    }

    @PreAuthorize("hasAuthority('admin.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return PAGE_UPLOAD;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam MultipartFile file, Model model) throws IOException {
        //使用FileUtil获取UUIDName
        String uuidName = FileUtil.getUUIDName(file.getOriginalFilename());
        //将图片存放在七牛云中
        QiniuUtils.upload2Qiniu(file.getBytes(), uuidName);
        //获取七牛云中的图片访问路径
        String headUrl = QiniuUtils.getUrl(uuidName);
        //将图片访问路径存放到acl_admin表中
        Admin admin = adminService.getById(id);
        admin.setHeadUrl(headUrl);
        adminService.update(admin);

        //更新hse_house_broker表中的broker_head_url
        HouseBroker houseBroker = new HouseBroker();
        houseBroker.setBrokerId(id);
        houseBroker.setBrokerHeadUrl(headUrl);
        houseBrokerService.updateBrokerHeadUrl(houseBroker);

        return successPage(model, "头像上传成功");
    }

    @PreAuthorize("hasAuthority('admin.assign')")
    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable Long id, Model model) {
        Map<String, List<Role>> roleListMap = adminService.findRolesByAdminId(id);
        model.addAllAttributes(roleListMap);
        model.addAttribute("adminId", id);
        return PAGE_ASSIGN_SHOW;
    }

    @PreAuthorize("hasAuthority('admin.assign')")
    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds,
                             Model model) {
        adminService.saveAdminRole(adminId, roleIds);
        return successPage(model, "权限修改成功");
    }
}
