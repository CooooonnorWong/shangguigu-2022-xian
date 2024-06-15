package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service
 * 作者:Connor
 * 日期:2022/6/20
 */
public interface UserFollowService {
    UserFollow findByUserIdAndHouseId(Long userId, Long houseId);

    void update(UserFollow userFollow);

    void insert(UserFollow userFollow);

    PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userInfoId);
}
