package com.people.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/**
	 * �??????��?��??�????�?�?串转为�?��??
	 * 
	 * @param yyyyMMdd �?�?串�?��?? 
	 * @return ??��??
	 */
	public static Date string2Date(String yyyyMMdd){
		if (null != yyyyMMdd && yyyyMMdd.matches("^\\d{8}$")){
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			try {
				return format.parse(yyyyMMdd);
			} catch (ParseException e) {
				return new Date();
			}
		} 
		
		return new Date();
	}
	
	/**
	 * yyyyMMdd -> MMdd
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static String formatMonthDay(String yyyyMMdd){
		if (null != yyyyMMdd && yyyyMMdd.matches("^\\d{8}$")){
			return yyyyMMdd.substring(4);
		}
		return yyyyMMdd;
	}
	
	/**
	 * yyyyMMdd -> yyyy-MM-dd
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static String formatDateStr(String yyyyMMdd){
		if (null != yyyyMMdd && yyyyMMdd.matches("^\\d{8}$")){
			StringBuffer sb = new StringBuffer(yyyyMMdd);
			sb.insert(4, "-");
			sb.insert(7, "-");
			return sb.toString();
		}
		
		return yyyyMMdd;
	}
	
	/**
	 * ??��????��??�????yyyy-MM-dd 
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		if (null != date){
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
		
		return "";
	}
	
	/**
	 * ??��????��??�????yyyyMMdd 
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate2(Date date){
		if (null != date){
			return new SimpleDateFormat("yyyyMMdd").format(date);
		}
		
		return "";
	}
	
	/**
	 * ??��????��??�????HHmmss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date){
		if (null != date){
			return new SimpleDateFormat("HHmmss").format(date);
		}
		
		return "";
	}
	
	/**
	 * ???�??????��?��?? yyyy-MM-dd
	 * @return
	 */
	public static String getSystemDate(){
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	/**
	 * ??��????��??�????MMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String getSystemMonthDay() {
		return new SimpleDateFormat("MMdd").format(new Date());
	}
	
	/**
	 * ???�??????��?��??
	 * 
	 * @return HHmmss
	 */
	public static String getSystemTime(){
		return new SimpleDateFormat("HHmmss").format(new Date());
	}
	
	/**
	 * ???�??????��?��????��?? MMddHHmmss
	 * @return
	 */
	public static String getSystemDateTime(){
		return new SimpleDateFormat("MMddHHmmss").format(new Date());
	}
	
	public static String formathhmmss(String hhmmss){
		if (null != hhmmss && hhmmss.length() == 6){
			StringBuffer sb = new StringBuffer(hhmmss);
			sb.insert(2, ":");
			sb.insert(5, ":");
			return sb.toString();
		}
		
		return "";
	}
	
	/**
	 * �?yyyyMMddhhmmss??��???????��??�????�?yyyy-MM-dd hh:mm:ss??��??
	 * 
	 * @param yyyyMMddhhmmss
	 * @return
	 */
	public static String formatDateTime(String yyyyMMddhhmmss){
		try{
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = format1.parse(yyyyMMddhhmmss.replace(" ", ""));
			
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format2.format(date);
		}catch(Exception e){
			e.printStackTrace();
			return yyyyMMddhhmmss;
		}
	}
	
	/**
	 * MMdd -> MM-dd
	 * 
	 * @param MMdd
	 * @return
	 */
	public static String formatMMddDash(String MMdd){
		if (null != MMdd && MMdd.length() == 4){
			StringBuffer sb = new StringBuffer(MMdd);
			sb.insert(2, "-");
			return sb.toString();
			
		} 
		return MMdd;
	}
	
	/**
	 * 计�??两个??��??�???��?�差???天�?��??注�??�?????????��?????java.sql.Date???
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(java.sql.Date date1,java.sql.Date date2)     
    {     
        Calendar cal = Calendar.getInstance();     
        cal.setTime(date1);     
        long time1 = cal.getTimeInMillis();                  
        cal.setTime(date2);     
        long time2 = cal.getTimeInMillis();          
        long between_days=(time2-time1)/(1000*3600*24);     
             
       return Integer.parseInt(String.valueOf(between_days));            
    }  

}
