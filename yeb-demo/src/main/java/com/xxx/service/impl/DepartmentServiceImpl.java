package com.xxx.service.impl;

import com.xxx.pojo.Department;
import com.xxx.mapper.DepartmentMapper;
import com.xxx.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author Bing
 * @since 2022-06-08
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

}
