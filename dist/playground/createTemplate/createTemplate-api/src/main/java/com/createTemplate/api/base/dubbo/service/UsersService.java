package com.createTemplate.api.base.dubbo.service;

import java.util.List;
import java.util.Map;

import com.createTemplate.model.admin.system.pojo.Users;
import com.createTemplate.model.admin.system.vo.UsersVo;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.Grid;

/**
 * 用户service
 *
 */
public interface UsersService {

    /**
     * 根据用户名密码查询用户（登陆，修改用户用此方法） 保存用户信息 密码由前端MD5加密
     * 
     * @param users
     *            用户对象
     */
    public void saveUsers(UsersVo users) throws BusinessException;

    /**
     * 根据用户名密码查询用户 密码由前端MD5加密
     * 
     * @param userName
     *            用户名
     * @param userPassword
     *            用户密码
     * @return 查询结果
     */
    public Users findByUsersNamePassword(String userName, String userPassword) throws BusinessException;

    /**
     * 修改密码
     * 
     * @author aj
     * @Version: V1.00
     * @Create: 2014-11-11 上午10:33:56
     * @param userName
     *            用户名
     * @param userPassword
     *            用户密码
     */
    public void updatePassword(Users users, String userPassword) throws BusinessException;

    /**
     * 根据用户名查询用户信息
     * 
     * @author aj
     * @Version: V1.00
     * @Create: 2014-11-11 上午10:33:56
     * @param userName
     *            用户名
     */
    public Users findByUserName(String userName) throws BusinessException;

    /**
     * 分页查询用户
     * 
     * @author aj
     * @Version: V1.00
     * @Create: 2014-11-11 上午10:33:56
     * @param：key 查询条件
     */
    public Grid findPage(UsersVo usersVo) throws BusinessException;

    /**
     * 校验用户名是否重复
     * 
     * @param users
     * @return false 表示用户名不存在；true表示已存在
     */
    public boolean checkUserName(Users users) throws BusinessException;

    /**
     * 根据角色id查询这些拥有这些角色的用户
     * 
     * @auther xf
     * @date 2015-1-16 下午3:27:37
     * @param roleIds
     * @return
     */
    public List<Users> findUsersByRoleIds(String roleIds) throws BusinessException;

    /**
     * 根据角色id查询这些拥有这些角色的用户
     * 
     * @param roleId
     * @return
     */
    public int findCountByRoleId(Long roleId) throws BusinessException;

    /**
     * 更新用户信息
     * 
     * @param users
     */
    void updateUsers(Users users) throws BusinessException;

    /**
     * 根据ID查询用户
     * 
     * @param id
     * @return
     */
    public Users findById(Long id) throws BusinessException;

    /**
     * 生成token 存入redis 根据id 生成 如果传入token则不重新生成
     * 
     * @param vo
     * @return
     */
    Map<String,Object> setUserToRedis(UsersVo vo) throws BusinessException;

    /**
     * 清空redis
     * 
     * @param id
     */
    void clearUserRedis(Long id) throws BusinessException;

    /**
     * 根据token获取当前用户session
     * 
     * @param authToken
     * @return
     */
    UsersVo getCurrentUser(String authToken) throws BusinessException;
}
