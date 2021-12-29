/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.admin.web.core.controller;

import java.util.List;
import java.util.Map;

import com.createTemplate.model.exception.ResultEnum;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.base.dubbo.service.RoleService;
import com.createTemplate.api.base.dubbo.service.UsersService;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.api.util.StringUtil;
import com.createTemplate.model.admin.system.pojo.Role;
import com.createTemplate.model.admin.system.vo.RoleVo;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.model.resultvo.ResultInfo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/*
 * 角色菜单控制层
 *
 */
@RestController
@RequestMapping(value = "service/role")
public class RoleController extends CommonBaseController {

    @Reference(version = "1.0.0", check = false)
    RoleService roleService;
    @Reference(version = "1.0.0", check = false)
    private UsersService usersService;

    /**
     * 角色分页查询
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "rows", value = "行数", required = true, dataType = "Integer", paramType = "query")})
    @ApiOperation(value = "getRolePage", notes = "角色分页查询")
    @RequestMapping(value = "/getRolePage", method = RequestMethod.POST)
    public ResultInfo<Grid> getRolePage(@RequestBody(required = false) RoleVo roleVo) {
        if (roleVo.getPage() == null || roleVo.getRows() == null) {
            throw new BusinessException("page,rows不能为空");
        }
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "查询成功", roleService.getRolePage(roleVo));
    }

    /**
     * 角色查询
     *
     * @return
     * @Version: V1.00
     */
    @RequestMapping(value = "/listAllRole", method = RequestMethod.POST)
    public ResultInfo<List<Role>> listAllRole(@RequestBody(required = false) Role role) {
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "查询成功", roleService.listAllRole());
    }

    /**
     * 菜单查询
     *
     * @return
     * @Version: V1.00
     */
    @RequestMapping(value = "/getMenu", method = RequestMethod.POST)
    public ResultInfo<List<Map>> getMenu(@RequestBody(required = false) Role role) {
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "查询成功", roleService.getMenu(role.getId()));
    }

    /**
     * 保存角色
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "remark", value = "描述", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "role_name", value = "角色名称", required = true, dataType = "String", paramType = "query")})
    @ApiOperation(value = "saveRole", notes = "保存角色")
    @RequestMapping(value = "/saveRole", method = RequestMethod.POST)
    public ResultInfo<Object> saveRole(@RequestBody(required = false) RoleVo role) {
        if (role.getRemark().isEmpty()) {
            throw new BusinessException("描述不能为空");
        }
        if (role.getRoleName().isEmpty()) {
            throw new BusinessException("名称不能为空");
        }
        // 首先验证是否重复
        if (!roleService.checkExistRoleName(role)) {
            throw new BusinessException("角色名称已重复");
        }
        roleService.saveRole(role.getRoleName(), role.getRemark(), role.getStatus(), role.getMenuIds());
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "保存成功", null);
    }

    /**
     * 更新角色
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")})
    @ApiOperation(value = "updateRole", notes = "更新角色")
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public ResultInfo<Object> updateRole(@RequestBody(required = false) RoleVo role) {
        if (StringUtil.checkNull(role.getRoleName())) {
            throw new BusinessException("角色名称不能为空");
        }
        // 首先验证是否重复
        if (!roleService.checkExistRoleName(role)) {
            throw new BusinessException("角色名称已重复");
        }
        roleService.updateRole(role, role.getMenuIds());
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "更新成功", null);

    }

    /**
     * 修改角色状态
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")})
    @ApiOperation(value = "updateRoleStatus", notes = "修改角色状态")
    @RequestMapping(value = "/updateRoleStatus", method = RequestMethod.POST)
    public ResultInfo<Object> updateRoleStatus(@RequestBody(required = false) Role role) {
        if (role.getId() == null || role.getStatus() == null) {
            throw new BusinessException("id、状态值不能为空");
        }
        roleService.updateRoleStatus(role.getId(), role.getStatus());
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "更新成功", null);
    }
}
