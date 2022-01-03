/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.personal.web.common.controller;

import java.util.Map;

import com.createTemplate.model.exception.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.createTemplate.api.base.dubbo.service.CommonRedisDao;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.model.annotation.AntController;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.ResultInfo;
import com.createTemplate.personal.web.config.OOSAutoConfiguration;

import io.swagger.annotations.ApiOperation;

/**
 * 公共
 * @author:  
 * @date: 2018年7月11日 上午10:43:39 
 * @version V1.0
 */
@RestController
@RequestMapping(value = "/service/common")
@AntController(author = "", desc = "公共接口", name = "公共接口")
public class CommonController extends CommonBaseController {
    
    @Reference(version="1.0.0",check = false)
	private CommonRedisDao commonRedisDao;
	
    
    @Autowired
    private OOSAutoConfiguration ossUtil;

    @ApiOperation(value = "上传到oos ", notes = "上传到oos")
    @PostMapping(value = "/uploadFileToOSS")
    public ResultInfo<Map> uploadFileToOSS(@RequestParam("file") MultipartFile upfile) throws Exception {
        if (upfile == null) {
            throw new BusinessException("文件不能为空");
        }
        Map fileMap = ossUtil.upload(upfile, ossUtil.getCommonBucket());
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "上传成功", fileMap);
    }
    
	/**
	 * 清空redis
	 * 
	 * @author aj
	 * @return
	 * @version V1.0
	 */
	@PostMapping(value = "/clearRedis")
	public ResultInfo clearRedis() {
		commonRedisDao.flushDB();
		return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "清空成功", null);
	}

}
