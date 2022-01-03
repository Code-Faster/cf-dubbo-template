package com.createTemplate.model.core.vo;

import com.createTemplate.model.core.pojo.TAccountDetail;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TAccountDetailVO
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:18 下午
 * @Version 1.0
 */
@Data
public class TAccountDetailVO extends TAccountDetail implements Serializable {
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
    @ApiModelProperty(value = "微信姓名")
    private String nickName;
    @ApiModelProperty(value = "头像地址")
    private String avatarUrl;
    @ApiModelProperty(value = "手机号")
    private String phoneNum;
}