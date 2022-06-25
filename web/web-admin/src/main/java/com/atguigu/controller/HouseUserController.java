package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.common.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 项目:shf-parent
 * 包:com.atguigu.controller
 * 作者:Connor
 * 日期:2022/6/17
 */
@Controller
@RequestMapping("/houseUser")
public class HouseUserController extends BaseController {

    private static final String PAGE_CREATE = "houseUser/create";
    private static final String PAGE_EDIT = "houseUser/edit";
    private static final String SHOW_ACTION = "redirect:/house/";


    @Reference
    private HouseUserService houseUserService;

    @PreAuthorize("hasAuthority('house.editUser')")
    @GetMapping("/create")
    public String create(@RequestParam Long houseId, Model model) {
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);
        model.addAttribute("houseUser", houseUser);
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('house.editUser')")
    @PostMapping("/save")
    public String save(HouseUser houseUser, Model model) {
        houseUserService.insert(houseUser);
        return successPage(model, "房东信息添加成功");
    }

    @PreAuthorize("hasAuthority('house.editUser')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser", houseUser);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('house.editUser')")
    @PostMapping("/update")
    public String update(HouseUser houseUser, Model model) {
        houseUserService.update(houseUser);
        return successPage(model, "房东信息修改成功");
    }

    @PreAuthorize("hasAuthority('house.editUser')")
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id) {
        houseUserService.delete(id);
        return SHOW_ACTION + houseId;
    }
}
