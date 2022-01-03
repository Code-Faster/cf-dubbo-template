package com.createTemplate.model.core.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "T_RECHARGE_ORDER")
@ApiModel(value = "充值订单表")
@SuppressWarnings("serial")
/*** 充值订单表 ***/
@Data
public class TRechargeOrder implements Serializable {
    @Id
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long personId;
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "金额 以分为单位")
    private Integer amount;
    @ApiModelProperty(value = "账户付的金额  以分为单位")
    private Integer accountAmount;
    @ApiModelProperty(value = "其他金额  以分为单位")
    private Integer otherAmount;
    @ApiModelProperty(value = "支付渠道 0 账户支付；1 微信支付；2 支付宝支付；3 银联支付 ")
    private Integer channel;
    @ApiModelProperty(value = "状态 0 未支付；1 已支付；2 已退订；")
    private Integer status;
    @ApiModelProperty(value = "退款时间")
    private Date refundDate;
    @ApiModelProperty(value = "退款响应时间")
    private Date refundResponseDate;
    private Date feeDate;
    @ApiModelProperty(value = "插入时间")
    private Date inputDate;
    @ApiModelProperty(value = "第三方支付订单id")
    private String otherOrderNo;
}