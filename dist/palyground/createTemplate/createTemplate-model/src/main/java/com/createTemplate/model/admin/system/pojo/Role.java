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
 * 角色
 *
 * @version 1.0
 * @date 2015-1-8上午10:10:38
 */
@SuppressWarnings("serial")
@Entity(name = "T_ROLE")
@ApiModel(value = "角色表")
@Data
public class Role implements Serializable {
    @Id
    /** id */
    private Long id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "状态 0 正常；1 禁用")
    private Integer status;

}
