package com.atguigu.mapper;

import com.atguigu.entity.Dict;

import java.util.List;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/14
 */
public interface DictMapper {
    List<Dict> findListByParentId(Long parentId);

    Integer countIsParent(Long id);

    List<Dict> findDictListByParentDictCode(String parentDictCode);
}
