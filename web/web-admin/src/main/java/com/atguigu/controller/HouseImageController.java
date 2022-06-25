package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.common.BaseController;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 项目:shf-parent
 * 包:com.atguigu.controller
 * 作者:Connor
 * 日期:2022/6/17
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {

    private static final String PAGE_UPLOAD = "house/upload";
    private static final String SHOW_ACTION = "redirect:/house/";
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;

    @PreAuthorize("hasAnyAuthority('house.create','house.edit','house.editImage')")
    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable Long houseId,
                             @PathVariable Integer type,
                             Model model
    ) {
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return PAGE_UPLOAD;
    }

    @PreAuthorize("hasAnyAuthority('house.create','house.edit','house.editImage')")
    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable Long houseId,
                         @PathVariable Integer type,
                         @RequestParam("file") MultipartFile[] files
    ) throws IOException {
        for (int i = 0; i < files.length; i++) {
            String uuidName = FileUtil.getUUIDName(files[i].getOriginalFilename());
            //将图片存到七牛云中
            QiniuUtils.upload2Qiniu(files[i].getBytes(), uuidName);
            //获取图片在七牛云中的url
            String imageUrl = QiniuUtils.getUrl(uuidName);
            //将HouseImage对象存到hse_house_image表中
            HouseImage houseImage = new HouseImage();
            houseImage.setHouseId(houseId);
            houseImage.setImageUrl(imageUrl);
            houseImage.setImageName(uuidName);
            houseImage.setType(type);
            houseImageService.insert(houseImage);
            if (i == 0) {
                //更新hse_house表中的default_image_url字段
                House house = houseService.getById(houseId);
                if (house.getDefaultImageUrl() == null || "".equals(house.getDefaultImageUrl()) || "null".equals(house.getDefaultImageUrl())) {
                    house.setDefaultImageUrl(imageUrl);
                    houseService.update(house);
                }
            }
        }
        return Result.ok();
    }

    @PreAuthorize("hasAnyAuthority('house.create','house.edit','house.editImage')")
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id) {
        //删除hse_house_image表中指定id的图片
        HouseImage houseImage = houseImageService.getById(id);
        houseImageService.delete(id);
        //删除七牛云中的图片文件
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        House house = houseService.getById(houseId);
        //如果hse_house表中指定id的house的default_image_url就是被删除的图片的url的话需要重新指定
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())) {
            //如果hse_house的default_image_url在hse_house_image中被删除，则指定为"null"值
            house.setDefaultImageUrl("null");
        }
        return SHOW_ACTION + houseId;
    }

}
