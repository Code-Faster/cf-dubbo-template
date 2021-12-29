/*
* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
*
* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
*/
package com.createTemplate.provider.base.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;

import com.createTemplate.api.base.dao.BaseDao;
import com.createTemplate.api.base.dao.MybatisDao;
import com.createTemplate.model.mybatis.page.PageParameter;
import com.createTemplate.model.resultvo.Grid;

/**
 * 
 *
 * @author: 
 * @date: 2018年6月12日 下午2:52:53
 * @version V1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MybatisDaoImpl implements ApplicationContextAware, MybatisDao {
    public static final char UNDERLINE_CHAR = '_';

    private static String table_ = "_table";

    private static String seq_ = "_seq";

    private static String id_ = "_id";

    /** 数据库类型(默认为oracle) */
    private static String dialect = "";

    /** 是否显示sql */
    private static boolean showsql = false;

    /** true字符串 */
    private static final String TRUE = "true";

    /** 逗号字符串 */
    private static final String COMMA = ",";

    /** oracle数据类型 */
    private static final String DIALECT_ORACLE = "oracle";

    /** mysql数据类型 */
    private static final String DIALECT_MYSQL = "mysql";

    protected ApplicationContext applicationContext;

    protected BaseDao baseDao;

    public BaseDao getBaseDao() {
        return baseDao;
    }

    @Resource(name = "baseDao")
    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
        Configuration configuration = this.baseDao.getConfiguration();
        dialect = configuration.getVariables().getProperty("dialect");
        String showsql1 = configuration.getVariables().getProperty("showsql");
        if (showsql1 != null && showsql1.toString().trim().equals(TRUE)) {
            showsql = true;
        }
    }

    @Override
    public Long save(Object obj) {
        Map m = parseMap(obj, 1);
        Long id = null;
        if (m.get(seq_) != null) {
            id = this.baseDao.getJdbcTemplate().queryForObject("select " + m.get(seq_) + ".nextval from dual ",
                    Long.class);
        }
        if (DIALECT_ORACLE.equals(dialect) && m.get(seq_) == null && m.get(id_) != null) {
            throw new RuntimeException("orcale 数据库主键【" + m.get(id_) + "】没有设置序列!");
        }
        try {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            Set<String> set = m.keySet();
            Object value = null;
            List<Object> valuesList = new ArrayList<Object>();
            for (String key : set) {
                value = m.get(key);
                if (key.equals(m.get(id_).toString())) {
                    if (DIALECT_MYSQL.equals(dialect)) {
                        continue;
                    }
                    value = id;
                } else if (key.equals(table_) || key.equals(seq_) || key.equals(id_)) {
                    continue;
                }
                columns.append(camel2Underline(key)).append(",");
//                values.append(value).append(",");
                values.append("?,");
                valuesList.add(value);
            }

            // 去掉最后的,

            String sql = "insert into " + m.get(table_) + "(" + removeLastComma(columns.toString()) + ") " + "values ("
                    + removeLastComma(values.toString()) + ")";

            if (showsql) {
                System.out.println("sql:" + sql);
            }

//            this.baseDao.getJdbcTemplate().execute(sql);

            Object [] valueArr = valuesList.toArray();
            this.baseDao.getJdbcTemplate().update(sql, valueArr);
            // 查询自增长的id
            if (DIALECT_MYSQL.equals(dialect) && m.get(id_) != null) {
                id = this.baseDao.getJdbcTemplate().queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            }
            Field oldField = obj.getClass().getDeclaredField("id");
            oldField.setAccessible(true);
            oldField.set(obj, id);
        } catch (Exception e) {
            e.printStackTrace();
            id = null;
            throw new RuntimeException(e.getMessage());
        }
        return id;
    }

    /**
     * 去掉最后的,号
     *
     * @author sjl 创建时间:2015-4-17 下午5:18:18
     * @param str
     * @return
     */
    private String removeLastComma(String str) {
        String result = "";
        if (str.lastIndexOf(COMMA) == str.length() - 1) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

    @Override
    public void update(Object obj) {
        Map m = parseMap(obj, 2);
        if (m.get(id_) == null || m.get(m.get(id_)) == null) {
            throw new RuntimeException("update object doesn't has id !");
        }
        StringBuilder columns = new StringBuilder();
        StringBuilder where = new StringBuilder();
        Set<String> set = m.keySet();
        List<Object> valuesList = new ArrayList<Object>();
        for (String key : set) {
            if (key.equals(m.get(id_).toString())) {
                where.append(" where ").append(m.get(id_)).append(" = ").append(m.get(key));
                continue;
            }
            if (seq_.equals(key) || table_.equals(key) || id_.equals(key)) {
                continue;
            }
            columns.append(camel2Underline(key) + "=?"  ).append(",");
            valuesList.add(m.get(key));
        }
        String sql = "update " + m.get(table_) + " " + "set " + removeLastComma(columns.toString()) + where.toString();

        if (showsql) {
            System.out.println("sql:" + sql);
        }
        this.baseDao.getJdbcTemplate().update(sql, valuesList.toArray());
//        this.baseDao.getJdbcTemplate().execute(sql);
    }

    /**
     *
     * @Description: 更新，但是不考试分表的字段
     * @author aijian
     * @date 2016-6-28 上午12:38:59
     * @version V1.0
     */
    @Override
    public void update(Object obj, Map<String, Object> shardMap) {
        Map m = parseMap(obj, 2);
        if (m.get(id_) == null || m.get(m.get(id_)) == null) {
            throw new RuntimeException("update object doesn't has id !");
        }
        StringBuilder columns = new StringBuilder();
        StringBuilder where = new StringBuilder();
        Set<String> set = m.keySet();
        List<Object> valuesList = new ArrayList<Object>();
        for (String key : set) {
            if (shardMap.containsKey(key)) {
                continue;
            }
            if (key.equals(m.get(id_).toString())) {
                where.append(" where ").append(m.get(id_)).append(" = ").append(m.get(key));
                continue;
            }
            if (seq_.equals(key) || table_.equals(key) || id_.equals(key)) {
                continue;
            }
//            columns.append(key + "=" + m.get(key)).append(",");
            columns.append(key + "=?"  ).append(",");
            valuesList.add(m.get(key));
        }
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(m.get(table_)).append(" set ").append(removeLastComma(columns.toString())).append(
                where);
        // String sql = "update " + m.get(table_) + " " + "set " +
        // removeLastComma(columns.toString()) + where.toString();

//        Object[] objArray = new Object[shardMap.size()];
//        int index = 0;

        for (Entry<String, Object> entry : shardMap.entrySet()) {
            // sql = sql + " and " + entry.getKey() + " = ? ";
            sql.append(" and ").append(entry.getKey()).append(" = ? ");
//            objArray[index] = entry.getValue();
//            index++;
            valuesList.add(entry.getValue());
        }
        /*
         * if(condition != null && !"".equals(condition)){ sql = sql + " " +
         * condition; }
         */

        if (showsql) {
            System.out.println("sql:" + sql.toString());
        }
        this.baseDao.getJdbcTemplate().update(sql.toString(), valuesList.toArray());
    }

    @Override
    public boolean delete(Object obj) {
        try {
            Map m = parseMap(obj, 3);
            if (m.get(id_) == null || m.get(m.get(id_)) == null) {
                throw new RuntimeException("delete object doesn't has id !");
            }
            this.baseDao.getJdbcTemplate().update("delete from " + m.get(table_) + " where " + m.get(id_) + "=?" ,m.get(m.get(id_)) );
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteById(String table, String idName, Long idValue) {
        try {
            this.baseDao.getJdbcTemplate().execute("delete from " + table + " where " + idName + "=" + idValue);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object findOne(Object obj) {
        List<Object> list = findList(obj);
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List findList(Object obj) {
        Map m = parseMap(obj, 4);
        List resultList = null;
        try {
            StringBuilder where = new StringBuilder("");
            Set<String> set = m.keySet();
            List<Object> valuesList = new ArrayList<Object>();
            if (set != null && set.size() > 0) {
                int i = 0;
                for (String key : set) {
                    if (key.equals(id_) || key.equals(table_) || key.equals(seq_)) {
                        continue;
                    } else {
                        valuesList.add(m.get(key));
                        if (i == 0) {
                            where.append(" ").append(camel2Underline(key)).append("=").append("? ");
                        } else {
                            where.append(" and ").append(camel2Underline(key)).append("=? ");
                        }
                        i++;
                    }
                }
            }
            String sql = "select * from " + m.get(table_) + (where.toString().equals("") ? "" : " where " + where.toString());

            if (showsql) {
                System.out.println("sql:" + sql);
            }
            List<Map<String, Object>> list = this.baseDao.getJdbcTemplate().queryForList(sql, valuesList.toArray());
            if (list != null && !list.isEmpty()) {
                resultList = new ArrayList();
                for (Map m1 : list) {
                    resultList.add(map2Obj(obj, m1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return resultList;
    }

    /**
     * 将map转换为object 对象
     *
     * @param obj
     * @param m
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Object map2Obj(Object obj, Map m) throws InstantiationException, IllegalAccessException {
        // 创建 JavaBean 对象
        Object robj = obj.getClass().newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        String fieldName = null;
        Object value = null;
        for (Field field : fields) {
            fieldName = field.getName();
            value = m.get(camel2Underline(field.getName()));
            if (value != null) {
                setFieldValue(robj, fieldName, value, field);
            }
        }
        return robj;
    }

    /**
     * 将对象转换为map(_table,_seq,_id 和其他属性值 )
     *
     * @param obj
     * @param oper
     *            1,添加;2,修改;3,删除;4,查询
     * @return
     */
    private Map parseMap(Object obj, int oper) {
        Map m = new HashMap();
        if (!obj.getClass().isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("save or update object not model class! please add @Entity!");
        }
        Entity entity = obj.getClass().getAnnotation(Entity.class);
        m.put(table_, entity.name());

        Class clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        String fieldName = null;
        Object value = null;
        for (Field field : fields) {
            fieldName = field.getName();
            if(field.getModifiers()!=Modifier.PRIVATE){
                continue;
            }

            Transient transient1 = field.getAnnotation(Transient.class);
            if (transient1 != null) {
                continue;
            }
            // id
            if (field.getAnnotation(Id.class) != null) {
                m.put(id_, fieldName);
                SequenceGenerator seq = field.getAnnotation(SequenceGenerator.class);
                if (seq != null) {
                    m.put(seq_, seq.name());

                } else {
                    m.put(seq_, null);
                }
                // 添加
                if (oper == 1) {
                    m.put(fieldName, null);
                    // 修改或者查询
                } else if (oper == 2 || oper == 4) {
                    value = getFieldValue(obj, fieldName);
                    if (value != null) {
                        m.put(fieldName, value);
                    }
                    // 删除
                } else if (oper == 3) {
                    value = getFieldValue(obj, fieldName);
                    if (value != null) {
                        m.put(fieldName, value);
                    }
                    break;
                }
            } else {
                value = getFieldValue(obj, fieldName);
                if (value != null) {
                    m.put(fieldName, value);
                }
            }
        }

        if (m.get(id_) == null) {
            throw new RuntimeException("the oper obj doesn't has @ID !");
        }
        return m;
    }

    /**
     * 得到值
     *
     * @param targetObj
     *            源对象
     * @param fieldName
     *            对象的属性
     * @return
     */
    private Object getFieldValue(Object targetObj, String fieldName) {
        Object value = null;
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method method = targetObj.getClass().getMethod(methodName, new Class[] {});
            value = method.invoke(targetObj, new Object[] {});
//            if (value instanceof String) {
//                if(value !=null && value.toString().indexOf("'")!=-1){
//                    value=value.toString().replaceAll("'", "\\\\'");
//                }
//                value = "'" + value + "'";
//            } else if (value instanceof Date) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                if (DIALECT_MYSQL.equals(dialect)) {
//                    value = "str_to_date('" + sdf.format((Date) value) + "','%Y-%m-%d %H:%i:%s')";
//                } else {
//                    value = "to_date('" + sdf.format((Date) value) + "','yyyy-MM-dd hh24:mi:ss')";
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 向对象中写入属性值
     *
     * @param targetObj
     *            源对象
     * @param fieldName
     *            对象的属性
     * @param value
     *            值
     * @param field
     */
    private void setFieldValue(Object targetObj, String fieldName, Object value, Field field) {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method method = targetObj.getClass().getMethod(methodName, new Class[] { field.getType() });
            if (value instanceof BigDecimal) {
                value = new BigDecimal("" + value);
            }
            value = method.invoke(targetObj, new Object[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Grid findPage(String sqlId, Map pmap, PageParameter page) {
        if (pmap == null) {
            pmap = new HashMap();
        }
        pmap.put("page", page);
        List<Map> list = this.baseDao.selectList(sqlId, pmap);
        page = (PageParameter) pmap.get("page");
        Grid grid = new Grid();
        grid.setCount(Long.valueOf(page.getTotalCount()));
        grid.setList(list);
        return grid;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    /**
     * 驼峰转下划线
     *
     * @param camelStr
     * @return
     */
    public static String camel2Underline(String camelStr) {

        if (StringUtils.isEmpty(camelStr)) {

            return StringUtils.EMPTY;
        }

        int len = camelStr.length();
        StringBuilder strb = new StringBuilder(len + len >> 1);
        for (int i = 0; i < len; i++) {

            char c = camelStr.charAt(i);
            if (Character.isUpperCase(c)) {

                strb.append(UNDERLINE_CHAR);
                strb.append(Character.toLowerCase(c));
            } else {

                strb.append(c);
            }
        }
        return strb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param underlineStr
     * @return
     */
    public static String underline2Camel(String underlineStr) {

        if (StringUtils.isEmpty(underlineStr)) {

            return StringUtils.EMPTY;
        }

        int len = underlineStr.length();
        StringBuilder strb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {

            char c = underlineStr.charAt(i);
            if (c == UNDERLINE_CHAR && (++i) < len) {

                c = underlineStr.charAt(i);
                strb.append(Character.toUpperCase(c));
            } else {

                strb.append(c);
            }
        }
        return strb.toString();
    }

}
