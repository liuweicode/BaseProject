package com.wy.utils.lang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * 描述：日期工具类
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-2-26
 */
public class DateUtil {
	/**
	 * 
	 * @param timeMillis
	 * @return    String :  2011-16-45 02:30:11
	 */
	public static String timeMillis2TimeStr( long timeMillis ){
		
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTimeInMillis(timeMillis) ;
		
		return calendar2TimeStr(calendar) ; 
	}
	
	/**
	 * 
	 * @param date
	 * @return    String :  2011-16-45 02:30:11
	 */
	public static String date2TimeStr(Date date){
		
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTimeInMillis(date.getTime()) ;
		
		return calendar2TimeStr(calendar) ;  
	}
	
	/**
	 * 
	 * @param date
	 * @return    String :  2011-16-45 02:30:11
	 */
	public static String date2TimeStr(Date date,String pattern){
		return new SimpleDateFormat(pattern,Locale.getDefault()).format(date);
	}
	
	/**
	 * 
	 * @param calendar
	 * @return    String :  2011-16-45 02:30:11
	 */
	public static String calendar2TimeStr ( Calendar  calendar){
		String year = String.valueOf(calendar.get(Calendar.YEAR)) ;
		String month  = String.format("%02d",calendar.get(Calendar.MONTH)+1 ) ;
		String day = String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH)) ;
		String hour = String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) ;
		String minutes = String.format("%02d",calendar.get(Calendar.MINUTE)) ;
		String seconds = String.format("%02d",calendar.get(Calendar.SECOND)) ;
		
		StringBuilder sb = new StringBuilder() ;
		sb.append(year).append('-').append(month).append('-').append(day).append(' ').append(hour).append(':').append(minutes).append(':').append(seconds);
		return  sb.toString() ;
	}
	
	/**
	 * 
	 * @param dateStr  dateStr ：2011-16-45 02:30:11 
	 * @return
	 */
	public static Date timeStr2date(String dateStr){
		Calendar calendar = timeStr2calendar(dateStr) ;
		Date date = calendar.getTime() ;
		return date ;
	}
	
	/**
	 * 
	 * @param dateStr  dateStr ：2011-16-45 02:30:11 
	 * @return
	 */
	public static Calendar timeStr2calendar( String dateStr){
		
		String [] dateTime = dateStr.split(" ") ;
		String date = dateTime[0] ;
		String time = dateTime[1] ;
		String[] dateArra = date.split("-") ;
		String[]  timeArra = time.split(":") ;
		
		int year = Integer.valueOf( dateArra[0] );
		int month = Integer.valueOf(  dateArra[1]) ;
		int day = Integer.valueOf(  dateArra[2]) ;
		int hour = Integer.valueOf(timeArra[0]) ;
		int minute = Integer.valueOf(timeArra[1]) ;
		int second = Integer.valueOf(timeArra[2]) ;
		
		Calendar calendar = Calendar.getInstance() ;
		calendar.set(year, month, day, hour, minute, second) ;
		
		return calendar ;
	}
	
	/**
	 * 检查传入字符串是否为规定格式  ：2011-16-45 02:30:11
	 * @param timeStr 被检查字符串
	 * @return boolean 是：true， 不是：false
	 */
	public static boolean isTimeStr( String timeStr ){
		
		if ( 19 == timeStr.length() ){
			String reg = "^[1-9]\\d{3}-(?:0[1-9]|1[012])-(?:0[1-9]|[12]\\d|3[01])\\s(?:[01]\\d|2[0123]):(?:[0-4]\\d|5[0-9]):(?:[0-4]\\d|5[0-9])$" ;

			Pattern pattern = Pattern.compile(reg) ;
			Matcher matcher = pattern.matcher(timeStr) ;
			if( matcher.find() ){
				return true ;
			}else{
				return false ;
			}
		}else{
			return false ;
		}
	}
	
	public static boolean isImportedStr ( String importedStr ){
		
		if( 19 == importedStr.length() ){
			String reg = "^\\d{4}\\-\\d{1,2}-\\d{1,2}-\\d{1,2}-\\d{1,2}-\\d{1,2}$" ;
			Pattern pattern = Pattern.compile(reg) ;
			Matcher matcher = pattern.matcher(importedStr) ;
			if ( matcher.find() ){
				return true ;
			}else {
				return false ;
			}
		}else {
			return false ;
		}
	}
	
	
	
	/**
	 * // 将导入目标目录名称 2011-01-06-05-40-56 转换为SQLite中支持时间格式   2008-01-03 21:54:06
	 * @param importStr    2011-01-06-05-40-56
	 * @return  String   2008-01-03 21:54:06
	 */
	public static String importStr2timeStr( String importStr ){
		StringBuilder sb = new StringBuilder( importStr ) ;
        sb.replace(10, 11, " ") ;
        sb.replace(13, 14, ":") ;
        sb.replace(16, 17, ":") ;
		String timeStr = sb.toString() ;
		return timeStr ;
	}
	
	/**
	 * 将时间格式的字符串  2008-01-03 21:54:06 转换为文件系统支持的表现形式 2011-01-06-05-40-56
	 * @param timeStr    2008-01-03 21:54:06
	 * @return  String   2011-01-06-05-40-56
	 */
	public static String timeStr2importStr( String timeStr ){
		timeStr = timeStr.replace(' ', '-') ;
		return timeStr.replace(':', '-') ;
	}
	
	private static final String FAST_FORMAT_HMMSS = "%1$d:%2$02d:%3$02d";

	/**
     * Fast formatting of h:mm:ss
     */
    public static String formatElapsedTime( long elapsedSeconds ) {
    	elapsedSeconds /=1000 ;
    	long hours = 0 ;
    	long minutes = 0 ;
    	long seconds = 0 ;
    	
    	if (elapsedSeconds >= 3600) {
            hours = elapsedSeconds / 3600;
            elapsedSeconds -= hours * 3600;
        }
        if (elapsedSeconds >= 60) {
            minutes = elapsedSeconds / 60;
            elapsedSeconds -= minutes * 60;
        }
        seconds = elapsedSeconds;
        
        return formatElapsedTime( "%1$d:%2$02d:%3$02d" , hours, minutes, seconds) ;
    }
	
    /**
     * Fast formatting of h:mm:ss
     */
    private static String formatElapsedTime( String format, long hours, long minutes, long seconds ) {
    	StringBuilder sb = new StringBuilder() ;
    	
    	if( format.equals(FAST_FORMAT_HMMSS) ){
    		if( hours < 10 ){
    			sb.append('0').append(hours) ;
    		}else{
    			sb.append(hours) ;
    		}
    		sb.append(':') ;
    		if( minutes < 10 ){
    			sb.append('0').append(minutes) ;
    		}else{
    			sb.append(minutes) ;
    		}
    		sb.append(':') ;
    		if( seconds < 10 ){
    			sb.append('0').append(seconds) ;
    		}else{
    			sb.append(seconds) ;
    		}
    		return sb.toString();
    	}else{
    		return null ;
    	}
    }
}
