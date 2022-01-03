/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.admin.web.core.controller;
import io.swagger.annotations.Api;
import com.createTemplate.model.exception.ResultEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.base.dubbo.service.MenuService;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.api.util.StringUtil;
import com.createTemplate.model.admin.system.pojo.Button;
import com.createTemplate.model.admin.system.pojo.Menu;
import com.createTemplate.model.admin.system.vo.ButtonVo;
import com.createTemplate.model.admin.system.vo.MenuVo;
import com.createTemplate.model.annotation.AntController;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.model.resultvo.ResultInfo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/service/menu")
@Api(value = "菜单接口",tags = {"菜单接口"})
public class MenuController extends CommonBaseController{

	@Reference(version = "1.0.0",check=false)
	private MenuService menuService;

	/**
	 * 菜单分页查询
	 * 
	 * @return
	 * 
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "page", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "rows", required = true, dataType = "int", paramType = "query") })
	@ApiOperation(value = "getMenuPage", notes = "菜单分页查询")
	@RequestMapping(value = "/getMenuPage", method = RequestMethod.POST)
	public ResultInfo<Grid> getMenuPage( @RequestBody(required=false) MenuVo menuVo) {
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "查询成功", menuService.getMenuPage(menuVo));
	}

	/**
	 * 保存菜单
	 * 
	 * @return
	 * 
	 */
	@ApiOperation(value = "saveMenu", notes = "保存菜单")
	@RequestMapping(value = "/saveMenu", method = RequestMethod.POST)
	public ResultInfo<Object> saveMenu( @RequestBody(required=false) Menu menu) {
		if (StringUtil.checkNull(menu.getMenuCname()) || StringUtil.checkNull(menu.getMenuEname())) {
			throw new BusinessException("菜单中文名，英文名不能为空");
		}
		menuService.saveMenu(menu);
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "保存成功", null);
	}
	
	/**
	 * 修改菜单
	 * 
	 * @return
	 * 
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
	@ApiOperation(value = "/updateMenu", notes = "修改菜单")
	public ResultInfo<Object> updateMenu( @RequestBody(required=false) Menu menu) {
		if(menu.getId()==null){
			throw new BusinessException("id不能为空");
		}
		menuService.updateMenu(menu);
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "更新成功", null);
	}
	
	/**
	 * 修改菜单状态
	 * 
	 * @return
	 * 
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "status", value = "状态 0 正常；1 禁用", required = true, dataType = "int", paramType = "query")})
	@RequestMapping(value = "/updateMenuStatus", method = RequestMethod.POST)
	@ApiOperation(value = "/updateMenuStatus", notes = "修改菜单状态")
	public ResultInfo<Object> updateMenuStatus( @RequestBody(required=false) Menu menu) {
		if(menu.getId()==null || menu.getStatus()==null){
			throw new BusinessException("id、状态值不能为空");
		}
		menuService.updateMenuStatus(menu);
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "更新成功", null);
	}

	/**
	 * 按钮分页查询
	 * 
	 * @return
	 * 
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "page", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "menu_id", value = "菜单id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "rows", value = "rows", required = true, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/getButtonPage", method = RequestMethod.POST)
	@ApiOperation(value = "getButtonPage", notes = "按钮分页查询")
	public ResultInfo<Grid> getButtonPage( @RequestBody(required=false) ButtonVo buttonVo) {
		if(buttonVo.getPage()==null || buttonVo.getRows()==null || buttonVo.getMenuId()==null){
			throw new BusinessException("page、rows、菜单id不能为空");
		}
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "查询成功",menuService.getButtonPage(buttonVo));
	}

	/**
	 * 按钮保存
	 * 
	 * @return
	 * 
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "button_cname", value = "按钮中文名", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "menu_id", value = "菜单id", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "button_ename", value = "按钮英文名", required = true, dataType = "String", paramType = "query") })
	@RequestMapping(value = "/saveButton", method = RequestMethod.POST)
	@ApiOperation(value = "saveButton", notes = "按钮保存")
	public ResultInfo<Object> saveButton( @RequestBody(required=false) Button button) {
		if (StringUtil.checkNull(button.getButtonCname()) || StringUtil.checkNull(button.getButtonEname()) ||
				button.getMenuId()==null) {
			throw new BusinessException("按钮中文名，英文名不能为空、菜单id不能为空");
		}
		menuService.saveButton(button);
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "保存成功", null);
	}

	/**
	 * 按钮更新
	 * 
	 * @return
	 * 
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query") })
	@RequestMapping(value = "/updateButton", method = RequestMethod.POST)
	@ApiOperation(value = "updateButton", notes = "按钮更新")
	public ResultInfo<Object> updateButton( @RequestBody(required=false) Button button) {
		if(button.getId()==null){
			throw new BusinessException("id不能为空");
		}
		menuService.updateButton(button);
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "更新成功", null);
	}
	
	/**
	 * 修改菜单状态
	 * 
	 * @return
	 * 
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long", paramType = "query"),
		@ApiImplicitParam(name = "status", value = "状态 0 正常；1 禁用", required = true, dataType = "int", paramType = "query")})
	@RequestMapping(value = "/updateButtonStatus", method = RequestMethod.POST)
	@ApiOperation(value = "/updateButtonStatus", notes = "修改菜单状态")
	public ResultInfo<Object> updateButtonStatus( @RequestBody(required=false) Button button) {
		if(button.getId()==null || button.getStatus()==null){
			throw new BusinessException("id、状态值不能为空");
		}
		menuService.updateButtonStatus(button);
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "更新成功", null);
	}
}