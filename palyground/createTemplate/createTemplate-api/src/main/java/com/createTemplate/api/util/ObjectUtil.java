package com.createTemplate.api.util;

public class ObjectUtil {
	/**
	 * 强制jvm释放没有引用的对象
	 */
	public static  void releaseUnUseObject() {
		System.gc();
		System.runFinalization();
	}
	
	public static String getNickName(String nickName,String personCode) {
		if(StringUtil.checkNull(nickName) && StringUtil.checkNull(personCode) ){
				if(StringUtil.isMobileNO(personCode)){
					return personCode.replace(personCode.substring(3, 7), "****");
				}else{
					return personCode;
				}
		}
		return nickName;
	}
}
