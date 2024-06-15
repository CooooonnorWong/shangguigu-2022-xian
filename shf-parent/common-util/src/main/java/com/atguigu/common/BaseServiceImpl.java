package com.atguigu.common;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu.common
 * 作者:Connor
 * 日期:2022/6/13
 */
public abstract class BaseServiceImpl<T> {

    protected abstract BaseMapper<T> getEntityMapper();

    /**
     * 新增
     *
     * @param t
     */
    public void insert(T t) {
        getEntityMapper().insert(t);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public T getById(Long id) {
        return getEntityMapper().getById(id);
    }

    /**
     * 修改
     *
     * @param t
     */
    public void update(T t) {
        getEntityMapper().update(t);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(Long id) {
        getEntityMapper().delete(id);
    }

    /**
     * 分页查询
     *
     * @param filters
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<T> findPage(Map<String, Object> filters) {
        PageHelper.startPage(CastUtil.castInt(filters.get("pageNum"), 1), CastUtil.castInt(filters.get("pageSize"), 10));
        return new PageInfo<>(getEntityMapper().findPage(filters), 10);
    }
}
