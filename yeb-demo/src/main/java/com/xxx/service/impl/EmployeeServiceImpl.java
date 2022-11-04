package com.xxx.service.impl;

import com.xxx.pojo.Employee;
import com.xxx.mapper.EmployeeMapper;
import com.xxx.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工表 服务实现类
 * </p>
 *
 * @author Bing
 * @since 2022-06-08
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
