/*
 * Copyright (c) 2017-2020 深圳市科瑞特网络科技有限公司 SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.admin.system.vo;

import com.createTemplate.model.admin.system.pojo.Role;
import com.createTemplate.model.annotation.AntBean;
import com.createTemplate.model.annotation.AntField;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Set;


/**
 * 角色vo
 *
 * @version 1.0
 */

@Entity(name = "T_ROLE")
@ApiModel(value = "角色vo")
@Data
public class RoleVo extends Role implements java.io.Serializable {

    @ApiModelProperty(value = "列")
    private String column;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "每页的条数")
    private Integer rows;

    @ApiModelProperty(value = "排序字段")
    private String order;

    @ApiModelProperty(value = "ids")
    private String ids;

    @ApiModelProperty(value = "分页参数 ")
    private PageParameter pageParameter;

    @ApiModelProperty(value = "菜单id")
    private String menuIds;

    @ApiModelProperty(value = "角色ids")
    private Set<Long> roleIds;


}
