package com.xxx.server.controller;

import com.xxx.server.pojo.Position;
import com.xxx.server.service.PositionService;
import com.xxx.server.utils.RespBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 职位管理 前端控制器
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    private PositionService positionService;

    //查询用get
    @ApiOperation(value = "获取所有职位信息")
    @GetMapping("/")
    public List<Position> getAllPosition() {
        return positionService.list();
    }

    //添加用post
    @ApiOperation(value = "添加职位")
    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        //自动创建时间
        position.setCreateDate(LocalDateTime.now());
        //保存
        if (positionService.save(position)) {
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    //更新用put
    @ApiOperation(value = "更新职位信息")
    @PutMapping("/") //@RequestBody主要用来接收前端传递给后端的请求体中的数据
    public RespBean updatePosition(@RequestBody Position position) {
        //自动创建时间
        position.setCreateDate(LocalDateTime.now());
        //根据id进行更新
        if (positionService.updateById(position)) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    //删除用delete
    @ApiOperation(value = "删除单条职位信息")
    @DeleteMapping("/{id}")
    public RespBean deletePosition(@PathVariable Integer id) {
        if (positionService.removeById(id)) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除职位信息")
    @DeleteMapping("/")
    public RespBean deletePositionByIds(Integer[] ids) {
        if (positionService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

}

