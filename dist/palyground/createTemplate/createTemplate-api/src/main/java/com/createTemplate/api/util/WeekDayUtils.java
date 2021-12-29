package com.createTemplate.api.util;

import java.util.ArrayList;
import java.util.List;

/**   
 *    
 * 项目名称：volunteer   
 * 类名称：WeekDayUtils   
 * 类描述：   
 * 创建时间：2016-8-4 下午2:28:37   
 * 修改备注：   
 * @version  1.0  
 *    
 */
public class WeekDayUtils {
	
	private static final String fullWeekDays = "1,2,3,4,5,6,7"; 
			
	
	public static String [] getUnSelectWeekDays(String selecctWeekDays){
		String [] fullArray = fullWeekDays.split(",");
		List<String> list= new ArrayList<String>();
		for (String weekDay : fullArray) {
			if(!selecctWeekDays.contains(weekDay)){
				list.add(weekDay);
			}
		}
		return list.toArray(new String[]{});
	}
	
}

