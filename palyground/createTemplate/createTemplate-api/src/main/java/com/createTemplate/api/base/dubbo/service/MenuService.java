package com.createTemplate.api.base.dubbo.service;


import com.createTemplate.api.base.dao.MybatisDao;
import com.createTemplate.model.admin.system.pojo.Button;
import com.createTemplate.model.admin.system.pojo.Menu;
import com.createTemplate.model.admin.system.vo.ButtonVo;
import com.createTemplate.model.admin.system.vo.MenuVo;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.Grid;

public interface MenuService extends MybatisDao {
	
	/**
	 * 分页查询菜单
	 * @param menuVo
	 * @return Grid
	 */
	public Grid getMenuPage(MenuVo menuVo) throws BusinessException;
	
	/**
	 * 保存菜单信息
	 * @return 
	 * 
	 */
	public Long saveMenu(Menu menu) throws BusinessException;

	/**
	 * 更新菜单信息
	 */
	public void updateMenu(Menu menu) throws BusinessException;
	
	/**
	 * 修改菜单状态
	 */
	public void updateMenuStatus(Menu menu) throws BusinessException;
	
	/**
	 * 分页查询按钮
	 * @param buttonVo
	 * @return Grid
	 */
	public Grid getButtonPage(ButtonVo buttonVo) throws BusinessException;
	
	/**
	 * 保存按钮信息
	 * 
	 */
	public void saveButton(Button button) throws BusinessException;
	
	/**
	 * 更新按钮信息
	 */
	public void updateButton(Button button) throws BusinessException;
	
	/**
	 * 修改按钮的状态
	 */
	public void updateButtonStatus(Button button) throws BusinessException;
	

}
