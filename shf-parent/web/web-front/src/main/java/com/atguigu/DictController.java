package com.atguigu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu
 * 作者:Connor
 * 日期:2022/6/18
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    @Reference
    private DictService dictService;

    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    public Result<List<Dict>> findDictListByParentDictCode(@PathVariable String dictCode) {
        List<Dict> dictListByParentDictCode = dictService.findDictListByParentDictCode(dictCode);
        return Result.ok(dictListByParentDictCode);
    }

    @GetMapping("/findDictListByParentId/{parentId}")
    public Result<List<Dict>> findDictListByParentId(@PathVariable Long parentId) {
        List<Dict> dictListByParentId = dictService.findDictListByParentId(parentId);
        return Result.ok(dictListByParentId);
    }
}
