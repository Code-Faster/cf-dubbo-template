package com.createTemplate.model.core.vo;

import com.createTemplate.model.core.pojo.TAdvanceOrder;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "T_ADVANCE_ORDER")
@ApiModel(value = "提现订单表VO")
@SuppressWarnings("serial")
/*** 提现订单表 ***/
public class TAdvanceOrderVO extends TAdvanceOrder implements Serializable {
    @ApiModelProperty(value = "ID集合，逗号分隔")
    private String ids;
    @ApiModelProperty(value = "当前页")
    private Integer page;
    @ApiModelProperty(value = "每页的条数")
    private Integer rows;
    @ApiModelProperty(value = "分页参数")
    private PageParameter pageParameter;
    @ApiModelProperty(value = "列")
    private String column;
}