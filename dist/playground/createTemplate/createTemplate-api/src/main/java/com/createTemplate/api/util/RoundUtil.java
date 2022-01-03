package com.createTemplate.api.util;

import java.math.BigDecimal;

public class RoundUtil {
	/**
	 * 获得指定位数的浮点数
	 * @author xf
	 * @Version: V1.00 
	 * @Create 2014-11-28 下午2:56:29
	 * @param targetValue   目标浮点数
	 * @param scale  精确的位数
	 * @param roundingMode 省略模式  （四舍五入。。。）
	 * @return 指定位数的浮点数
	 */
	public static float round(float targetValue , int scale, int roundingMode ){
		BigDecimal bd = new BigDecimal(targetValue);     
        bd = bd.setScale(scale, roundingMode);     
        float d = bd.floatValue();     
        bd = null;     
        return d;     
	}
	
	public static float round(float targetValue , int scale){
        return round(targetValue, scale, BigDecimal.ROUND_HALF_UP);
	}
	
	
	/**
	 * 获得指定位数的浮点数
	 * @author xf
	 * @Version: V1.00 
	 * @Create 2014-11-28 下午2:56:29
	 * @param targetValue   目标浮点数
	 * @param scale  精确的位数
	 * @param roundingMode 省略模式  （四舍五入。。。）
	 * @return 指定位数的浮点数
	 */
	public static double round(double targetValue , int scale, int roundingMode ){
		BigDecimal bd = new BigDecimal(targetValue);     
        bd = bd.setScale(scale, roundingMode);     
        double d = bd.doubleValue();     
        bd = null;     
        return d;     
	}
	
	public static double round(double targetValue , int scale){
        return round(targetValue, scale, BigDecimal.ROUND_HALF_UP);
	}
}
