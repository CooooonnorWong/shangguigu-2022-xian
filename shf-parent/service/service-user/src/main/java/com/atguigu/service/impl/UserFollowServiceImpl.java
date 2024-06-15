package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 项目:shf-parent
 * 包:com.atguigu.service.impl
 * 作者:Connor
 * 日期:2022/6/20
 */
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public UserFollow findByUserIdAndHouseId(Long userId, Long houseId) {
        return userFollowMapper.findByUserIdAndHouseId(userId, houseId);
    }

    @Override
    public void update(UserFollow userFollow) {
        userFollowMapper.update(userFollow);
    }

    @Override
    public void insert(UserFollow userFollow) {
        userFollowMapper.insert(userFollow);
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userInfoId) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userFollowMapper.findListPage(userInfoId));
    }
}
