package com.createTemplate.model.core.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "T_ADVANCE_ORDER")
@ApiModel(value = "提现订单表")
@SuppressWarnings("serial")
/*** 提现订单表 ***/
@Data
public class TAdvanceOrder implements Serializable {
    @Id
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long personId;
    @ApiModelProperty(value = "退款订单号")
    private String orderNo;
    @ApiModelProperty(value = "金额 以分为单位")
    private Integer amount;
    @ApiModelProperty(value = "账户付的金额  以分为单位")
    private Integer accountAmount;
    @ApiModelProperty(value = "其他金额  以分为单位")
    private Integer otherAmount;
    @ApiModelProperty(value = "提现渠道 0 提现到微信余额 1提现到银行卡 ")
    private Integer channel;
    @ApiModelProperty(value = "状态 0 未提现 1 已提现")
    private Integer status;
    @ApiModelProperty(value = "退款时间")
    private Date refundDate;
    @ApiModelProperty(value = "退款响应时间")
    private Date refundResponseDate;
    private Date feeDate;
    @ApiModelProperty(value = "插入时间")
    private Date inputDate;
    @ApiModelProperty(value = "微信付款单号")
    private String paymentNo;
}