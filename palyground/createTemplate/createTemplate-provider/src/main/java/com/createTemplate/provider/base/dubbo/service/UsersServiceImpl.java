/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.provider.base.dubbo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.api.base.dubbo.service.UsersService;
import com.createTemplate.api.util.DataUtils;
import com.createTemplate.api.util.PropertyUtils;
import com.createTemplate.api.util.StringUtil;
import com.createTemplate.api.util.TokenProcessor;
import com.createTemplate.model.admin.system.pojo.Users;
import com.createTemplate.model.admin.system.vo.UsersVo;
import com.createTemplate.model.constant.RedisConstant;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.mybatis.page.PageParameter;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;

@Service(version = "1.0.0", retries = -1, timeout = 6000)
public class UsersServiceImpl extends MybatisDaoImpl implements UsersService {

    public static final String className = "users";

    @Autowired
    private CommonRedisDao commonRedisDao;

    /**
     * 保存用户信息
     *
     * @param users
     */
    public void saveUsers(UsersVo users) {
        // 如果数据库有此用户
        if (checkUserName(users)) {
            throw new BusinessException("请勿重复添加!");
        }
        users.setInputDate(new Date());
        users.setUpdateDate(new Date());
        Users user = new Users();
        DataUtils.copyPropertiesIgnoreNull(users, user);
        this.save(user);
    }

    /**
     * 根据用户名密码查询用户
     *
     * @param userName
     * @param userPassword
     * @return
     */
    public Users findByUsersNamePassword(String userName, String userPassword) {
        UsersVo usersVo = new UsersVo();
        usersVo.setUserName(userName);
        usersVo.setUserPassword(userPassword);
        usersVo.setColumn(PropertyUtils.getPropertyNames(Users.class));
        return (Users) this.baseDao.selectOne(className + ".findByUsersNamePassword", usersVo);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return
     */
    public Users findByUserName(String userName) {
        UsersVo usersVo = new UsersVo();
        usersVo.setUserName(userName);
        usersVo.setColumn(PropertyUtils.getPropertyNames(Users.class));
        return (Users) this.baseDao.selectOne(className + ".findByUserName", usersVo);

    }

    /**
     * 分页查询用户
     *
     * @param usersVo
     * @return
     */
    public Grid findPage(UsersVo usersVo) {
        if (null == usersVo.getRows() || null == usersVo.getRows()) {
            throw new BusinessException("page 、 rows 不能为空 ");
        }
        usersVo.setColumn(PropertyUtils.getPropertyNames(Users.class, "u"));
        usersVo.setPageParameter(new PageParameter(usersVo.getPage(), usersVo.getRows()));

        List<UsersVo> list = this.baseDao.selectList(className + ".findPage", usersVo);
        Grid grid = new Grid();
        grid.setCount(Long.valueOf(usersVo.getPageParameter().getTotalCount()));
        grid.setList(list);
        return grid;
    }

    /**
     * 校验用户名是否重复
     *
     * @param users
     * @return false 表示用户名不存在；true表示已存在
     */
    @Override
    public boolean checkUserName(Users users) {
        Users dbUsers = this.findByUserName(users.getUserName());
        if (dbUsers == null) {
            return false;
        }
        if (users.getId() == null) {
            return true;
        } else {
            if (users.getId().longValue() != dbUsers.getId().longValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据角色id查询这些拥有这些角色的用户
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Users> findUsersByRoleIds(String roleIds) {
        if (roleIds == null || "".equals(roleIds))
            return new ArrayList<Users>();
        UsersVo usersVo = new UsersVo();
        usersVo.setColumn(PropertyUtils.getPropertyNames(Users.class));
        usersVo.setRoleIds(roleIds);
        return this.baseDao.selectList(className + ".findUsersByRoleIds", usersVo);
    }

    /**
     * 根据角色id查询这些拥有这些角色的用户
     *
     * @param roleId
     * @return
     */
    @Override
    public int findCountByRoleId(Long roleId) {
        if (roleId == null)
            return 0;
        Long count = this.baseDao.selectOne(className + ".findCountByRoleId", roleId);
        return count.intValue();
    }

    /**
     * 修改用户信息
     *
     * @param users
     */
    @Override
    public void updateUsers(Users users) {
        if (this.checkUserName(users)) {
            throw new BusinessException("用户名重复");
        }
        Users dbUsers = this.findById(users.getId());
        dbUsers.setUserName(users.getUserName());
        dbUsers.setStatus(users.getStatus());
        dbUsers.setRoleId(users.getRoleId());
        dbUsers.setRealName(users.getRealName());
        dbUsers.setUpdateDate(new Date());
        super.update(dbUsers);
    }

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    public Users findById(Long id) {
        UsersVo usersVo = new UsersVo();
        usersVo.setId(id);
        usersVo.setColumn(PropertyUtils.getPropertyNames(Users.class));
        return (Users) this.baseDao.selectOne(className + ".findById", usersVo);
    }

    /**
     * 更新密码
     *
     * @param users
     * @param userPassword
     */
    @Override
    public void updatePassword(Users users, String userPassword) {
        users.setUserPassword(userPassword);
    }

    /**
     * 生成token 存入redis 根据id 生成 如果传入token则不重新生成
     *
     * @param vo
     * @return
     */
    @Override
    public Map<String, Object> setUserToRedis(UsersVo vo) {
        if (null == vo) {
            throw new BusinessException("用户不能为空!");
        }
        this.clearUserRedis(vo.getId());
        String seed = StringUtil.md5(vo.getId().toString());
        String token;
        Integer expireTime = 60 * 24 * 7;
        if (StringUtils.isBlank(vo.getAuthToken())) {
            token = TokenProcessor.getInstance().generateToken(seed);
        } else {
            token = vo.getAuthToken();
        }
        commonRedisDao.set(RedisConstant.userSeed + seed, token);
        // 设置登录失效 7天
        commonRedisDao.set(RedisConstant.userToken + token, JSON.toJSONString(vo), expireTime);
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回token
        map.put("token", token);
        // 返回失效时间
        map.put("expireTime", expireTime);
        return map;
    }

    /**
     * 清空redis
     *
     * @param id
     */
    @Override
    public void clearUserRedis(Long id) {
        if (null != id) {
            String seed = StringUtil.md5(id.toString());
            String token = commonRedisDao.get(RedisConstant.userSeed + seed);
            if (null != token) {
                commonRedisDao.delete(RedisConstant.userToken + token);
            }
        } else {
            throw new BusinessException("id不能为空!");
        }
    }

    /**
     * 根据token获取当前用户session
     *
     * @param authToken
     * @return
     */
    @Override
    public UsersVo getCurrentUser(String authToken) {
        String redisPerson = commonRedisDao.get(RedisConstant.userToken + authToken);
        if (StringUtils.isBlank(redisPerson)) {
            return null;
        }
        UsersVo vo = JSON.parseObject(redisPerson, UsersVo.class);
        vo.setAuthToken(authToken);
        return vo;
    }
}
