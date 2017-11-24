package com.kanuhasu.ap.out.recon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
	
	/* -----------------| INSTANCE |-----------------*/
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static SimpleDateFormat sdf__yyyy$MM$dd_HH$mm$ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * yyyyMMdd
	 */
	public static SimpleDateFormat sdf__yyyyMMdd= new SimpleDateFormat("yyyyMMdd");
	/**
	 * MMddyyyy HH:mm:ss
	 */
	public static SimpleDateFormat sdf__MMddyyyy_HH$mm$ss = new SimpleDateFormat("MMddyyyy HH:mm:ss");	
	/**
	 * yyyy-MM-dd
	 */
	public static SimpleDateFormat sdf__yyyy$MM$dd= new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 
	 */
	//public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	//public static DateTimeFormatter dateTimeFormatterBasic = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	static{
		sdf__yyyy$MM$dd_HH$mm$ss.setLenient(false);
		sdf__yyyyMMdd.setLenient(false);
		sdf__MMddyyyy_HH$mm$ss.setLenient(false);
	}
	
	/* -----------------| BUSINESS |-----------------*/

	/** | PARSE |**/
	
	/**
	 * Parses with "yyyy-MM-dd HH:mm:ss"
	 */
	public static Date parse(String dateStr){
		try {
			//dateTimeFormatter.parse(dateStr);
			return DateUtil.parse(dateStr, DateUtil.sdf__yyyy$MM$dd_HH$mm$ss);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Parses with "yyyyMMdd"
	 */
	public static Date parseBasic(String dateStr){
		try {
			//dateTimeFormatterBasic.parse(dateStr);
			return DateUtil.parse(dateStr, DateUtil.sdf__yyyyMMdd);
		}
		catch (Exception e) {
			return null;
		}		
	}	
	
	public static Date parse(String dateStr, SimpleDateFormat sdf){
		Date parserDate= null;
		if(StringUtils.isNotBlank(dateStr)){
			dateStr= dateStr.replaceAll("\\P{Print}","");
			//trim
			dateStr= dateStr.trim();
			try {
				parserDate= sdf.parse(dateStr);
			}
			catch (ParseException e) {
				parserDate= null;
			}
		}
		return parserDate;
	}
	
	/** | FORMAT |**/
	
	public static String format(Date date, SimpleDateFormat sdf){
		try {
			return sdf.format(date);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		//System.out.println(DateUtil.parse("2016-04-18 00:00:00"));
		String ip= "YYYY-MM-DD 00:00:00  HH:mm:ss UTC";
		String[] ipAry= ip.split(" ");
		for(String str:ipAry) {
			System.out.println(str);
		}
	}
}