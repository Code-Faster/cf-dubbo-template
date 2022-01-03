/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.base.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.createTemplate.model.annotation.AntField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据字典
 *
 * @version V1.0
 * @author:
 * @date: 2018年5月26日 下午5:46:31
 */
@ApiModel(value = "数据字典信息")
@Entity(name = "T_ENUMOBJ")
@Data
public class EnumObj implements Serializable {

    /**
     * id
     */
    @Id
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 上级字典id
     */
    @ApiModelProperty(value = "上级字典id")
    private Long upperId;

    /**
     * 上级字典项
     */
    @ApiModelProperty(value = "上级字典项")
    private String upperEnumKey;

    /**
     * 字典类型主键id
     */
    @ApiModelProperty(value = "字典类型主键id")
    private Long enumTypeId;

    /**
     * 字典类型代码
     */
    @ApiModelProperty(value = "字典类型代码")
    private String enumTypeCode;

    /**
     * 字典项
     */
    @ApiModelProperty(value = "字典项")
    private String enumKey;

    /**
     * 字典值
     */
    @ApiModelProperty(value = "字典值")
    private String enumValue;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    /**
     * 状态 0正常；1删除
     */
    @ApiModelProperty(value = "状态 0正常；1删除")
    private Integer status;

    /**
     * 数据字典前缀
     */
    @ApiModelProperty(value = "数据字典前缀")
    public static final String enumFix = "recruit_enum_";

    /**
     * 数据字典按code获取的前缀
     */
    @ApiModelProperty(value = "数据字典按code获取的前缀")
    public static final String enumByCodeFix = "recruit_enumByCode_";

    /**
     * 数据字典所有key，用逗号分隔
     */
    @ApiModelProperty(value = "数据字典所有key，用逗号分隔")
    public static final String enumAllKeyString = "recruit_enum_enumAllKeyString";

}