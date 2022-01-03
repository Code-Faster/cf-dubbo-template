/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.personal.web.base.controller;

import java.util.List;

import com.createTemplate.model.exception.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.api.base.dubbo.service.EnumService;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.model.base.pojo.EnumObj;
import com.createTemplate.model.resultvo.ResultInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 字典接口
 *
 * @version V1.0
 * @author:
 * @date: 2018年5月15日 下午5:08:10
 */
@RestController
@RequestMapping(value = "/service/enum")
@Api(value = "数据字典controller", tags = {"数据字典"})
@Slf4j
public class EnumController extends CommonBaseController {

    @Reference(version = "1.0.0", check = false)
    EnumService enumService;
    @Reference(version = "1.0.0", check = false)
    CommonRedisDao commonRedisDao;

    /**
     * 数据字典查询
     *
     * @param @param enumObj
     * @version V1.0
     */

    @ApiImplicitParams({
            @ApiImplicitParam(name = "enum_type_code", value = "字典类型代码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "upper_id", value = "上级字典id", required = true, dataType = "Long", paramType = "query"),
    })
    @ApiOperation(value = "数据字典查询", notes = "数据字典查询")
    @RequestMapping(value = "/findEnum", method = RequestMethod.POST)
    public ResultInfo<List<EnumObj>> findEnum(@RequestBody(required = false) EnumObj enumObj) {
        List<EnumObj> enumObjList = enumService.listEnum(enumObj.getEnumTypeCode(), enumObj.getUpperId());
        return new ResultInfo<List<EnumObj>>(ResultEnum.SUCCESS.getCode(), "查询成功", enumObjList);
    }

    /**
     * 数据字典查询
     *
     * @param @param enumObj
     * @version V1.0
     */
    @ApiOperation(value = "clearEnum", notes = "数据字典查询")
    @RequestMapping(value = "/clearEnum", method = RequestMethod.POST)
    public ResultInfo<List<EnumObj>> clearEnum(@RequestBody(required = false) EnumObj enumObj) {
        commonRedisDao.delete("zt_enumByUpperId:crowdConsumptionPsychology_null");
        List<EnumObj> enumObjList = enumService.listEnum(enumObj.getEnumTypeCode(), enumObj.getUpperId());
        return new ResultInfo<List<EnumObj>>(ResultEnum.SUCCESS.getCode(), "查询成功", enumObjList);
    }

}
