package com.xxx.service.impl;

import com.xxx.pojo.Admin;
import com.xxx.mapper.AdminMapper;
import com.xxx.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author Bing
 * @since 2022-06-08
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
