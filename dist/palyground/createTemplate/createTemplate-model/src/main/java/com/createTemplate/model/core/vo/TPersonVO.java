package com.createTemplate.model.core.vo;

import com.createTemplate.model.core.pojo.TPerson;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity(name = "T_PERSON")
@ApiModel(value = "用户表VO")
@SuppressWarnings("serial")
/*** 用户表***/
@Data
public class TPersonVO extends TPerson implements Serializable {
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
    @ApiModelProperty(value = "微信code")
    private String code;
    @ApiModelProperty(value = "uuid")
    private String uuid;
    @ApiModelProperty(value = "authToken")
    private String authToken;
    @ApiModelProperty(value = "提现金额  以分为单位")
    private Integer advanceMoney;
    @ApiModelProperty(value = "提现渠道 0 提现到微信余额；1提现到银行卡")
    private Integer advanceChannel;
    @ApiModelProperty(value = "充值金额  以分为单位")
    private Integer rechargeMoney;
    @ApiModelProperty(value = "ip")
    private String spbillCreateIp;
    @ApiModelProperty(value = "包括敏感数据在内的完整用户信息的加密数据")
    private String encryptedData;
    @ApiModelProperty(value = "数据进行加密签名的密钥")
    private String sessionKey;
    @ApiModelProperty(value = "加密算法的初始向量")
    private String iv;

}