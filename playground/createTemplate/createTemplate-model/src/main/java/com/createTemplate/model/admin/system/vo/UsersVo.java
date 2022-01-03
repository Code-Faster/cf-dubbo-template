/*
 * Copyright (c) 2017-2020 深圳市科瑞特网络科技有限公司 SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.admin.system.vo;

import com.createTemplate.model.admin.system.pojo.Users;
import com.createTemplate.model.annotation.AntField;
import com.createTemplate.model.mybatis.page.PageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Map;

/**
 * 用户vo
 *
 * @version 1.0
 */
@SuppressWarnings("serial")
@Entity(name = "T_USER")
@ApiModel(value = "管理员用户vo")
@Data
public class UsersVo extends Users implements java.io.Serializable {

    @ApiModelProperty(value = "允许访问的菜单,key 为菜单的英文名称；value 菜单下的按钮权限")
    private Map<String, Map<String, String>> menuMap;

    @ApiModelProperty(value = "列")
    private String column;

    @ApiModelProperty(value = "用户token")
    private String authToken;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "关键字")
    private String key;

    @ApiModelProperty(value = "角色id，多个逗号分隔")
    private String roleIds;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "每页的条数")
    private Integer rows;

    @ApiModelProperty(value = "分页参数")
    private PageParameter pageParameter;

    @ApiModelProperty(value = "失效时间")
    private long expireTime;

    /**
     * 判断是否有权限
     *
     * @param menuEName
     * @return
     */
    public final static boolean checkRole(UsersVo sessionUsersVo, String menuEName, String buttonEName) {
        Map<String, Map<String, String>> menuMap = sessionUsersVo.getMenuMap();
        if (menuMap.containsKey(menuEName)) {
            /** 有权限 */
            Map<String, String> buttonMap = menuMap.get(menuEName);
            if (buttonMap.containsKey(buttonEName)) {
                return true;
            } else {
                return false;
            }
        } else {
            /** 无权限 */
            return false;
        }
    }

}
