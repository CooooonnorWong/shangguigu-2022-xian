package com.atguigu.mapper;

import com.atguigu.entity.UserInfo;

/**
 * 项目:shf-parent
 * 包:com.atguigu.mapper
 * 作者:Connor
 * 日期:2022/6/20
 */
public interface UserInfoMapper {

    UserInfo getByPhone(String phone);

    void insert(UserInfo userInfo);
}
