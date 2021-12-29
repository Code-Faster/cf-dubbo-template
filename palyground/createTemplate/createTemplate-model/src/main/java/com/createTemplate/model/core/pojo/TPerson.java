package com.createTemplate.model.core.pojo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;

@Entity(name = "T_PERSON")
@ApiModel(value = "用户表")
@SuppressWarnings("serial")
/*** 用户表***/
@Data
public class TPerson implements Serializable {
    @Id
    private Long id;
    @ApiModelProperty(value = "微信姓名")
    private String nickName;
    @ApiModelProperty(value = "头像地址")
    private String avatarUrl;
    @ApiModelProperty(value = "状态 0正常  1禁用")
    private Integer status;
    @ApiModelProperty(value = "微信id")
    private String appId;
    @ApiModelProperty(value = "添加时间")
    private Date inputDate;
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;
    @ApiModelProperty(value = "是否是VIP   0不是   1是")
    private Integer vipFlag;
    @ApiModelProperty(value = "vipk开始时间")
    private Date vipBeginDate;
    @ApiModelProperty(value = "vip到期时间")
    private Date vipEndDate;
    @ApiModelProperty(value = "手机号")
    private String phoneNum;
    @ApiModelProperty(value = "表单id")
    private String formId;
}