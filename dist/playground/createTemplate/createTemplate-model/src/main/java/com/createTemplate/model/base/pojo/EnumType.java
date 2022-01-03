/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.base.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.createTemplate.model.annotation.AntBean;
import com.createTemplate.model.annotation.AntField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典类型
 *
 * @version V1.0
 * @author:
 * @date: 2018年5月26日 下午5:46:55
 */
@ApiModel(description = "数据字典类型")
@Entity(name = "T_ENUMTYPE")
@Data
public class EnumType implements Serializable {

    @Id
    /**id*/
    private Long id;

    /**
     * 字典类型名称
     */
    @ApiModelProperty(value = "字典类型名称")
    private String enumTypeName;

    /**
     * 字典类型描述
     */
    @ApiModelProperty(value = "字典类型描述")
    private String remark;

    /**
     * 字典类型代码
     */
    @ApiModelProperty(value = "字典类型代码")
    private String enumTypeCode;

}
