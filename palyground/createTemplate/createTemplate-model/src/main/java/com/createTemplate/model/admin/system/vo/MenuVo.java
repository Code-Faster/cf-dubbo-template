/*
 * Copyright (c) 2017-2020 深圳市科瑞特网络科技有限公司 SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.admin.system.vo;

import com.createTemplate.model.admin.system.pojo.Menu;
import com.createTemplate.model.annotation.AntField;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 菜单表vo
 */

@Entity(name = "T_MENU")
@ApiModel(value = "菜单表VO")
@Data
public class MenuVo extends Menu implements java.io.Serializable {

    @ApiModelProperty(value = "列")
    private String column;

    @ApiModelProperty(value = "分页参数")
    private PageParameter pageParameter;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "ids 多个逗号分隔")
    private String ids;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(name = "每页的条数")
    private Integer rows;

    @ApiModelProperty(value = "按钮英文名称 多个逗号分隔")
    private String buttonEname;

}
