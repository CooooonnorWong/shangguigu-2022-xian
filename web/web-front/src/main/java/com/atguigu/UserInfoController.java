package com.atguigu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu
 * 作者:Connor
 * 日期:2022/6/20
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;

    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone, HttpSession session) {
        // TODO: 2022/6/20
        //模拟发送验证码
        String code = "0000";
        session.setAttribute("CODE", code);
        return Result.ok();
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterBo registerBo, HttpSession session) {
        //判断用户是否已经存在
        UserInfo userInfo = userInfoService.getByPhone(registerBo.getPhone());
        if (userInfo != null) {
            //如果已经存在，则返回错误信息
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //用户不存在，则检查验证码是否正确
        if (!registerBo.getCode().equals(session.getAttribute("CODE"))) {
            //如果不正确，则返回错误信息
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //验证码正确，进行注册
        userInfo = new UserInfo();
        BeanUtils.copyProperties(registerBo, userInfo);
        userInfo.setPassword(MD5.encrypt(userInfo.getPassword()));
        userInfo.setStatus(1);
        System.out.println("userInfo = " + userInfo);
        userInfoService.insert(userInfo);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginBo loginBo, HttpSession session) {
        //判断用户是否存在
        UserInfo userInfo = userInfoService.getByPhone(loginBo.getPhone());
        if (userInfo == null) {
            //如果不存在，则返回错误信息
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        //用户存在,则判断密码是否正确
        if (userInfo.getPassword().equals(MD5.encrypt(loginBo.getPassword()))) {
            //如果密码不正确，则返回错误信息
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        //将用户信息放入session中
        session.setAttribute("USER", userInfo);
        //数据回显
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("nickName", userInfo.getNickName());
        responseMap.put("phone", userInfo.getPhone());
        return Result.ok(responseMap);
    }

    @GetMapping("/logout")
    public Result logout(HttpSession session) {
        session.invalidate();
        return Result.ok();
    }
}
