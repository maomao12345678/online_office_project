package com.xxx.service.impl;

import com.xxx.pojo.Role;
import com.xxx.mapper.RoleMapper;
import com.xxx.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Bing
 * @since 2022-06-08
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
