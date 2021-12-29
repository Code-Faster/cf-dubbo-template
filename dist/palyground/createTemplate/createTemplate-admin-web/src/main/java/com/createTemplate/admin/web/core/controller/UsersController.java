/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.admin.web.core.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.createTemplate.api.util.EncryptUtil;
import com.createTemplate.model.exception.ResultEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.base.dubbo.service.RoleService;
import com.createTemplate.api.base.dubbo.service.UserRoleService;
import com.createTemplate.api.base.dubbo.service.UsersService;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.api.util.StringUtil;
import com.createTemplate.model.admin.system.pojo.Users;
import com.createTemplate.model.admin.system.vo.MenuVo;
import com.createTemplate.model.admin.system.vo.UsersVo;
import com.createTemplate.model.auth.AuthToken;
import com.createTemplate.model.constant.CommonConstant;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.model.resultvo.ResultInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/service/users")
@Scope(value = "prototype")
@Api(value = "管理员管理接口", tags = {"管理员管理接口"})
public class UsersController extends CommonBaseController {

    @Reference(version = "1.0.0", check = false)
    private UserRoleService userRoleService;

    @Reference(version = "1.0.0", check = false)
    private UsersService usersService;

    @Reference(version = "1.0.0", check = false)
    private RoleService roleService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userPassword", value = "密码 , 统一由前端MD5加密", required = true, dataType = "string", paramType = "query")})
    @ApiOperation(value = "登录", notes = "本人-用户登录 返回map token,expireTime")
    @PostMapping(value = "login")
    public ResultInfo<Map<String, Object>> login(@RequestBody(required = false) UsersVo usersVo) {
        Users dbUsers = usersService.findByUsersNamePassword(usersVo.getUserName(), usersVo.getUserPassword());
        if (dbUsers == null) {
            throw new BusinessException("用户名密码错误");
        } else {
            if (dbUsers.getStatus() != null && dbUsers.getStatus().intValue() == CommonConstant.STATUS_UNUSE) {
                throw new BusinessException("该用户已被禁用，请联系系统管理员");
            }
            UsersVo newUsersVo = new UsersVo();
            newUsersVo.setUserName(dbUsers.getUserName());
            newUsersVo.setRealName(dbUsers.getRealName());
            newUsersVo.setRoleId(dbUsers.getRoleId());
            newUsersVo.setId(dbUsers.getId());
            List<MenuVo> list = roleService.listByRoleId(newUsersVo.getRoleId());
            Map<String, Map<String, String>> menuMap = new HashMap<String, Map<String, String>>();
            for (MenuVo tempRoleMenu : list) {
                Map<String, String> buttonMap = new HashMap<String, String>();
                if (StringUtil.checkNotNull(tempRoleMenu.getButtonEname())) {
                    String[] buttonArray = tempRoleMenu.getButtonEname().split("\\,");
                    for (String buttonEName : buttonArray) {
                        buttonMap.put(buttonEName, buttonEName);
                    }
                }
                buttonMap.put("url", tempRoleMenu.getUrl());
                buttonMap.put("icon", tempRoleMenu.getIconCls());
                buttonMap.put("cname", tempRoleMenu.getMenuCname());
                buttonMap.put("level", tempRoleMenu.getLevel());
                menuMap.put(tempRoleMenu.getMenuEname(), buttonMap);
            }
            newUsersVo.setMenuMap(menuMap);
            return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "登录成功", usersService.setUserToRedis(newUsersVo));
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "rows", value = "行数", required = true, dataType = "int", paramType = "query")})
    @ApiOperation(value = "管理员-分页查询用户", notes = "管理员-分页查询用户")
    @PostMapping(value = "findPage")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<Grid> findPage(@RequestBody(required = false) UsersVo usersVo) {
        UsersVo currentUser = usersService.getCurrentUser(usersVo.getAuthToken());
        System.out.println(CommonConstant.menuEName.glygl);
        if (!UsersVo.checkRole(currentUser, CommonConstant.menuEName.glygl,
                CommonConstant.buttonEName.queryButton)) {
            throw new BusinessException("非法操作");
        }
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "查询成功", usersService.findPage(usersVo));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userPassword", value = "密码 , 统一由前端MD5加密", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "RoleId", value = "角色ID", required = true, dataType = "int", paramType = "query")})
    @ApiOperation(value = "管理员-新增用户", notes = "管理员-新增用户")
    @PostMapping(value = "addUsers")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<Object> addUsers(@RequestBody(required = false) UsersVo users) {
        if (StringUtil.checkNull(users.getUserName()) || StringUtil.checkNull(users.getUserPassword()) || users
                .getRoleId() == null) {
            throw new BusinessException("用户名、密码、角色不能为空");
        }
        UsersVo currentUser = usersService.getCurrentUser(users.getAuthToken());
        if (!UsersVo.checkRole(currentUser, CommonConstant.menuEName.glygl, CommonConstant.buttonEName.addButton)) {
            throw new BusinessException("非法操作");
        }
        usersService.saveUsers(users);
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "保存成功", null);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "RoleId", value = "角色ID", required = true, dataType = "int", paramType = "query")})
    @ApiOperation(value = "管理员-更新用户信息", notes = "管理员-更新用户信息")
    @PostMapping(value = "updateUsers")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<Object> updateUsers(@RequestBody(required = false) UsersVo users) {
        if (users.getId() == null || StringUtil.checkNull(users.getUserName()) || users.getRoleId() == null) {
            throw new BusinessException("id、用户名、角色不能为空");
        }
        UsersVo currentUser = usersService.getCurrentUser(users.getAuthToken());
        if (!UsersVo.checkRole(currentUser, CommonConstant.menuEName.glygl, CommonConstant.buttonEName.editButton)) {
            throw new BusinessException("非法操作");
        }
        Users dbUsers = (Users) usersService.findById(users.getId());
        if (null == dbUsers) {
            throw new BusinessException("没有此用户");
        }
        usersService.updateUsers(users);
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "保存成功", null);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "Status", value = "状态", required = true, dataType = "int", paramType = "query")})
    @ApiOperation(value = "管理员-禁用/激活用户", notes = "管理员-禁用/激活用户")
    @PostMapping(value = "updateStatus")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<Object> updateStatus(@RequestBody(required = false) UsersVo usersVo) {
        if (usersVo.getId() == null) {
            throw new BusinessException("缺少ID参数");
        }
        Users dbUsers = (Users) usersService.findById(usersVo.getId());
        if (usersVo.getStatus() == null) {
            throw new BusinessException("状态标识错误");
        }
        UsersVo currentUser = usersService.getCurrentUser(usersVo.getAuthToken());
        if ("0".equals(usersVo.getStatus())) {
            if (!UsersVo.checkRole(currentUser, CommonConstant.menuEName.glygl,
                    CommonConstant.buttonEName.enableButton)) {
                throw new BusinessException("非法操作");
            }
        } else {
            if (!UsersVo.checkRole(currentUser, CommonConstant.menuEName.glygl,
                    CommonConstant.buttonEName.disableButton)) {
                throw new BusinessException("非法操作");
            }
        }
        if (null == dbUsers) {
            throw new BusinessException("没有此用户");
        }
        dbUsers.setStatus(usersVo.getStatus());
        dbUsers.setId(usersVo.getId());
        usersService.updateUsers(dbUsers);
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "保存成功", null);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oldPassword", value = "用户密码 前端MD5加密", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newPassword", value = "新密码 前端MD5加密", required = true, dataType = "String", paramType = "query")})
    @ApiOperation(value = "本人-修改密码", notes = "本人-修改密码")
    @PostMapping(value = "updatePassword")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<Object> updatePassword(@RequestBody(required = false) UsersVo usersVo) {
        UsersVo currentUser = usersService.getCurrentUser(usersVo.getAuthToken());
        String newPassword = StringUtil.objectToString(request.getParameter("newPassword"));
        String oldPassword = StringUtil.objectToString(request.getParameter("oldPassword"));
        Users dbUsers = usersService.findByUsersNamePassword(currentUser.getUserName(), oldPassword);
        if (dbUsers == null) {
            return new ResultInfo<>(ResultEnum.BUSSINESS_EXCEPTION.getCode(), "密码错误", null);
        } else {
            dbUsers.setUpdateDate(new Date());
            usersService.updatePassword(dbUsers, newPassword);
            // 清除用户redis 重新登录
            usersService.clearUserRedis(dbUsers.getId());
            return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "修改成功", null);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token", required = true, dataType = "String", paramType = "query")})
    @ApiOperation(value = "本人-退出登陆", notes = "本人-退出登陆")
    @PostMapping(value = "logout")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<Object> logout(@RequestBody(required = false) UsersVo usersVo) {
        UsersVo currentUser = usersService.getCurrentUser(usersVo.getAuthToken());
        usersService.clearUserRedis(currentUser.getId());
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "退出成功", null);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "UserName", value = "用户名", required = true, dataType = "String", paramType = "query")})
    @ApiOperation(value = "管理员-重置密码", notes = "管理员-重置密码")
    @PostMapping(value = "resetPassword")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<Object> resetPassword(@RequestBody(required = false) UsersVo usersVo) {
        UsersVo currentUser = usersService.getCurrentUser(usersVo.getAuthToken());
        if (!UsersVo.checkRole(currentUser, CommonConstant.menuEName.glygl,
                CommonConstant.buttonEName.resetPwdButton)) {
            throw new BusinessException("非法操作");
        }
        Users dbUsers = usersService.findByUserName(usersVo.getUserName());
        if (dbUsers == null) {
            return new ResultInfo<>(ResultEnum.USER_NOT_EXIT);
        } else {
            dbUsers.setUpdateDate(new Date());
            usersService.updatePassword(dbUsers, EncryptUtil.md5("123456"));
            return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "重置成功", null);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "用户token", required = true, dataType = "String", paramType = "query"),
    })
    @ApiOperation(value = "获取当前用户session", notes = "获取当前用户session")
    @PostMapping(value = "getUser")
    @AuthToken(type = CommonConstant.SESSION_TYPE_ADMIN)
    public ResultInfo<UsersVo> getUser(@RequestBody(required = false) UsersVo usersVo) {
        String authToken = request.getParameter("authToken");
        UsersVo currentUser = usersService.getCurrentUser(authToken);
        return new ResultInfo<UsersVo>(ResultEnum.SUCCESS.getCode(), "成功", currentUser);
    }

}
