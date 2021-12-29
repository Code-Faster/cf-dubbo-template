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
import java.io.Serializable;

/**
 * 按钮
 */
@Entity(name = "T_BUTTON")
@ApiModel(value = "按钮")
@Data
public class Button implements Serializable {
    @Id
    /** id */
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    /**
     * 菜单英文名称
     */
    @ApiModelProperty(value = "菜单英文名称")
    private String menuEname;

    /**
     * 按钮英文名称
     */
    @ApiModelProperty(value = "按钮英文名称")
    private String buttonEname;

    /**
     * 按钮中文名称
     */
    @ApiModelProperty(value = "按钮中文名称")
    private String buttonCname;

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private Long menuId;

    @ApiModelProperty(value = "状态 0 正常；1 禁用")
    private Integer status;

}
