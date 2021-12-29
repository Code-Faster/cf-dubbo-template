/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.model.base.vo;

import com.createTemplate.model.annotation.AntField;
import com.createTemplate.model.base.pojo.EnumType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *数据字典类型vo
 * @author:  
 * @date: 2018年5月26日 下午5:57:53 
 * @version V1.0
 */
@ApiModel(value ="数据字典类型vo")
public class EnumTypeVo extends EnumType implements java.io.Serializable{
    @ApiModelProperty(value ="列",hidden=true)
	@AntField(name="列",desc="列",required=false)
	private String column;
	
	@AntField(name="当前页",desc="当前页",required=false)
	private Integer page;
	
	@AntField(name="每页的条数",desc="每页的条数",required=false)
	private Integer rows;
	
	/**排序字段*/
	private String order;

    
    /**
     * @return the column
     */
    public String getColumn() {
        return column;
    }

    
    /**
     * @param column the column to set
     */
    public void setColumn(String column) {
        this.column = column;
    }

    
    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    
    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    
    /**
     * @return the rows
     */
    public Integer getRows() {
        return rows;
    }

    
    /**
     * @param rows the rows to set
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    
    /**
     * @return the order
     */
    public String getOrder() {
        return order;
    }

    
    /**
     * @param order the order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

}
