package com.xxx.server.exception;

import com.xxx.server.utils.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author bing  @create 2021/1/15-上午10:46
 */
//表示为控制增强类
@RestControllerAdvice
public class GlobalException {
    //补抓SQL异常
    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e) {
        //关联数据异常
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据有关联数据，操作失败！");
        }
        return RespBean.error("数据库异常，操作失败！");
    }
}
