package com.xxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.server.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 员工表 Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    //这里的@Param是用于给Mapper的实现xml获取值,其中是在sql语句中使用
    Page<Employee> getEmployeeByPage(Page<Employee> page, @Param("employee") Employee employee, @Param("beginDateScope") LocalDate[] beginDateScope);

    /**
     * 查询员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    /**
     * 获取所有员工账套(分页）
     * @param page
     * @return
     */
    Page<Employee> getEmployeeWithSalary(Page<Employee> page);

}
