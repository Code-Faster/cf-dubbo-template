/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.base.vo;

import java.io.Serializable;
import java.util.List;

import com.createTemplate.model.annotation.AntBean;
import com.createTemplate.model.annotation.AntField;
/**
 *表格vo
 * @author:  
 * @date: 2018年5月26日 下午5:58:34 
 * @version V1.0
 */
@AntBean(name = "表格vo", desc = "表格vo，只提供扫描bean", author = "xf")
public class GridVo implements Serializable{
    private static final long serialVersionUID = 1L;

    @AntField(name = "总数", desc = "总数", required = false)
	private Integer count;

	@AntField(name = "返回列表", desc = "返回列表", required = false)
	private List list;

    
    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    
    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    
    /**
     * @return the list
     */
    public List getList() {
        return list;
    }

    
    /**
     * @param list the list to set
     */
    public void setList(List list) {
        this.list = list;
    }

}
