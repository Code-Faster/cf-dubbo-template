/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.provider.base.dubbo.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.api.base.dubbo.service.EnumService;
import com.createTemplate.api.util.DataUtils;
import com.createTemplate.api.util.PropertyUtils;
import com.createTemplate.model.base.pojo.EnumObj;
import com.createTemplate.model.base.pojo.EnumType;
import com.createTemplate.model.base.vo.EnumObjVo;
import com.createTemplate.model.base.vo.EnumTypeVo;
import com.createTemplate.model.constant.CommonConstant;
import com.createTemplate.model.constant.RedisConstant;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.mybatis.page.PageParameter;
import com.createTemplate.model.resultvo.Grid;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;

/**
 * 数据字典service类
 *
 * @version V1.0
 * @author: Administrator
 * @date: 2018年5月15日 下午2:03:02
 */
@Service(version = "1.0.0", retries = -1, timeout = 6000)
public class EnumServiceImpl extends MybatisDaoImpl implements EnumService {

    @Autowired
    private CommonRedisDao commonRedisDao;

    public static final String CLASSNAME = "enum";

    /**
     * 数据字典查询
     *
     * @param @param enumObj
     * @return
     * @version V1.0
     */
    @Override
    public List<EnumObj> listEnum(String enumTypeCode, Long upperId) {
        List<EnumObj> list = new ArrayList<EnumObj>();
        /** 从redis中取配置 */
        String str = commonRedisDao.get(RedisConstant.enumByUpperIdFix + enumTypeCode + "_" + upperId);
        if (str != null && !"".equals(str)) {
            list = JSON.parseArray(str, EnumObj.class);
        } else {
            EnumObjVo condition = new EnumObjVo();
            condition.setEnumTypeCode(enumTypeCode);
            condition.setUpperId(upperId);
            condition.setColumn(PropertyUtils.getPropertyNames(EnumObj.class));
            condition.setStatus(0);
            list = this.baseDao.selectList(CLASSNAME + ".findEnum", condition);
            commonRedisDao.set(RedisConstant.enumByUpperIdFix + enumTypeCode + "_" + upperId, JSON.toJSONString(list));
        }
        return list;
    }

    /**
     * 获取数据字典值
     *
     * @return
     */
    @Override
    public String getEnumValue(Long enumId) {

        EnumObj dbEnumObj = this.getEnumObj(enumId);
        if (dbEnumObj == null) {
            return "";
        } else {
            return dbEnumObj.getEnumValue();
        }

    }

    /**
     * @Description:根据id查询
     * @date 2016-6-21 下午08:50:31
     * @version V1.0
     */
    @Override
    public EnumObj findById(Long enumId) {
        EnumObjVo condition = new EnumObjVo();
        condition.setId(enumId);
        condition.setColumn(PropertyUtils.getPropertyNames(EnumObj.class));
        return (EnumObj) this.baseDao.selectOne(CLASSNAME + ".findEnumById", condition);
    }

    /**
     * 获取数据字典对象
     *
     * @param enumId
     * @return
     */
    @Override
    public EnumObj getEnumObj(Long enumId) {
        if (enumId == null || "".equals(enumId)) {
            return new EnumObj();
        }
        EnumObj enumObj = new EnumObj();
        String str = commonRedisDao.get(RedisConstant.enumByIdFix + enumId);
        if (str != null && !"".equals(str)) {
            enumObj = JSON.parseObject(str, EnumObj.class);
        } else {
            enumObj = this.findById(enumId);
            if (enumObj == null) {
                return new EnumObj();
            }
            commonRedisDao.set(RedisConstant.enumByIdFix + enumId, JSON.toJSONString(enumObj));
        }
        return enumObj;
    }

    /**
     * @Description:根据数据字典类型code查询数据字典类型
     * @author aijian
     * @date 2016-6-21 下午08:56:46
     * @version V1.0
     */
    public EnumType findEnumTypeByEnumTypeCode(String enumTypeCode) {
        EnumTypeVo condition = new EnumTypeVo();
        condition.setColumn(PropertyUtils.getPropertyNames(EnumType.class));
        condition.setEnumTypeCode(enumTypeCode);
        return (EnumType) this.baseDao.selectOne(CLASSNAME + ".findEnumType", condition);
    }

    /**
     * 清空redis
     *
     * @return
     * @version V1.0
     */
    @Override
    public void clearEnumRedis(String enumTypeCode, Long upperId, Long enumId) {

        commonRedisDao.delete(RedisConstant.enumByUpperIdFix + enumTypeCode + "_" + upperId);

        if (enumId != null) {
            commonRedisDao.delete(RedisConstant.enumByIdFix + enumId);
        }

    }

    public void setcommonRedisDao(CommonRedisDao commonRedisDao) {
        this.commonRedisDao = commonRedisDao;
    }

    /**
     * @return
     */
    @Override
    public List<EnumType> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 数据字典查询
     *
     * @param @param enumObj
     * @return
     * @author aj
     * @date 2015-3-28 下午01:53:37
     * @version V1.0
     */
    @Override
    public List<EnumObj> findEnum(EnumObj enumObj) {
        List<EnumObj> list = new ArrayList<EnumObj>();
        /** 从redis中取配置 */
        String str = commonRedisDao
                .get(RedisConstant.enumFix + enumObj.getEnumTypeCode() + "_" + enumObj.getUpperEnumKey());
        if (str != null && !"".equals(str)) {
            list = JSON.parseArray(str, EnumObj.class);
        } else {
            ;
            list = this.baseDao.selectList(CLASSNAME + ".findEnumObjPage", enumObj);
            commonRedisDao.set(RedisConstant.enumFix + enumObj.getEnumTypeCode() + "_" + enumObj.getUpperEnumKey(),
                    JSON.toJSONString(list));
        }
        return list;
    }

    /**
     * 生成规则
     *
     * @author aj
     * @param @param
     *            enumObj
     * @param @param
     *            page
     * @param @param
     *            rows
     * @param @return
     * @return
     * @date 2015-3-28 下午01:59:18
     * @version V1.0
     */

    // public GuzzRule generateGuzzRule(EnumObj enumObj, Integer page, Integer
    // rows) {
    // GuzzRule guzzRule = new GuzzRule();
    // guzzRule.addTable(EnumObj.class);
    // guzzRule.addColumn(EnumObj.class);
    // guzzRule.addEqual("enumTypeCode", null, enumObj.getEnumTypeCode());
    // if (enumObj != null && enumObj.getUpperId() != null) {
    // guzzRule.addEqual("upperId", null, enumObj.getUpperId());
    // }
    // if (enumObj != null && enumObj.getUpperEnumKey() != null &&
    // !"".equals(enumObj.getUpperEnumKey())) {
    // guzzRule.addEqual("upperEnumKey", null, enumObj.getUpperEnumKey());
    // } else {
    // guzzRule.addSqlCondition("upperEnumKey is null", null);
    // }
    // if (enumObj != null && enumObj.getEnumKey() != null &&
    // !"".equals(enumObj.getEnumKey())) {
    // guzzRule.addEqual("enumKey", null, enumObj.getEnumKey());
    // }
    // if (enumObj != null && enumObj.getStatus() != null &&
    // !"".equals(enumObj.getStatus())) {
    // guzzRule.addEqual("status", null, enumObj.getStatus());
    // }
    // if (page != null && rows != null) {
    // guzzRule.setBeginIndex(page, rows);
    // guzzRule.setIndexLength(rows);
    // }
    // guzzRule.addOrderColumn("enumTypeCode asc , sortNo asc,id asc");
    // return guzzRule;
    // }

    /**
     * 分页查询
     *
     * @param @param  enumObjVo
     * @param @param  page
     * @param @param  rows
     * @param @return
     * @return
     * @author aj
     * @date 2015-4-12 上午11:13:26
     * @version V1.0
     */
    @Override
    public Grid getPage(EnumObjVo enumObjVo) {
        enumObjVo.setColumn(PropertyUtils.getPropertyNames(EnumObj.class, "e"));
        enumObjVo.setPageParameter(new PageParameter(enumObjVo.getPage(), enumObjVo.getRows()));
        List<EnumObjVo> list = this.baseDao.selectList(CLASSNAME + ".findPage", enumObjVo);
        Grid grid = new Grid();
        grid.setCount(Long.valueOf(enumObjVo.getPageParameter().getTotalCount()));
        grid.setList(list);
        return grid;
    }

    /**
     * 查询数据字典类型
     *
     * @return
     */
    @Override
    public List<EnumType> listEnumType(EnumType enumType) {
        return this.baseDao.selectList(CLASSNAME + ".findEnumType", enumType);
    }

    /**
     * 激活/禁用
     *
     * @param
     * @return
     * @author aj
     * @date 2015-4-12 下午12:24:53
     * @version V1.0
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        EnumObj dbEnumObj = findById(id);
        if (null != dbEnumObj) {
            dbEnumObj.setStatus(status);
            dbEnumObj.setId(id);
            super.update(dbEnumObj);
            // 清除redis
            this.clearEnumRedis(dbEnumObj.getEnumTypeCode(), dbEnumObj.getUpperEnumKey(), null);
            this.clearEnumRedis(dbEnumObj.getEnumTypeCode(), dbEnumObj.getUpperId(), dbEnumObj.getId());
        }
    }

    /**
     * 清空redis
     *
     * @param
     * @return
     * @author aj
     * @date 2015-4-12 下午04:24:18
     * @version V1.0
     */
    public void clearEnumRedis(String enumTypeCode, String upperEnumKey, String enumKey) {

        commonRedisDao.delete(RedisConstant.enumFix + enumTypeCode + "_" + upperEnumKey);

        if (enumKey != null) {
            commonRedisDao.delete(RedisConstant.enumByCodeFix + enumTypeCode + "_" + upperEnumKey + "_" + enumKey);
        }

    }

    /**
     * 数据字典保存
     *
     * @param @param enumObj
     * @return
     * @author aj
     * @date 2015-3-28 下午01:51:46
     * @version V1.0
     */
    @Override
    public void save(EnumObj enumObj) {
        // TODO 保存
        enumObj.setStatus(CommonConstant.STATUS_USED);
        if (enumObj.getUpperId() != null) {
            EnumObj upperEnumObj = (EnumObj) super.findOne(enumObj);
            enumObj.setUpperEnumKey(upperEnumObj.getEnumKey());
        }

        EnumType enumTypeInfo = new EnumType();
        enumTypeInfo.setEnumTypeCode(enumObj.getEnumTypeCode());
        // EnumType enumType=this.baseDao.selectOne(CLASSNAME +
        // ".findEnumType",enumTypeInfo);
        EnumType enumType = (EnumType) super.findOne(enumTypeInfo);
        if (enumType == null) {
            throw new BusinessException("数据字典类型不存在");
        }
        enumObj.setEnumTypeId(enumType.getId());
        if (this.checkEnum(enumObj)) {
            throw new BusinessException("数据字典已存在");
        }
        super.save(enumObj);

        // ** 清除redis *//*
        this.clearEnumRedis(enumObj.getEnumTypeCode(), enumObj.getUpperEnumKey(), null);
        this.clearEnumRedis(enumObj.getEnumTypeCode(), enumObj.getUpperId(), enumObj.getId());

    }

    /**
     * 数据字典修改
     *
     * @param @param enumObj
     * @return
     * @author aj
     * @date 2015-3-28 下午01:51:46
     * @version V1.0
     */
    @Override
    public void update(EnumObj enumObj) {
        // TODO 修改
        if (enumObj.getUpperId() != null) {
            EnumObj upperEnumObj = (EnumObj) super.findOne(enumObj);
            enumObj.setUpperEnumKey(upperEnumObj.getEnumKey());
        }
        EnumType enumTypeInfo = new EnumType();
        enumTypeInfo.setEnumTypeCode(enumObj.getEnumTypeCode());
        EnumType enumType = (EnumType) super.findOne(enumTypeInfo);
        enumObj.setEnumTypeId(enumType.getId());
        if (this.checkEnum(enumObj)) {
            throw new BusinessException("数据字典已存在");
        }
        enumObj.setEnumValue(enumObj.getEnumValue());
        EnumObj dbEnumObj = findById(enumObj.getId());
        if (null != dbEnumObj) {
            DataUtils.copyPropertiesIgnoreNull(enumObj, dbEnumObj);
            super.update(dbEnumObj);
            // 清除redis
            this.clearEnumRedis(dbEnumObj.getEnumTypeCode(), dbEnumObj.getUpperEnumKey(), null);
            this.clearEnumRedis(dbEnumObj.getEnumTypeCode(), dbEnumObj.getUpperId(), dbEnumObj.getId());
        }
    }

    /**
     * 校验数据字典是否重复
     *
     * @return false 表示用户名不存在；true表示已存在
     */
    public boolean checkEnum(EnumObj enumObj) {
        EnumObjVo newEnumObjVO = new EnumObjVo();
        DataUtils.copyPropertiesIgnoreNull(enumObj, newEnumObjVO);
        newEnumObjVO.setColumn(PropertyUtils.getPropertyNames(EnumObj.class));
        EnumObj dbEnumObj = (EnumObj) this.baseDao.selectOne(CLASSNAME + ".checkEnum", newEnumObjVO);
        if (dbEnumObj == null) {
            return false;
        }
        if (enumObj.getId() == null) {
            return true;
        } else {
            if (enumObj.getId().longValue() != dbEnumObj.getId().longValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取数据字典值
     *
     * @param enumTypeCode
     * @param upperEnumKey
     * @param enumKey
     * @return
     */
    @Override
    public String getEnumValue(String enumTypeCode, String upperEnumKey, String enumKey) {
        if (enumKey == null || "".equals(enumKey)) {
            return "";
        }
        String str = commonRedisDao
                .get(RedisConstant.enumByCodeFix + enumTypeCode + "_" + upperEnumKey + "_" + enumKey);
        if (str != null && !"".equals(str)) {
            return str;
        } else {
            EnumObj enumObjVo = new EnumObj();
            enumObjVo.setEnumTypeCode(enumTypeCode);
            enumObjVo.setUpperEnumKey(upperEnumKey);
            enumObjVo.setEnumKey(enumKey);
            EnumObj enumObj =  (EnumObj)super.findOne(enumObjVo);
            if (enumObj == null) {
                return "";
            }
            commonRedisDao.set(RedisConstant.enumByCodeFix + enumTypeCode + "_" + upperEnumKey + "_" + enumKey,
                    enumObj.getEnumValue());
            return enumObj.getEnumValue();
        }

    }

    /**
     * 根据数据字典类型获取所有key，用逗号分隔
     *
     * @param enumTypeCode
     * @return
     */
    @Override
    public String getAllEnumKeyString(String enumTypeCode) {
        String str = commonRedisDao.get(RedisConstant.enumAllKeyString + enumTypeCode);
        if (str != null && !"".equals(str)) {
            return str;
        } else {
            EnumObj enumObj = new EnumObj();
            enumObj.setEnumTypeCode(enumTypeCode);

            List<EnumObj> list = this.baseDao.selectList(CLASSNAME + ".findEnumObjPage", enumObj);
            if (list.size() == 0) {
                return "";
            }
            str = "";
            for (EnumObj tempEnumObj : list) {
                if (!"".equals(str)) {
                    str = str + ",";
                }
                str = str + tempEnumObj.getEnumKey();
            }
            commonRedisDao.set(RedisConstant.enumAllKeyString + enumTypeCode, str);
            return str;
        }
    }

    /**
     * 根据多个行业类型代码转换为名称
     *
     * @param businessTypeCodes
     */
    @Override
    public String getBusinessTypeNamesByCodes(String businessTypeCodes) {
        if (businessTypeCodes == null || "".equals(businessTypeCodes)) {
            return "";
        }
        String businessTypeName = "";
        String[] businessTypeCodesArray = businessTypeCodes.split(",");
        for (String businessTypeCode : businessTypeCodesArray) {
            if (!"".equals(businessTypeName)) {
                businessTypeName = businessTypeName + ",";
            }
            businessTypeName = businessTypeName
                    + this.getEnumValue(CommonConstant.EnumType.ENUMTYPE_COMPANYBUSINESSTYPE, null, businessTypeCode);
        }
        return businessTypeName;
    }

}