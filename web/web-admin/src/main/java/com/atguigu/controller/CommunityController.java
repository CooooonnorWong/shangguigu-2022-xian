package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.common.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
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
@RequestMapping("/community")
public class CommunityController extends BaseController {
    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";

    private static final String LIST_ACTION = "redirect:/community";

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @PreAuthorize("hasAnyAuthority('community.show','community.create','community.edit','community.delete')")
    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize", 10);
        }
        PageInfo<Community> page = communityService.findPage(filters);
        if (!filters.containsKey("areaId")) {
            filters.put("areaId", 0);
        }
        if (!filters.containsKey("plateId")) {
            filters.put("plateId", 0);
        }
        //搜索回显
        model.addAttribute("filters", filters);
        //分页信息
        model.addAttribute("page", page);
        //二级联动菜单栏-区域选择栏
        model.addAttribute("areaList", dictService.findDictListByParentDictCode("beijing"));
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('community.create')")
    @RequestMapping("/create")
    public String create(Model model) {
        model.addAttribute("areaList", dictService.findDictListByParentDictCode("beijing"));
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('community.create')")
    @PostMapping("/save")
    public String save(Community community, Model model) {
        communityService.insert(community);
        return successPage(model, "小区信息添加成功");
    }

    @PreAuthorize("hasAuthority('community.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("community", communityService.getById(id));
        model.addAttribute("areaList", dictService.findDictListByParentDictCode("beijing"));
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('community.edit')")
    @PostMapping("/update")
    public String update(Community community, Model model) {
        communityService.update(community);
        return successPage(model, "小区信息修改成功");
    }

    @PreAuthorize("hasAuthority('community.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        communityService.delete(id);
        return LIST_ACTION;
    }
}
