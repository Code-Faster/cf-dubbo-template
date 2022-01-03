package com.createTemplate.api.util;

/**
 * 功能：数据Util
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


public class DataUtils {

	private static Map supportTypeMap;
    static 
    {
        supportTypeMap = new HashMap();
        supportTypeMap.put(Integer.class, "");
        supportTypeMap.put(java.lang.Long.class, "");
        supportTypeMap.put(java.lang.Double.class, "");
        supportTypeMap.put(java.math.BigDecimal.class, "");
        supportTypeMap.put(java.lang.String.class, "");
        supportTypeMap.put(java.util.Date.class, "");
        supportTypeMap.put(java.lang.Boolean.class, "");
        supportTypeMap.put(java.util.Calendar.class, "");
        supportTypeMap.put(java.lang.Float.class, "");
    }


    /**
	 * 属性拷贝
	 * @author aijian
	 * @param toObj 目标对象
	 * @param fromObj 源对象
	 * @param copyNull 是否拷贝null，true拷贝，false不拷贝
	 * @Date:2014-3-4下午03:07:54
	 */
	public static Object copyObject(Object toObj,Object fromObj,boolean copyNull){
        Map map;
        Iterator iter;
        Method method;
        if(toObj == null || toObj == null){
        	return null;
        }
        List setMethodList = getSetter(fromObj.getClass());
        List getMethodList = getGetter(toObj.getClass());
        map = new HashMap();
        for(iter = getMethodList.iterator(); iter.hasNext(); map.put(method.getName(), method)) {
			method = (Method)iter.next();
		}

        iter = setMethodList.iterator();
//          goto _L1
//_L3:
        String fieldName;
        while(iter.hasNext()){
	        method = (Method)iter.next();
	        fieldName = method.getName().substring(3);
	        Method getMethod;
	        getMethod = (Method)map.get((new StringBuilder("get")).append(fieldName).toString());
	        if(getMethod == null) {
				getMethod = (Method)map.get((new StringBuilder("is")).append(fieldName).toString());
			}
	        if(getMethod == null) {
				continue; /* Loop/switch isn't completed */
			}
	        if(supportTypeMap.containsKey(getMethod.getReturnType())){
	        	try{
	        		Object value = getMethod.invoke(fromObj, new Object[0]);
	        		if(value == null && !copyNull){
	        			continue;
	        		}
	        		if(value == null){
	        			method.invoke(toObj, new Object[] {value});
	                }else if(value != null){
	                	method.invoke(toObj, new Object[] {value});
	            	}
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	        }
        }
        return toObj;
	}
	
	public static void main(String[] args) {
		
//		UserInfo user = new UserInfo();
//		user.setUserName("aijian");
//		user.setUserPassword("aaa");
//		//user.setCreateTime(Calendar.getInstance());
//		UserInfo newUser = new UserInfo();
//		System.out.println(newUser.getUserName());
//		System.out.println(newUser.getUserPassword());
//		DataUtils.copyObject(newUser,user,false);
//		System.out.println(newUser.getUserName());
//		System.out.println(newUser.getUserPassword());
//		
//		UserInfo user2 = new UserInfo();
//		DataUtils.copyObject(newUser,user2,false);
//		System.out.println(newUser.getUserName());
//		System.out.println(newUser.getUserPassword());		
//	
//		DataUtils.copyObject(newUser,user2,true);
//		System.out.println(newUser.getUserName());
//		System.out.println(newUser.getUserPassword());	
//		int[] arr = DataUtils.StringArr2IntArr("2,3,4", ",");
//		for (int i : arr) {
//			System.out.println(i);
//		}
		
	}
	
	/**
	 * 功能：根据Class获得所有的set方法
	 * @param cl 类
	 * @return
	 */
    public static List getSetter(Class cl){
    	List list = new ArrayList();
    	Method methods[] = cl.getDeclaredMethods();
    	for(int i = 0; i < methods.length; i++){
    		Method method = methods[i];
    		String methodName = method.getName();
    		if(methodName.startsWith("set")){
    			list.add(method);
    		}
    	}
        do
        {
            cl = cl.getSuperclass();
            if(cl != java.lang.Object.class){
                list.addAll(getSetter(cl));
            }else{
            	return list;
            }
        } while(true);
    }
    
    /**
     * 功能：根据Class获得所有的get方法
     * @param cl 类
     * @return
     */
    public static List getGetter(Class cl){
    	
    	List list = new ArrayList();
    	Method methods[] = cl.getDeclaredMethods();
    	for(int i = 0; i < methods.length; i++){
    		Method method = methods[i];
    		String methodName = method.getName();
    		if(methodName.startsWith("get") || methodName.startsWith("is")){
    			list.add(method);
    		}
    	}
        do
        {
            cl = cl.getSuperclass();
            if(cl != java.lang.Object.class){
            	list.addAll(getGetter(cl));
            }else{
            	return list;
            }
        } while(true);
    }
    public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public static void copyPropertiesIgnoreNull(Object src, Object target) {
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}
}