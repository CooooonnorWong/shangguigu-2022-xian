package com.atguigu;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.House;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目:shf-parent
 * 包:com.atguigu
 * 作者:Connor
 * 日期:2022/6/18
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private CommunityService communityService;
    @Reference
    private UserFollowService userFollowService;

    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result<PageInfo<HouseVo>> list(@PathVariable Integer pageNum,
                                          @PathVariable Integer pageSize,
                                          @RequestBody HouseQueryBo houseQueryBo) {
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryBo);
        return Result.ok(pageInfo);
    }

    @GetMapping("/info/{id}")
    public Result<Map<String, Object>> info(@PathVariable Long id, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        House house = houseService.getById(id);
        map.put("house", house);
        map.put("community", communityService.getById(house.getCommunityId()));
        map.put("houseBrokerList", houseBrokerService.findHouseBrokerList(id));
        map.put("houseImage1List", houseImageService.findHouseImageList(id, 1));
        //查询用户是否关注了该房源
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        boolean isFollow = false;
        if (userInfo != null) {
            UserFollow userFollow = userFollowService.findByUserIdAndHouseId(userInfo.getId(), id);
            isFollow = userFollow != null && userFollow.getIsDeleted() == 0;
        }
        map.put("isFollow", isFollow);
        return Result.ok(map);
    }
}
