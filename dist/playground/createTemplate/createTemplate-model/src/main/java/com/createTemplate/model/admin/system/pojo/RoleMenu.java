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

/**
 * 菜单
 *
 * @version 1.0
 * @date 2015-1-8上午10:10:38
 */
@Entity(name = "T_ROLE_MENU")
@ApiModel(value = "角色菜单")
@Data
public class RoleMenu implements java.io.Serializable {

    @Id
    /** id */
	@ApiModelProperty(value = "Id")
    private Long id;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private Long menuId;

    /**
     * 菜单英文名称
     */
    @ApiModelProperty(value = "菜单英文名称")
    private String menuEname;

    /**
     * 按钮英文名称 多个逗号分隔
     */
    @ApiModelProperty(value = "按钮英文名称 多个逗号分隔")
    private String buttonEname;

}
