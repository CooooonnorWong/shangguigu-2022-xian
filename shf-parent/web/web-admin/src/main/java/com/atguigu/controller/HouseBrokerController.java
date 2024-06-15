package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.common.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
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
 * 日期:2022/6/16
 */
@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {

    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String SHOW_ACTION = "redirect:/house/";

    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private AdminService adminService;

    @PreAuthorize("hasAuthority('house.editBroker')")
    @GetMapping("/create")
    public String create(HouseBroker houseBroker, Model model) {
        model.addAttribute("houseBroker", houseBroker);
        List<Admin> adminList = adminService.findAll();
        removeDuplicatedAdmin(adminList, houseBroker.getHouseId());
        model.addAttribute("adminList", adminList);
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('house.editBroker')")
    @PostMapping("/save")
    public String save(HouseBroker houseBroker, Model model) {
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);
        return successPage(model, "经纪人添加成功");
    }

    /**
     * 去除adminList中已经存在的houseBroker,
     * 使下拉菜单中只有不是该房源houseBroker的admin
     *
     * @param adminList
     * @param houseId
     */
    private void removeDuplicatedAdmin(List<Admin> adminList, Long houseId) {
        //去除adminList中已经存在的houseBroker,使下拉菜单中只有不是该房源houseBroker的admin
        for (HouseBroker broker : houseBrokerService.findHouseBrokerList(houseId)) {
            adminList.removeIf(admin -> admin.getId().equals(broker.getBrokerId()));
        }
    }

    @PreAuthorize("hasAuthority('house.editBroker')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("houseBroker", houseBroker);
        List<Admin> adminList = adminService.findAll();
        removeDuplicatedAdmin(adminList, houseBroker.getHouseId());
        model.addAttribute("adminList", adminList);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('house.editBroker')")
    @PostMapping("/update")
    public String update(HouseBroker houseBroker, Model model) {
        houseBroker.setBrokerName(adminService.getById(houseBroker.getBrokerId()).getName());
        houseBrokerService.update(houseBroker);
        return successPage(model, "经纪人修改成功");
    }

    @PreAuthorize("hasAuthority('house.editBroker')")
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id) {
        houseBrokerService.delete(id);
        return SHOW_ACTION + houseId;
    }

}
