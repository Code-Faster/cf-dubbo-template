package com.createTemplate.api.base.dao;

import java.util.List;
import java.util.Map;
import com.createTemplate.model.mybatis.page.PageParameter;
import com.createTemplate.model.resultvo.Grid;

/**
 *
 * @author:  
 * @date: 2018年6月13日 上午9:35:02 
 * @version V1.0
 */
public interface MybatisDao {
	/**
	 * 保存javaBean
	 * @param obj
	 * @return
	 */
	public Long save(Object obj);
	/**
	 * 更新javaBean
	 * @param obj
	 */
	public void update(Object obj);
	
	/**
	 * 更新javaBean，但是不考试分表的字段
	 * @param obj  待更新对象
	 * @param shardMap 分表字段
	 */
	public void update(Object obj,Map<String, Object> shardMap)  ;
	/**
	 * 删除javaBean
	 * @param obj 要删除的对象
	 * @return
	 */
	public boolean delete(Object obj);
	/**
	 * 根据ID删除javaBean
	 * @param table 表名
	 * @param idName id字段名
	 * @param idValue id值
	 * @return
	 */
	public boolean deleteById(String table,String idName,Long idValue);
	/**
	 * 根据javaBean 查询条件查询一条记录
	 * @param obj  查询条件对象
	 * @return
	 */
	public Object findOne(Object obj);
	/**
	 * 根据javaBean 查询条件查询多条记录
	 * @param obj 查询条件对象
	 * @return
	 */
	public List findList(Object obj);
	
	/**
	 * 分页查询数据
	 * @param sqlId mapper.xml中select语句ID
     * @param pmap 查询参数
     * @param page 页码信息
	 * @return
	 */
	public Grid findPage(String sqlId,Map pmap,PageParameter page);
}