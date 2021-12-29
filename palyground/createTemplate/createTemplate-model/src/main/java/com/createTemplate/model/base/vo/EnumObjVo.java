/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.base.vo;

import com.createTemplate.model.annotation.AntField;
import com.createTemplate.model.base.pojo.EnumObj;
import com.createTemplate.model.mybatis.page.PageParameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典vo
 *
 * @version V1.0
 * @author:
 * @date: 2018年5月26日 下午5:55:43
 */
@ApiModel(value = "数据字典vo")
@Data
public class EnumObjVo extends EnumObj implements Serializable {
    @ApiModelProperty(value = "列", hidden = true)
    private String column;
    @ApiModelProperty(value = "当前页")
    private Integer page;
    @ApiModelProperty(value = "每页的条数")
    private Integer rows;
    @ApiModelProperty(value = "上级字典值")
    private String upperEnumValue;
    @ApiModelProperty(value = "类型名称")
    private String enumTypeName;
    @ApiModelProperty(name = "分页参数 ", hidden = true)
    private PageParameter pageParameter;

}
