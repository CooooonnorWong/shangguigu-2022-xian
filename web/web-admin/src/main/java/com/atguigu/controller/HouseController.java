package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.common.BaseController;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.House;
import com.atguigu.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.controller
 * 作者:Connor
 * 日期:2022/6/15
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";

    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;

    private void saveAllDictToRequestScope(Model model) {
        model.addAttribute("communityList", communityService.findAll());
        model.addAttribute("houseTypeList", dictService.findDictListByParentDictCode("houseType"));
        model.addAttribute("floorList", dictService.findDictListByParentDictCode("floor"));
        model.addAttribute("buildStructureList", dictService.findDictListByParentDictCode("buildStructure"));
        model.addAttribute("directionList", dictService.findDictListByParentDictCode("direction"));
        model.addAttribute("decorationList", dictService.findDictListByParentDictCode("decoration"));
        model.addAttribute("houseUseList", dictService.findDictListByParentDictCode("houseUse"));
    }

    @PreAuthorize("hasAnyAuthority('house.show','house.create','house.edit','house.delete','house.editImage','house.editBroker','house.editUser','house.publish')")
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize", 10);
        }
        model.addAttribute("filters", filters);
        model.addAttribute("page", houseService.findPage(filters));

        saveAllDictToRequestScope(model);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('house.create')")
    @RequestMapping("/create")
    public String create(Model model) {
        saveAllDictToRequestScope(model);
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('house.create')")
    @PostMapping("/save")
    public String save(House house, Model model) {
        house.setStatus(HouseStatus.UNPUBLISHED.code);
        houseService.insert(house);
        return successPage(model, "房源信息添加成功");
    }

    @PreAuthorize("hasAuthority('house.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        House houseById = houseService.getById(id);
        model.addAttribute("house", houseById);
        saveAllDictToRequestScope(model);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('house.edit')")
    @PostMapping("/update")
    public String update(House house, Model model) {
        houseService.update(house);
        return successPage(model, "房源信息修改成功");
    }

    @PreAuthorize("hasAuthority('house.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        houseService.delete(id);
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('house.publish')")
    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable Long id, @PathVariable Integer status) {
        houseService.publish(id, status);
        return LIST_ACTION;
    }

    @PreAuthorize("hasAnyAuthority('house.show','house.create','house.edit','house.editImage','house.editBroker','house.editUser')")
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        House house = houseService.getById(id);
        model.addAttribute("house", house);
        model.addAttribute("community", communityService.getById(house.getCommunityId()));
        model.addAttribute("houseImage1List", houseImageService.findHouseImageList(house.getId(), 1));
        model.addAttribute("houseImage2List", houseImageService.findHouseImageList(house.getId(), 2));
        model.addAttribute("houseBrokerList", houseBrokerService.findHouseBrokerList(house.getId()));
        model.addAttribute("houseUserList", houseUserService.findHouseUserList(house.getId()));

        return PAGE_SHOW;
    }
}
