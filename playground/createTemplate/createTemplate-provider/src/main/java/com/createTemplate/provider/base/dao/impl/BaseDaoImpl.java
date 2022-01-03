/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.provider.base.dao.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.createTemplate.api.base.dao.BaseDao;



/**
 * 
 * mybatis基础dao
 * @author:  
 * @date: 2018年6月1日 下午4:02:40 
 * @version V1.0
 */
@Component("baseDao")
public class BaseDaoImpl implements BaseDao{


	public static final Log LOG = LogFactory.getLog(BaseDaoImpl.class);

	@Resource
	JdbcTemplate jdbcTemplate;

	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	/**
	 * @return the jdbcTemplate
	 */
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @return the sqlSessionTemplate
	 */
	@Override
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	/**
	 * @param sqlSessionTemplate the sqlSessionTemplate to set
	 */
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public void clearCache() {
		sqlSessionTemplate.clearCache();
	}

	@Override
	public void close() {
		sqlSessionTemplate.close();
	}

	@Override
	public void commit() {
		sqlSessionTemplate.commit();
	}

	@Override
	public void commit(boolean arg0) {
		sqlSessionTemplate.commit(arg0);
	}

	@Override
	public int delete(String arg0) {
		return sqlSessionTemplate.delete(arg0);
	}

	@Override
	public int delete(String arg0, Object arg1) {
		return sqlSessionTemplate.delete(arg0, arg1);
	}

	@Override
	public List<BatchResult> flushStatements() {
		return sqlSessionTemplate.flushStatements();
	}

	@Override
	public Configuration getConfiguration() {
		return sqlSessionTemplate.getConfiguration();
	}

	@Override
	public Connection getConnection() {
		return sqlSessionTemplate.getConnection();
	}

	@Override
	public <T> T getMapper(Class<T> arg0) {
		return sqlSessionTemplate.getMapper(arg0);
	}

	@Override
	public int insert(String arg0) {
		return sqlSessionTemplate.insert(arg0);
	}

	@Override
	public int insert(String arg0, Object arg1) {
		return sqlSessionTemplate.insert(arg0, arg1);
	}

	@Override
	public void rollback() {
		sqlSessionTemplate.rollback();
	}

	@Override
	public void rollback(boolean arg0) {
		sqlSessionTemplate.rollback(arg0);
	}

	@Override
	public void select(String arg0, ResultHandler arg1) {
		sqlSessionTemplate.select(arg0, arg1);
	}

	@Override
	public void select(String arg0, Object arg1, ResultHandler arg2) {
		sqlSessionTemplate.select(arg0, arg1, arg2);
	}

	@Override
	public void select(String arg0, Object arg1, RowBounds arg2,
					   ResultHandler arg3) {
		sqlSessionTemplate.select(arg0, arg1, arg2, arg3);
	}

	@Override
	public <E> List<E> selectList(String arg0) {
		return sqlSessionTemplate.selectList(arg0);
	}

	@Override
	public <E> List<E> selectList(String arg0, Object arg1) {
		return sqlSessionTemplate.selectList(arg0, arg1);
	}

	@Override
	public <E> List<E> selectList(String arg0, Object arg1, RowBounds arg2) {
		return sqlSessionTemplate.selectList(arg0, arg1, arg2);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String arg0, String arg1) {
		return sqlSessionTemplate.selectMap(arg0, arg1);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String arg0, Object arg1, String arg2) {
		return sqlSessionTemplate.selectMap(arg0, arg1, arg2);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String arg0, Object arg1, String arg2,
									  RowBounds arg3) {
		return sqlSessionTemplate.selectMap(arg0, arg1, arg2, arg3);
	}

	@Override
	public <T> T selectOne(String arg0) {
		return sqlSessionTemplate.selectOne(arg0);
	}

	@Override
	public <T> T selectOne(String arg0, Object arg1) {
		return sqlSessionTemplate.selectOne(arg0, arg1);
	}

	@Override
	public int update(String arg0) {
		return sqlSessionTemplate.update(arg0);
	}

	@Override
	public int update(String arg0, Object arg1) {
		return sqlSessionTemplate.update(arg0, arg1);
	}

	@Override
	public void batchInsertList(String stateName, List list) {
		int result = 1;
		SqlSession batchSqlSession = null;
		try {
			// 获取批量方式的sqlsession
			batchSqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			// 每批commit的个数
			int batchCount = 1000;
			// 每批最后一个的下标
			int batchLastIndex = batchCount;
			for (int index = 0; index < list.size();) {
				if (batchLastIndex >= list.size()) {
					batchLastIndex = list.size();
					result = result * batchSqlSession.insert(stateName,list.subList(index, batchLastIndex));
					batchSqlSession.commit();
					System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
					// 数据插入完毕，退出循环
					break;
				} else {
					result = result * batchSqlSession.insert(stateName,list.subList(index, batchLastIndex));
					batchSqlSession.commit();
					System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
					// 设置下一批下标
					index = batchLastIndex;
					batchLastIndex = index + (batchCount - 1);
				}
			}
			batchSqlSession.commit();
		}
		finally {
			batchSqlSession.close();
		}
	}

	@Override
	public <T> Cursor<T> selectCursor(String statement) {
		return sqlSessionTemplate.selectCursor(statement);
	}
	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter) {
		return sqlSessionTemplate.selectCursor(statement,parameter);
	}
	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
		return sqlSessionTemplate.selectCursor(statement,parameter,rowBounds);
	}

}
