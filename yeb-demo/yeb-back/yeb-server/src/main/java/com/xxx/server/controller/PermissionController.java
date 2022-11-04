package com.xxx.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.pojo.Menu;
import com.xxx.server.pojo.MenuRole;
import com.xxx.server.pojo.Role;
import com.xxx.server.service.MenuRoleService;
import com.xxx.server.service.MenuService;
import com.xxx.server.service.RoleService;
import com.xxx.server.utils.RespBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组 - 角色
 *
 * @author bing  @create 2021/1/16-下午4:25
 */
@RestController
@RequestMapping("/system/basic/permission")
public class PermissionController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role") //@RequsetBody把前端返回的json数据变成对象
    public RespBean addRole(@RequestBody Role role) {
        //因为用了SpringSecurity，所以角色要加ROLE_
        //判断一下是否有ROLE_的前缀
        if (!role.getName().startsWith("ROLE_")) {
            //没有就进行补充
            role.setName("ROLE_" + role.getName());
        }
        if (roleService.save(role)) {
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}") //@PathVariable从前端根据url传过来的数据进行获取
    public RespBean deleteRole(@PathVariable Integer rid) {
        if (roleService.removeById(rid)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色 id 查询菜单 id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid) {
        //放入了id条件
        return menuRoleService.list(new QueryWrapper<MenuRole>()
                .eq("rid", rid)) // eq里面是数据库的字段
                .stream().map(MenuRole::getMid)//先转成菜单的id
                .collect(Collectors.toList());//然后再转成list
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        return menuRoleService.updateMenuRole(rid, mids);
    }

}
