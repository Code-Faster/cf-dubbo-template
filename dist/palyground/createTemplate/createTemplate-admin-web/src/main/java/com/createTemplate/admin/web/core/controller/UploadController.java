/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.admin.web.core.controller;

import java.util.Map;

import com.createTemplate.model.exception.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.createTemplate.admin.web.config.OOSAutoConfiguration;
import com.createTemplate.api.common.controller.CommonBaseController;
import com.createTemplate.model.exception.BusinessException;
import com.createTemplate.model.resultvo.ResultInfo;

import io.swagger.annotations.ApiOperation;

@RequestMapping(value = "/service/upload")
@Scope(value = "prototype")
@RestController
public class UploadController extends CommonBaseController {

    @Autowired
    private OOSAutoConfiguration ossUtil;

    @ApiOperation(value = "上传到oos ", notes = "上传到oos")
    @RequestMapping(value = "/uploadFileToOSS")
    public ResultInfo<Map> uploadFileToOSS(@RequestParam("file") MultipartFile upfile) throws Exception {
        if (upfile == null) {
            throw new BusinessException("文件不能为空");
        }
        Map fileMap = ossUtil.upload(upfile, ossUtil.getCommonBucket());
        return new ResultInfo<>(ResultEnum.SUCCESS.getCode(), "上传成功", fileMap);
    }
}
