/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.provider.base.dubbo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.createTemplate.api.base.dubbo.service.RoleService;
import com.createTemplate.api.util.PropertyUtils;
import com.createTemplate.api.util.StringUtil;
import com.createTemplate.model.admin.system.pojo.Button;
import com.createTemplate.model.admin.system.pojo.Menu;
import com.createTemplate.model.admin.system.pojo.Role;
import com.createTemplate.model.admin.system.pojo.RoleMenu;
import com.createTemplate.model.admin.system.vo.ButtonVo;
import com.createTemplate.model.admin.system.vo.MenuVo;
import com.createTemplate.model.admin.system.vo.RoleMenuVo;
import com.createTemplate.model.admin.system.vo.RoleVo;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.mybatis.page.PageParameter;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;

@Service(version = "1.0.0", retries = -1, timeout = 6000)
public class RoleServiceImpl extends MybatisDaoImpl implements RoleService {

    public static final String className = "role";

    /**
     * 角色分页查询
     *
     * @return
     * @Version: V1.00
     */
    @Override
    public Grid getRolePage(RoleVo roleVo) {
        roleVo.setColumn(PropertyUtils.getPropertyNames(Role.class));
        roleVo.setPageParameter(new PageParameter(roleVo.getPage(), roleVo.getRows()));
        Grid grid = new Grid();
        List<RoleVo> list = this.baseDao.selectList(className + ".getRolePage", roleVo);
        grid.setCount(Long.valueOf(roleVo.getPageParameter().getTotalCount()));
        grid.setList(list);
        return grid;
    }

    /**
     * 角色查询
     *
     * @param : roleName 角色名称
     * @Version: V1.00
     * @return：返回查询对象
     */
    @Override
    public List<Role> listRoleByRoleName(String roleName) {
        RoleVo roleVo = new RoleVo();
        roleVo.setColumn(PropertyUtils.getPropertyNames(Role.class));
        roleVo.setRoleName(roleName);
        return this.baseDao.selectList(className + ".findRoleByRoleName", roleVo);
    }

    /**
     * 角色查询
     *
     * @param : roleName 角色名称
     * @Version: V1.00
     * @return：返回查询对象
     */
    @Override
    public List<Role> listAllRole() {
        RoleVo roleVo = new RoleVo();
        roleVo.setColumn(PropertyUtils.getPropertyNames(Role.class));
        return this.baseDao.selectList(className + ".findAllRole", roleVo);
    }

    /**
     * 根据roleIds删除角色
     *
     * @param roleIds
     * @return 1 成功 0 失败
     * @Version: V1.00
     */
    @Override
    public void deleteRoleById(String roleIds) {
        RoleVo roleVo = new RoleVo();
        roleVo.setIds(roleIds);
        this.baseDao.delete(className + ".deleteRoleById", roleVo);
    }

    /**
     * 菜单查询
     *
     * @param :roleId 角色Id
     * @Version: V1.00
     * @return：返回查询对象
     */
    @Override
    public List<Map> getMenu(Long roleId) {
        Role role = new Role();
        role.setId(roleId);
        List<Map> menuList = this.baseDao.selectList(className + ".findMenuByRoleId", role);
        Map<String, String> buttonMap = new HashMap<String, String>(); // key为菜单英文名称+按钮英文名称
        for (Map tempMap : menuList) {
            String menuEName = tempMap.get("menu_ename").toString();
            String buttonEName = "";
            if (tempMap.get("button_ename") != null) {
                buttonEName = tempMap.get("button_ename").toString();
            }
            if (StringUtils.isNotBlank(buttonEName)) {
                for (String temp : buttonEName.split("\\,")) {
                    buttonMap.put(menuEName + "|" + temp, menuEName + "|" + temp);
                }
            }
        }
        List<Button> buttonList = this.findButton();
        for (Button tempButton : buttonList) {
            Map map = new HashMap();
            map.put("id", tempButton.getMenuId() + "_" + tempButton.getButtonEname());
            map.put("menu_cname", tempButton.getButtonCname());
            map.put("parent_id", tempButton.getMenuId());
            if (buttonMap.containsKey(tempButton.getMenuEname() + "|" + tempButton.getButtonEname())) {
                map.put("selected", "1");
            } else {
                map.put("selected", "0");
            }
            menuList.add(map);
        }
        return menuList;
    }

    /**
     * 查询按钮
     *
     * @Description:
     * @version V1.0
     */
    public List<Button> findButton() {
        ButtonVo buttonVo = new ButtonVo();
        buttonVo.setColumn(PropertyUtils.getPropertyNames(Button.class));
        return this.baseDao.selectList(className + ".findButton", buttonVo);
    }

    /**
     * 更新
     */
    @Override
    public void updateRole(Role role, String menuIds) {
        /** 更新角色 */
        this.baseDao.update(className + ".updateRole", role);

        // 先删除原先的rolemenu数据
        deleteOldRoleMenuInfo(role.getId());
        // 插入新的rolemenu数据
        if (StringUtils.isNotBlank(menuIds)) {
            insertRoleMenuInfo(menuIds, role.getId());
        }
    }

    /**
     * 删除原先的rolemenu数据
     *
     * @param id
     */
    private void deleteOldRoleMenuInfo(Long id) {
        RoleMenuVo roleMenuVo = new RoleMenuVo();
        roleMenuVo.setRoleId(id);
        this.baseDao.delete(className + ".deleteRoleMenu", roleMenuVo);
    }

    /**
     * 添加角色
     *
     * @param roleName
     * @param remark
     * @param menuIds
     * @Version: V1.00
     */
    @Override
    public Long saveRole(String roleName, String remark, Integer status, String menuIds) {
        // 保存角色
        Role role = new Role();
        role.setRoleName(roleName);
        role.setRemark(remark);
        role.setStatus(status);
        Long id = this.save(role);
        // 插入rolemenu数据
        if (StringUtil.checkNotNull(menuIds)) {
            insertRoleMenuInfo(menuIds, role.getId());
        }
        return id;
    }

    /**
     * 插入rolemenu数据
     *
     * @param roleId
     */
    private void insertRoleMenuInfo(String ids, Long roleId) {
        // 根据menuIds查询RoleMenu信息,用以获取menuEName信息

        Map<Long, String> buttonMap = new HashMap<Long, String>(); // key为菜单id；value为按钮英文名称
        // 多个逗号分隔
        String menuIds = "";
        for (String temp : ids.split("\\,")) {
            if (temp.contains("_")) {
                String[] array = temp.split("\\_");
                Long menuId = Long.parseLong(array[0]);
                String buttonEName = array[1];
                String tempButtonEName = "";
                if (buttonMap.containsKey(menuId)) {
                    tempButtonEName = buttonMap.get(menuId);
                    tempButtonEName = tempButtonEName + ",";
                    tempButtonEName = tempButtonEName + buttonEName;
                } else {
                    tempButtonEName = buttonEName;
                }
                buttonMap.put(menuId, tempButtonEName);
                // if(!"".equals(buttonIds)){
                // buttonIds = buttonIds + ",";
                // }
                // buttonIds = buttonIds + temp;
            } else {
                if (!"".equals(menuIds)) {
                    menuIds = menuIds + ",";
                }
                menuIds = menuIds + temp;
            }
        }

        MenuVo menuVo = new MenuVo();
        menuVo.setColumn(PropertyUtils.getPropertyNames(Menu.class));
        menuVo.setIds(menuIds);
        List<Menu> menus = this.baseDao.selectList("menu.findByIds", menuVo);
        Map<Long, String> menuENameMap = new HashMap<Long, String>();
        for (Menu menu : menus) {
            // 封装menuEName信息到menuENameMap中，其中key是menuId，value是menuEName
            menuENameMap.put(menu.getId(), menu.getMenuEname());
        }

        // 保存roleMenu数据
        for (String idStr : menuIds.split("\\,")) {
            if ("".equals(idStr)) {
                continue;
            }
            Long menuId = Long.parseLong(idStr);
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuEname(menuENameMap.get(menuId));
            roleMenu.setButtonEname(buttonMap.get(menuId));
            this.save(roleMenu);
        }
    }

    /**
     * 校验角色名是否已存在
     *
     * @return true 通过校验 false 校验不通过
     * @Version: V1.00
     */
    @Override
    public boolean checkExistRoleName(Role role) {
        List<Role> roles = this.listRoleByRoleName(role.getRoleName());
        if (null == roles || roles.size() == 0) {
            return true;
        }
        if (null != role.getId()) {
            if (roles.size() == 1 && (roles.get(0).getId().equals(role.getId()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据角色id查询拥有的菜单权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<MenuVo> listByRoleId(Long roleId) {
        RoleMenuVo condition = new RoleMenuVo();
        condition.setRoleId(roleId);
        condition.setColumn(PropertyUtils.getPropertyNames(RoleMenu.class));
        List<MenuVo> parentList = this.baseDao.selectList(className + ".findParentByRoleId", condition);
        List<MenuVo> sonList = this.baseDao.selectList(className + ".findSonByRoleId", condition);
        parentList.addAll(sonList);
        return parentList;
    }

    /**
     * 修改角色状态值
     */
    @Override
    public Role updateRoleStatus(Long id, Integer status) {
        Role role = new Role();
        role.setId(id);
        Role roleInfo = (Role) this.findOne(role);
        if (roleInfo == null) {
            throw new BusinessException("数据不存在");
        }
        roleInfo.setStatus(status);
        super.update(roleInfo);
        return roleInfo;
    }

    @Override
    public List<RoleMenu> listByRoleIds(Set<Long> roleIds) {
        RoleVo condition = new RoleVo();
        condition.setRoleIds(roleIds);
        condition.setColumn(PropertyUtils.getPropertyNames(RoleMenu.class, "rm"));
        return this.baseDao.selectList(className + ".findRoleMenuByRoleIds", condition);
    }

    /**
     * @param roleIds
     * @return
     */
    @Override
    public List<Menu> listMenuByRoleIds(Set<Long> roleIds) {
        RoleVo condition = new RoleVo();
        condition.setRoleIds(roleIds);
        condition.setColumn(PropertyUtils.getPropertyNames(Menu.class, "m"));
        return this.baseDao.selectList(className + ".findMenuListByRoleIds", condition);
    }

}
