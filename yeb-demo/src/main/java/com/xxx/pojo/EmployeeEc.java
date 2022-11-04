package com.xxx.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 员工奖罚表
 * </p>
 *
 * @author Bing
 * @since 2022-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_employee_ec")
@ApiModel(value="EmployeeEc对象", description="员工奖罚表")
public class EmployeeEc implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工编号")
    private Integer eid;

    @ApiModelProperty(value = "奖罚日期")
    private LocalDateTime ecDate;

    @ApiModelProperty(value = "奖罚原因")
    private String ecReason;

    @ApiModelProperty(value = "奖罚分")
    private Integer ecPoint;

    @ApiModelProperty(value = "奖罚类别，0：奖，1：罚")
    private Integer ecType;

    @ApiModelProperty(value = "备注")
    private String remark;


}
