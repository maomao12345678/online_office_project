package com.xxx.server.controller;

import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.Menu;
import com.xxx.server.service.AdminService;
import com.xxx.server.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/system/config")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "通过用户id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
//        Integer adminId = ((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
//        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
//        List<Menu> menus = (List<Menu>)valueOperations.get("meau_" + adminId);
//        //如果没有就从数据库中拿
//        if(CollectionUtils.isEmpty(menus)){
//            menus = menuService.getMenusByAdminId(adminId);
//            valueOperations.set("meau_"+adminId, menus);
//        }
//        return menus;
        // 只要登录，用户信息存在 security 全局对象中，从全局对象中获取用户id
        return menuService.getMenusByAdminId();
    }

}

