package com.xxx.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 用户登录实体类
 *  专门封装用户登录数据 vo
 *  用于接收前端输入的信息
 * @author bing  @create 2021/1/13-下午8:30
 */
@Data
//@EqualsAndHashCode(callSuper = false)：不会比较其继承的父类的属性可能会导致错误判断
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//这个用于接口文档
@ApiModel(value="Admin登录对象", description="")
public class AdminLoginParam {

    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    public String code;
}
