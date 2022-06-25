package com.atguigu.common;

import org.springframework.ui.Model;

/**
 * 项目:shf-parent
 * 包:com.atguigu.common
 * 作者:Connor
 * 日期:2022/6/13
 */
public class BaseController {
    private static final String PAGE_SUCCESS = "common/successPage";

    public String successPage(Model model, String successMessage) {
        model.addAttribute("messagePage", successMessage);
        return PAGE_SUCCESS;
    }
}
