/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.api.core.doubbo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.createTemplate.api.base.dao.MybatisDao;
import com.createTemplate.model.core.pojo.TPerson;
import com.createTemplate.model.core.pojo.TRechargeOrder;
import com.createTemplate.model.core.vo.TPersonVO;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.Grid;

/**
 * 用户接口service
 *
 * @version V1.0
 * @author:
 * @date: 2018年6月23日 下午4:33:12
 */
public interface PersonService extends MybatisDao {

    /***
     * 根据用户ID 更新用户信息
     *
     * @param person
     * @author libiqi
     */
    void updatePersonById(TPersonVO person) throws BusinessException;

    /**
     * 根据ID查询用户
     *
     * @param id
     * @return
     * @author libiqi
     */
    TPerson findById(Long id) throws BusinessException;

    /**
     * 根据IDs查询用户
     *
     * @param ids
     * @return
     * @author libiqi
     */
    List<TPerson> findByIds(Set<Long> ids) throws BusinessException;

    /**
     * 分页查询person lname name beginDate endDate
     *
     * @param vo
     * @return
     * @author libiqi
     */
    Grid findPersonPage(TPersonVO vo) throws BusinessException;

    /**
     * 将用户信息插入redis 根据ID
     *
     * @param PersonVo
     * @return
     * @author libiqi
     */
    Map<String, Object> setPersonToRedis(TPersonVO PersonVo) throws BusinessException;

    /**
     * 根据token获取当前用户session
     *
     * @param authToken
     * @return
     * @author libiqi
     */
    TPersonVO getCurrentUser(String authToken) throws BusinessException;

    /**
     * 清空redis
     *
     * @param id
     * @date 2018年5月15日
     */
    public void clearPersonRedis(Long id);

    /**
     * 根据用户ID查询用户详情
     *
     * @param id
     * @return
     */
    TPersonVO findPersonDetailById(Long id) throws BusinessException;

    /**
     * 根据小程序ID 登录 获取用户信息
     *
     * @author libiqi
     */
    public TPersonVO saveOrloginByAppletOpenId(String softOpenId, JSONObject userInfo) throws BusinessException;

    /**
     * 充值
     *
     * @param tPersonVO
     * @return
     * @throws BusinessException
     */
    public String accountsRecharge(TPersonVO tPersonVO) throws BusinessException;

    /**
     * 提现
     *
     * @param tPersonVO
     * @return
     * @throws BusinessException
     */
    public void withdrawDeposit(TPersonVO tPersonVO) throws BusinessException;

    /**
     * 充值订单回调处理
     *
     * @param out_trade_no
     * @param transaction_id
     * @throws BusinessException
     */
    public void rechargeOrderCall(String out_trade_no, String transaction_id) throws BusinessException;

    /**
     * 查询充值订单
     *
     * @param id
     * @param person_id
     * @param status
     * @param recharge_type
     * @return
     */
    public TRechargeOrder findTRechargeOrder(Long id, String order_no, Long person_id, String status,
                                             Integer recharge_type);

    /**
     * 获取用户总余额
     *
     * @param person_id
     * @return
     */
    public int queryPersonSurplusMoeny(Long person_id);

    /**
     * 获取用户可用余额
     *
     * @param person_id
     * @return
     */
    public int queryPersonFreeSurplusMoeny(Long person_id);

    /**
     * 获取用户冻结余额
     *
     * @param person_id
     * @return
     */
    public int queryPersonFreezonSurplusMoeny(Long person_id);

    /**
     * 查询我的推荐
     *
     * @param vo
     * @return
     */
    public Grid<TPersonVO> findRecommendPage(TPersonVO vo) throws BusinessException;

    /**
     * 获取小程序信息
     *
     * @param code
     * @return
     */
    JSONObject getSessionKeyOropenid(String code) throws BusinessException;

    /**
     * 查询用户余额详情
     *
     * @param person_id
     * @return
     * @author xiongfeng
     */
    Map getAccountBalanceDetail(Long person_id) throws BusinessException;
}
