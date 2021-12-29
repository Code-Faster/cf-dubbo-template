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
 * 菜单
 *
 * @author
 * @version 1.0
 * @date 2015-1-8上午10:10:38
 */
@Entity(name = "T_MENU")
@ApiModel(value = "菜单")
@Data
public class Menu implements Serializable {
    private static final long serialVersionUID = -6626977647959072701L;
    @Id
    /** id */
    private Long id;
    /**
     * 菜单中文名称
     */
    @ApiModelProperty(value = "菜单中文名称")
    private String menuCname;

    /**
     * 菜单英文名称
     */
    @ApiModelProperty(value = "菜单英文名称")
    private String menuEname;

    /**
     * 上级菜单id
     */
    @ApiModelProperty(value = "上级菜单id")
    private Long parentId;

    /**
     * 级别 1 一级菜单；2 二级菜单
     */
    @ApiModelProperty(value = "级别 1 一级菜单；2 二级菜单")
    private String level;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String iconCls;

    /**
     * 链接地址
     */
    @ApiModelProperty(value = "链接地址")
    private String url;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态 0 正常；1 禁用")
    private Integer status;

}
