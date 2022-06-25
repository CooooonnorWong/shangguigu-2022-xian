package com.atguigu.common;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.common
 * 作者:Connor
 * 日期:2022/6/13
 */
public interface BaseService<T> {
    /**
     * 新增
     *
     * @param t
     */
    void insert(T t);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 修改
     *
     * @param t
     */
    void update(T t);

    /**
     * 根据id删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 分页查询
     *
     * @param filters
     * @return
     */
    PageInfo<T> findPage(Map<String, Object> filters);

}
