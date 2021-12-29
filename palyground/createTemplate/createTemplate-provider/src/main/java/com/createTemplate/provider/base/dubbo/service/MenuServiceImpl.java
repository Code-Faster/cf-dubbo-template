/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.provider.base.dubbo.service;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.BuilderException;

import com.createTemplate.api.base.dubbo.service.MenuService;
import com.createTemplate.api.util.DataUtils;
import com.createTemplate.api.util.PropertyUtils;
import com.createTemplate.model.admin.system.pojo.Button;
import com.createTemplate.model.admin.system.pojo.Menu;
import com.createTemplate.model.admin.system.vo.ButtonVo;
import com.createTemplate.model.admin.system.vo.MenuVo;
import com.createTemplate.model.constant.CommonConstant;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.mybatis.page.PageParameter;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;

@Service(version = "1.0.0",retries=0,timeout=6000)
public class MenuServiceImpl extends MybatisDaoImpl implements MenuService {

	public static final String className = "menu";

	/**
	 * 分页查询菜单
	 */
	@Override
	public Grid getMenuPage(MenuVo menuVo) {
		menuVo.setColumn(PropertyUtils.getPropertyNames(Menu.class));
		menuVo.setPageParameter(new PageParameter(menuVo.getPage(), menuVo.getRows()));
		List<MenuVo> list = this.baseDao.selectList(className + ".findPage", menuVo);
		Grid grid = new Grid();
		grid.setCount(Long.valueOf(menuVo.getPageParameter().getTotalCount()));
		grid.setList(list);
		return grid;
	}
	/**
	 * 保存 menu
	 */
	@Override
	public Long saveMenu(Menu menu) {
		if(StringUtils.isBlank(menu.getMenuCname()) || StringUtils.isBlank(menu.getMenuEname())
				|| StringUtils.isBlank(menu.getLevel())){
			throw new BusinessException("缺少菜单中文名、英文名、级别参数!");
		}
		menu.setStatus(CommonConstant.STATUS_USED);
		return this.save(menu);
	}
	/**
	 * 修改menu
	 */
	@Override
	public void updateMenu(Menu menu) {
		Menu conditionObj = new Menu();
		conditionObj.setId(menu.getId());
		Menu oldMenu = (Menu) this.findOne(conditionObj);
		DataUtils.copyPropertiesIgnoreNull(menu, oldMenu);
		super.update(menu);
	}
	/**
	 * 分页查询按钮
	 */
	@Override
	public Grid getButtonPage(ButtonVo buttonVo) {
		buttonVo.setColumn(PropertyUtils.getPropertyNames(Button.class));
		buttonVo.setPageParameter(new PageParameter(buttonVo.getPage(), buttonVo.getRows()));
		List<ButtonVo> list = this.baseDao.selectList(className + ".findButtonPage", buttonVo);
		Grid grid = new Grid();
		grid.setCount(Long.valueOf(buttonVo.getPageParameter().getTotalCount()));
		grid.setList(list);
		return grid;
	}
	/**
	 * 新增按钮
	 */
	@Override
	public void saveButton(Button button) {
		Menu menuInfo=new Menu();
		menuInfo.setId(button.getMenuId());
		Menu menu=(Menu)this.findOne(menuInfo);
		if(menu==null){
			throw new BuilderException("菜单不存在");
		}
		button.setStatus(CommonConstant.STATUS_USED);
		button.setMenuEname(menu.getMenuEname());
		this.save(button);
	}
	/**
	 * 更新按钮
	 */
	@Override
	public void updateButton(Button button) {
		Button conditionObj = new Button();
		conditionObj.setId(button.getId());
		Button oldObj = (Button) this.findOne(conditionObj);
		DataUtils.copyPropertiesIgnoreNull(button, oldObj);
		super.update(button);
	}

	/**
	 * 修改菜单状态
	 */
	@Override
	public void updateMenuStatus(Menu menu) {
		Menu menuInfo=new Menu();
		menuInfo.setId(menu.getId());
		Menu menuDB=(Menu)this.findOne(menuInfo);
		if(menuDB==null){
			throw new BuilderException("菜单不存在");
		}
		super.update(menu);
	}

	/**
	 * 修改按钮状态
	 */
	@Override
	public void updateButtonStatus(Button button) {
		Button buttonInfo=new Button();
		buttonInfo.setId(button.getId());
		Button buttonInfoDB=(Button)this.findOne(buttonInfo);
		if(buttonInfoDB==null){
			throw new BuilderException("按钮不存在");
		}
		super.update(button);
	}
}
