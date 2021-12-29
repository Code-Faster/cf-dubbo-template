package com.createTemplate.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.createTemplate.model.exception.BusinessException;

public class DateUtils {
	private static SimpleDateFormat dateTimeformat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateformat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat monthFormat = new SimpleDateFormat(
            "yyyy.MM");

	/** 
	    * 获取未来 第 past 天的日期 
	    * @param past 
	    * @return 
	    */  
	   public static String getFetureDate(int past) {  
	       Calendar calendar = Calendar.getInstance();  
	       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);  
	       Date today = calendar.getTime();  
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	       String result = format.format(today);  
	       return result;  
	   }  
	
	 /** 
	    * 获取过去第几天的日期 
	    * 
	    * @param past 
	    * @return 
	    */  
	   public static String getPastDate(int past) {  
	       Calendar calendar = Calendar.getInstance();  
	       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);  
	       Date today = calendar.getTime();  
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	       String result = format.format(today);  
	       return result;  
	   }  
	/**
	 * 得到本月的第一天
	 * 
	 * @return
	 */
	public static Date getMonthFirstDay(Date now) {
		Calendar calendar = Calendar.getInstance();
		try {
			Date dateStartTime = dateTimeformat.parse(dateformat.format(now)
					+ " 00:00:00");
			calendar.setTime(dateStartTime);
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		} catch (Exception ex) {

		}
		return calendar.getTime();
	}

	/**
	 * 得到本月的最后一天
	 * 
	 * @return
	 */
	public static Date getMonthLastDay(Date now) {
		Calendar calendar = Calendar.getInstance();
		try {
			Date dateStartTime = dateTimeformat.parse(dateformat.format(now)
					+ " 23:59:59");
			calendar.setTime(dateStartTime);
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		} catch (Exception ex) {

		}
		return calendar.getTime();
	}

	public static Date getDayStart(Date date) {
		Date dateStartTime = new Date();
		try {
			dateStartTime = dateTimeformat.parse(dateformat.format(date)
					+ " 00:00:00");
		} catch (Exception ex) {

		}
		return dateStartTime;
	}

	public static Date getDayStart(Date date, String time) {
		Date dateStartTime = new Date();
		try {
			dateStartTime = dateTimeformat.parse(dateformat.format(date) + " "
					+ time);
		} catch (Exception ex) {

		}
		return dateStartTime;
	}

	public static Date getDayEnd(Date date) {
		Date dateStartTime = new Date();
		try {
			dateStartTime = dateTimeformat.parse(dateformat.format(date)
					+ " 23:59:59");
		} catch (Exception ex) {

		}
		return dateStartTime;
	}

	public static Date getDayEnd(Date date, String time) {
		Date dateStartTime = new Date();
		try {
			dateStartTime = dateTimeformat.parse(dateformat.format(date) + " "
					+ time);
		} catch (Exception ex) {

		}
		return dateStartTime;
	}

	// timeMeasure=0,1,2,3 秒 分 时 日
	public static long getDifferent(Date startdate, Date enddate,
			int timeMeasure) {
		try {
			if (startdate == null) {
				return 1;
			}
			if (enddate == null) {
				return 1;
			}
			long start = startdate.getTime() / 1000;
			long end = enddate.getTime() / 1000;
			long time_diff = end - start;
			// 相差的秒数
			if (timeMeasure == 0) {
				return time_diff;
			}
			// 相差的分钟数
			time_diff = time_diff / 60;
			if (timeMeasure == 1) {
				return time_diff;
			}
			// 相差的小时数
			time_diff = time_diff / 60;
			if (timeMeasure == 2) {
				return time_diff;
			}
			// 相差的天数
			time_diff = time_diff / 24;
			if (timeMeasure == 3) {
				return time_diff;
			}
		} catch (Exception ex) {

		}
		return 0;
	}

	/**
	 * 获得指定日期的前几天 -n 后几天 +n
	 * 
	 * @param date
	 * @param skip +-n
	 * @return
	 * @throws Exception
	 */
	public static Date dayBeforeOrAfter(Date date,int skip) {

		Calendar c = Calendar.getInstance();

		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + skip);

		return c.getTime();
	}
	/**
	 * 获得指定日期的前几月 -n 后几月 +n
	 * 
	 * @param date
	 * @param skip +-n
	 * @return
	 * @throws Exception
	 */
	public static Date monthBeforeOrAfter(Date date,int skip) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.MONTH);
		c.set(Calendar.MONTH, day + skip);
		return c.getTime();
	}
	
	
	
	/**
	 * 获得指定日期的前几分钟 -n 后几分钟 +n
	 * 
	 * @param date
	 * @param skip +-n
	 * @return
	 * @throws Exception
	 */
	public static Date minuteBeforeOrAfter(Date date,int skip) {

		Calendar c = Calendar.getInstance();

		c.setTime(date);
		int minute = c.get(Calendar.MINUTE);
		c.set(Calendar.MINUTE, minute + skip);

		return c.getTime();
	}

	public static void main(String[] args) {
//		try {
//	        Date earlydate = new Date();   
//	        Date latedate = new Date();   
//	        DateFormat df = DateFormat.getDateInstance();   
//	        try {   
//	            earlydate = df.parse("2016-07-18");   
//	            latedate = df.parse("2016-07-28");   
//	        } catch (Exception e) {   
//	              e.printStackTrace();   
//	          }   
//	         int days = daysBetween(earlydate,latedate);  
//	         Calendar cal = Calendar.getInstance();
//	         cal.setTime(earlydate);
//	         cal.set(java.util.Calendar.HOUR_OF_DAY, 0);   
//	         cal.set(java.util.Calendar.MINUTE, 0);   
//	         cal.set(java.util.Calendar.SECOND, 0);   
//	         for (int i = 0; i <= days; i++) {
//	        	 if(i!=0){
//	        		 cal.add(Calendar.DAY_OF_YEAR, 1);
//	        	 }
//	        	 System.out.println(cal.get(Calendar.DAY_OF_WEEK));
//	        	 System.out.println(cal.getTime().toLocaleString());
//			}
////	         System.out.println(days);   
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
		System.out.println(DateUtils.parseDateTime(new Date()));
	}
	
	  
    /**
     * get first date of given month and year
     * @param year
     * @param month
     * @return
     */
    public String getFirstDayOfMonth(int year,int month){
        String monthStr = month < 10 ? "0" + month : String.valueOf(month);
        return year + "-"+monthStr+"-" +"01";
    }
    
    /**
     * get the last date of given month and year
     * @param year
     * @param month
     * @return
     */
    public String getLastDayOfMonth(int year,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR , year);
        calendar.set(Calendar.MONTH , month - 1);
        calendar.set(Calendar.DATE , 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR , -1);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" +
               calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * get Calendar of given year
     * @param year
     * @return
     */
    private Calendar getCalendarFormYear(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);      
        cal.set(Calendar.YEAR, year);
        return cal;
    }
    
    /**
     * get start date of given week no of a year
     * @param year
     * @param weekNo
     * @return
     */
    public String getStartDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
               cal.get(Calendar.DAY_OF_MONTH);    
        
    }
    
    /**
     * get the end day of given week no of a year.
     * @param year
     * @param weekNo
     * @return
     */
    public String getEndDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
               cal.get(Calendar.DAY_OF_MONTH);    
    }
	

	public static String getFirstDayOfWeek() {
		Calendar monday = Calendar.getInstance();
		return DateFormatUtils.format( getADayOfWeek(monday, Calendar.MONDAY), "yyyy-MM-dd");
	}

	public static Calendar getFirstDayOfWeek(Calendar day) {
		Calendar monday = (Calendar) day.clone();
		return getADayOfWeek(monday, Calendar.MONDAY);
	}

	public static String getLastDayOfWeek() {
		Calendar sunday = Calendar.getInstance();
		return DateFormatUtils.format( getADayOfWeek(sunday, Calendar.SUNDAY), "yyyy-MM-dd");
	}

	public static Calendar getLastDayOfWeek(Calendar day) {
		Calendar sunday = (Calendar) day.clone();
		return getADayOfWeek(sunday, Calendar.SUNDAY);
	}

	private static Calendar getADayOfWeek(Calendar day, int dayOfWeek) {
		int week = day.get(Calendar.DAY_OF_WEEK);
		if (week == dayOfWeek) {
			return day;
		}
		int diffDay = dayOfWeek - week;
		if (week == Calendar.SUNDAY) {
			diffDay -= 7;
		} else if (dayOfWeek == Calendar.SUNDAY) {
			diffDay += 7;
		}
		day.add(Calendar.DATE, diffDay);
		return day;
	}
	
	public static String parseDate(Date date){
		return dateformat.format(date );
	}
	
	
	
	public static String parseDateTime(Date date){
        return dateTimeformat.format(date);
    }
	
	public static Date parseDateTime(String date) throws ParseException{
        return dateTimeformat.parse(date);
    }
	
	public static String parseDateTimeStr(Date date){
		//时分秒毫秒
		return DateFormatUtils.format(date , "HHmmssSSS");
	}
	
	
	public static final int daysBetween(Date early, Date late) { 
	     
        java.util.Calendar calst = java.util.Calendar.getInstance();   
        java.util.Calendar caled = java.util.Calendar.getInstance();   
        calst.setTime(early);   
         caled.setTime(late);   
         //设置时间为0时   
         calst.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         calst.set(java.util.Calendar.MINUTE, 0);   
         calst.set(java.util.Calendar.SECOND, 0);   
         caled.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         caled.set(java.util.Calendar.MINUTE, 0);   
         caled.set(java.util.Calendar.SECOND, 0);   
        //得到两个日期相差的天数   
         int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst   
                .getTime().getTime() / 1000)) / 3600 / 24;   
         
        return days;   
   }   
	/**
     * 获取年月对应的date对象
     * @description:
     *@param monthDate
     *@return
     *@version: V1.00
     */
    public static Date toMonthDate(String monthDate){
        try {
            return monthFormat.parse(monthDate);
        } catch (ParseException e) {
            throw new BusinessException("请输入正确的年月，格式为yyyy.MM，例如 : 2016.09");
        }
    }
}
