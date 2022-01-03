/*
* Copyright (c) 2017-2020 深圳市科瑞特网络科技有限公司 SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.admin.system.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 按钮
 */
@Entity(name = "T_USERS_ROLE")
@ApiModel(value = "用户角色")
@Data
public class UserRole implements java.io.Serializable {
	@Id
	/** id */
	@ApiModelProperty(value = "唯一标识")
	private Long id;

	/** 用户id */
	@ApiModelProperty(value = "用户id")
	private Long custId;

	/** 用户名 */
	@ApiModelProperty(value = "用户名")
	private String userName;

	/** 角色id */
	@ApiModelProperty(value = "角色id")
	private Long roleId;

	/** 状态 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/** 创建时间 */
	@ApiModelProperty(value = "创建时间")
	private Date inputDate;

	/** 修改时间 */
	@ApiModelProperty(value = "修改时间")
	private Date updateDate;

}
