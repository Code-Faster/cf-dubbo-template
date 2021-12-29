/*
 * Copyright (c) 2017-2020 深圳市科瑞特网络科技有限公司 SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.admin.system.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户
 *
 * @version 1.0
 */
@Entity(name = "T_USER")
@ApiModel(value = "管理员")
@Data
public class Users implements java.io.Serializable {
    @Id
    /**id*/
    @ApiModelProperty(value = "Id")
    private Long id;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String realName;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String userPassword;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date inputDate;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

    /**
     * 0 正常 ； 1 禁用
     */
    @ApiModelProperty(value = "0 正常 ； 1 禁用")
    private Integer status;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Long roleId;

}
