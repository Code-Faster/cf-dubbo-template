package com.createTemplate.model.core.vo;

import com.createTemplate.model.core.pojo.TRechargeOrder;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "T_RECHARGE_ORDER")
@ApiModel(value = "充值订单表VO")
@SuppressWarnings("serial")
/*** 充值订单表 ***/
@Data
public class TRechargeOrderVO extends TRechargeOrder implements Serializable {
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