package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.controller
 * 作者:Connor
 * 日期:2022/6/14
 */
@Controller
@RequestMapping("/dict")
public class DictController {
    private static final String PAGE_INDEX = "dict/index";
    @Reference
    private DictService dictService;

    @PreAuthorize("hasAuthority('dict.show')")
    @GetMapping
    public String index() {
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('dict.show')")
    @GetMapping("/findZnodes")
    @ResponseBody
    public Result<List<Map<String, Object>>> findZnodes(@RequestParam(defaultValue = "0") Long id) {
        List<Map<String, Object>> zNodes = dictService.findZnodes(id);
        return Result.ok(zNodes);
    }

    @PreAuthorize("hasAuthority('dict.show')")
    @GetMapping("/findDictListByParentId/{parentId}")
    @ResponseBody
    public Result<List<Dict>> findDictListByParentId(@PathVariable Long parentId) {
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }
}
