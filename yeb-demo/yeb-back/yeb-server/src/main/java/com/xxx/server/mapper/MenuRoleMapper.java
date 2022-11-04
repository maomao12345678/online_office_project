package com.xxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 菜单角色中间表 Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {


    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return 返回受影响行数
     */
    //这里一定要加注解不然MenuRoleMapper输入的两个值(item, rid)可能会报错
    Integer insertRecord(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
