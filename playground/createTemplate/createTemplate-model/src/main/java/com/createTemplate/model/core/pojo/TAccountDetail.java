package com.createTemplate.model.core.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "T_ACCOUNT_DETAIL")
@ApiModel(value = "账户明细")
@SuppressWarnings("serial")
/*** 账户明细 ***/
@Data
public class TAccountDetail implements Serializable {
    @Id
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long personId;
    @ApiModelProperty(value = "关联Id")
    private Long relevance;
    @ApiModelProperty(value = "消费类型  0充值  1 购买VIP会员 2 车位租赁付费 3线下提现 4 车位租赁收入 5 预约退款  6 线上提现（提现到微信零钱）7线上提现（提现到微信零钱）手续费")
    private Integer type;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "添加时间")
    private Date inputDate;
    @ApiModelProperty(value = "关联订单号")
    private String orderNo;
    @ApiModelProperty(value = "交易状态 1、正常 2、冻结 3、取消交易")
    private Integer status;
    @ApiModelProperty(value = "交易金额 分为单位")
    private Integer account;
    /**
     * 交易类型 充值0
     */
    public static final int TYPE_RECHARGE = 0;
    /**
     * 交易类型 vip消费1
     */
    public static final int TYPE_VIP_CONSUME = 1;
    /**
     * 交易类型 车位租赁付费2
     */
    public static final int TYPE_RENT_CONSUME = 2;
    /**
     * 交易类型 线下提现3
     */
    public static final int TYPE_BACKSTAGE_WITHDRAW = 3;
    /**
     * 交易类型 车位租赁收入4
     */
    public static final int TYPE_RENT_INCOME = 4;
    /**
     * 交易类型 预约退款5
     */
    public static final int TYPE_BOOKING_REFUND = 5;
    /**
     * 交易类型 线上提现（提现到微信零钱） 6
     */
    public static final int TYPE_WITHDRAW_DEPOSIT = 6;
    /**
     * 交易类型 7消费返现
     */
    public static final int TYPE_WITHDRAW_DEPOSIT_CONSUME = 7;
    /**
     * 交易状态 正常
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 交易状态 冻结
     */
    public static final int STATUS_FREEZEL = 2;
    /**
     * 交易状态 取消交易
     */
    public static final int STATUS_CANCLE = 3;
}