package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/14
 */
public interface DictService {
    /**
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> findZnodes(Long id);

    /**
     * 根据dictCode查询dict集合
     *
     * @param parentDictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);

    /**
     * 根据父节点的id查询其所有的子节点
     *
     * @param parentId
     * @return
     */
    List<Dict> findDictListByParentId(Long parentId);
}
