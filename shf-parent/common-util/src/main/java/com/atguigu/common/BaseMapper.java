package com.atguigu.common;

import java.util.List;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.common
 * 作者:Connor
 * 日期:2022/6/13
 */
public interface BaseMapper<T> {
    /**
     * 新增
     *
     * @param t
     */
    void insert(T t);

    /**
     * 根据id查找
     *
     * @param id
     * @return T
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
     * 分页查找
     *
     * @param filters
     * @return
     */
    List<T> findPage(Map<String, Object> filters);

}
