/*
 * Copyright (c) 2017-2020 深圳市科瑞特网络科技有限公司 SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.admin.system.vo;

import com.createTemplate.model.admin.system.pojo.RoleMenu;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 角色菜单表vo
 */

@SuppressWarnings("serial")
@Entity(name = "T_ROLE_MENU")
@ApiModel(value = "角色菜单表vo")
@Data
public class RoleMenuVo extends RoleMenu {

    @ApiModelProperty(value = "列")
    private String column;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "每页的条数")
    private Integer rows;

    @ApiModelProperty(value = "分页参数")
    private PageParameter pageParameter;


}
