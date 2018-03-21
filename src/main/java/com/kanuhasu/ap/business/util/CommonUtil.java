

package com.kanuhasu.ap.business.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.type.bo.user.Roles;
import com.kanuhasu.ap.business.type.response.Param;

public class CommonUtil {
	protected static AtomicReference<Long> currentTime = new AtomicReference<>(System.currentTimeMillis());
	private static String regexJSON = "\\s*'KEY'\\s*:\\s*'(.+?)\\s*'";
	private static String regexXML = "<KEY(.*?)>(.+?)</KEY>";

	public static <T> T fetchFromAry(T[] ipAry, int idx) {
		if(ipAry.length > idx) {
			return ipAry[idx];
		}
		return null;
	}
	
	public static String mapToStr(Map<String, String> map){
		StringBuilder str= new StringBuilder();
		if(map!=null){
			for(Entry<String, String> header: map.entrySet()){
				str.append(header.getKey()).append("|").append(header.getValue()).append("#");
			}			
		}
		return str.toString();
	}	
	
	public static List<String> fetchValFromJson(String ipStr, String key) {
		List<String> vals= new ArrayList<String>();
		if(StringUtils.isNotEmpty(ipStr) && StringUtils.isNotEmpty(key)){
			ipStr= ipStr.replace("'", "\"");
			String ipRegex = regexJSON.replace("KEY", key);
			ipRegex= ipRegex.replace("'", "\"");
			Matcher matcher = Pattern.compile(ipRegex).matcher(ipStr);
			while (matcher.find()) {
				String jStr = matcher.group();
				jStr= jStr.replace(",", "");
				jStr= new StringBuilder().append("{").append(jStr).append("}").toString();
				JsonElement jEle= new JsonParser().parse(jStr);
				JsonObject jObj = jEle.getAsJsonObject();
				String val = jObj.get(key).getAsString();
				vals.add(val);
			}			
		}
		return vals;
	}
	
	
	public static List<String> fetchValFromXML(String ipStr, String key) throws Exception {
		List<String> vals= new ArrayList<String>();
		if(StringUtils.isNotEmpty(ipStr) && StringUtils.isNotEmpty(key)){		
			String ipRegex = regexXML.replace("KEY", key);
			Matcher matcher = Pattern.compile(ipRegex).matcher(ipStr);
			while (matcher.find()) {
				String xmlEle = matcher.group();
				Document doc = CommonUtil.xmlStrToDoc(xmlEle);
				if(doc!=null){
					String val = doc.getDocumentElement().getTextContent();
					vals.add(val);					
				}
			}
		}
		return vals;
	}
	
	public static Document xmlStrToDoc(String xml) throws Exception {
		if(StringUtils.isNotEmpty(xml)){
			DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
			DocumentBuilder bldr = fctr.newDocumentBuilder();
			InputSource insrc = new InputSource(new StringReader(xml));
			return bldr.parse(insrc);			
		}
		return null;
	}	
	
	
	/**
	 * will fetch unique long value based on current time. to avoid duplicates,
	 * AtomicReference is used which provides volatile behaviour for counter.
	 */
	public static Long nextRegNo() {
		Long prev;
		Long next = System.currentTimeMillis();
		do {
			prev = currentTime.get();
			/*
			 * we need this, as its likely program executes so fast that, prev
			 * and next both might have the same value.
			 */
			next = next > prev ? next : prev + 1;
		}
		/*
		 * each time we fetch 1 new ID, we update its value in currentTime. And
		 * we take reference of it to make sure, we are threadsafe.
		 * 
		 * compareAndSet will provide a check against stale values. i.e. while
		 * updating currentTime's values, if existing copied value of
		 * currentTime isnt same as the current value of currentTime, some other
		 * thread already have taken the nextID.
		 */
		while (!currentTime.compareAndSet(prev, next));
		return next;
	}

	public void populateRoles() {

	}

	public static String fetchLoginID() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public static boolean isAdmin(Collection<SimpleGrantedAuthority> authorities) {
		boolean isAdmin = false;
		for (SimpleGrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals(Roles.ADMIN.getName())) {
				isAdmin = true;
			}
		}
		return isAdmin;
	}

	public static boolean isAdmin() {
		boolean isAdmin = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			@SuppressWarnings("unchecked")
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			for (SimpleGrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals(Roles.ADMIN.getName())) {
					isAdmin = true;
				}
			}
		}
		return isAdmin;
	}

	public static boolean isAuth(Authentication auth) {
		boolean isAuth = false;
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			isAuth = true;
		}
		return isAuth;
	}

	public static boolean isAuth() {
		boolean isAuth = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			isAuth = true;
		}
		return isAuth;
	}

	public static String fetchAuthName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public static long calculateNoOfPages(long rowCount, long rowPerPage) {
		long pageCount = 0;
		if (rowCount != 0 && rowPerPage != 0) {
			pageCount = rowCount / rowPerPage;
			if (rowCount % rowPerPage != 0) {
				pageCount++;
			}
		}
		return pageCount;
	}
	
	public static byte[] mapToByteAry(Map<String, String> ipMap) throws IOException, ClassNotFoundException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(ipMap);
        oos.close();
		return bos.toByteArray();
	}
	
	public static Map<String, String> ByteAryToMap(byte[] ipByteAry) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bia= new ByteArrayInputStream(ipByteAry);
        ObjectInputStream ois = new ObjectInputStream(bia);
		@SuppressWarnings("unchecked")
		Map<String, String> opMap = (Map<String, String>) ois.readObject();
        ois.close();
		return opMap;	
	}
	
	public static String mapToString(Map<String, String> ipMap) throws ClassNotFoundException, IOException{
		if(ipMap != null){
			byte[] byteAry= CommonUtil.mapToByteAry(ipMap);
			String Base64URL = Base64.encodeBase64URLSafeString(byteAry);
			return Base64URL;			
		}
		return null;
	}
	
	public static Map<String, String> stringToMap(String mapStr) throws ClassNotFoundException, IOException{
		if(StringUtils.isNotEmpty(mapStr)){
			byte[] byteAry = Base64.decodeBase64(mapStr);
			Map<String, String> opMap = CommonUtil.ByteAryToMap(byteAry);
			return opMap;
		}
		return null;
	}
	
	public static String fetchBaseUrl(HttpServletRequest request){
		StringBuilder builder= new StringBuilder();
		//http://
		builder.append(request.getScheme()).append("://");
		//localhost:8080/
		builder.append(request.getServerName()).append(":").append(request.getServerPort());
		//alphaplus
		builder.append(request.getContextPath());
		return builder.toString();
	}
	
	public static String buildUrl(HttpServletRequest request, String resourcePath){
		StringBuilder builder= new StringBuilder(CommonUtil.fetchBaseUrl(request));
		builder.append("/").append(resourcePath);
		return builder.toString();
	}
	
	public static String fetchXMLEleData(String ipXml, String xmlEle) {
		ipXml = "<TESTSubmitMORequest> 	<messageId>6b87c7a0-d462-4dfc-938c-34d05b0b40a9</messageId> 	<destination>85261194765</destination> 	<message>PAYTEST mId=testmerchant oId=B9PAAwBBhxhUVwAAAAAAAA</message> 	<account refType='ACR'>457756211d568eaf7a9fd1-4d705d2e1d5381a8</account> </SubmitMORequest>  ";
		xmlEle = "messageId";
		
		final Pattern pattern = Pattern.compile("<" + xmlEle + ">(.+?)</" + xmlEle + ">");
		final Matcher matcher = pattern.matcher(ipXml);
		matcher.find();
		String val = matcher.group(1);
		System.out.println(val); // Prints String I want to extract	
		return val;
	}
	
	/**
	 * It builds date given minimum year. 
	 * Works on date format of yyyy-MM-dd HH:mm:ss
	 * 
	 * Example: 
	 * 2017 				==>  2017-01-01 00:00:00
	 * 2017-02 				==>  2017-02-01 00:00:00
	 * 2017-02-22 			==>  2017-02-22 00:00:00
	 * 2017-02-22 11:00:00  ==>  2017-01-01 11:00:00
	 * 2017-02-22 11:11:00  ==>  2017-01-01 11:11:00
	 * 2017-02-22 11:11:11  ==>  2017-01-01 11:11:11
	 * 
	 * @param fullDateStr
	 * @param criteria
	 * @return full-built date
	 * @throws ParseException 
	 */
	public static Response processDate(String fullDateStr) throws ParseException {
		Boolean isFullDateAvailable = false;
		Boolean isDateAvailable = false;
		
		//if dateStr is not-empty, than at least "year" is known
		if(StringUtils.isNotEmpty(fullDateStr)) {
			List<String> dateEleList = new ArrayList<String>();
			List<String> timeEleList = new ArrayList<String>();
			
			String[] ipEles = fullDateStr.split(" ");
			
			/**process date**/
			//"fullDateEle[0]" should at least having the "date-part" and this date-part should at least year in it.
			String dateStr = ipEles[0];
			String[] dateEles = dateStr.split("-");
			if(ipEles.length > 0 && dateEles.length > 0 && StringUtils.isNotEmpty(dateEles[0])) {
				switch (dateEles.length) {
					//Only year is available i.e. add default month(01) and day(01)
					case 1:
						dateEleList.add(0, (StringUtils.isNotEmpty(dateEles[0]) ? dateEles[0] : "01"));
						dateEleList.add(1, "01");
						dateEleList.add(2, "01");
						break;
					//year and month are available i.e. add default day(01)
					case 2:
						dateEleList.add(0, (StringUtils.isNotEmpty(dateEles[0]) ? dateEles[0] : "01"));
						dateEleList.add(1, (StringUtils.isNotEmpty(dateEles[1]) ? dateEles[1] : "01"));
						dateEleList.add(2, "01");
						break;
					case 3:
						isDateAvailable = true;
						String year, month, day;
						//year
						if(StringUtils.isEmpty(dateEles[0])) {
							year = "01";
							isDateAvailable = false;
						}
						else {
							year = dateEles[0];
						}
						//month
						if(StringUtils.isEmpty(dateEles[1])) {
							month = "01";
							isDateAvailable = false;
						}
						else {
							month = dateEles[1];
						}
						//day
						if(StringUtils.isEmpty(dateEles[2])) {
							day = "01";
							isDateAvailable = false;
						}
						else {
							day = dateEles[2];
						}
						dateEleList.add(0, year);
						dateEleList.add(1, month);
						dateEleList.add(2, day);
						break;
				}
			}
			else {
				throw new ParseException("Input date[" + dateStr + "] is wrong: Not even having a year in it.", 0);
			}
			
			/**process time**/
			//time is present in input-date?
			if(ipEles.length > 1 && StringUtils.isNotEmpty(ipEles[1])) {
				String timeStr = ipEles[1];
				String[] timeEles = timeStr.split(":");
				switch (timeEles.length) {
					//Only Hours is available i.e. add default Minutes(00) and seconds(00)
					case 1:
						timeEleList.add(0, (StringUtils.isNotEmpty(timeEles[0]) ? timeEles[0] : "00"));
						timeEleList.add(1, "00");
						timeEleList.add(2, "00");
						break;
					//Hours and minutes are available i.e. add default seconds(00)
					case 2:
						timeEleList.add(0, (StringUtils.isNotEmpty(timeEles[0]) ? timeEles[0] : "00"));
						timeEleList.add(1, (StringUtils.isNotEmpty(timeEles[1]) ? timeEles[1] : "00"));
						timeEleList.add(2, "00");
						break;
					case 3:
						isFullDateAvailable = true;
						String hours, min, seconds;
						//hours
						if(StringUtils.isEmpty(timeEles[0])) {
							hours = "";
							isFullDateAvailable = false;
						}
						else {
							hours = timeEles[0];
						}
						//min
						if(StringUtils.isEmpty(timeEles[1])) {
							min = "";
							isFullDateAvailable = false;
						}
						else {
							min = timeEles[1];
						}
						//seconds
						if(StringUtils.isEmpty(timeEles[2])) {
							seconds = "";
							isFullDateAvailable = false;
						}
						else {
							seconds = timeEles[2];
						}
						timeEleList.add(0, hours);
						timeEleList.add(1, min);
						timeEleList.add(2, seconds);
						break;
					default:
						timeEleList.add(0, "00");
						timeEleList.add(1, "00");
						timeEleList.add(2, "00");
						break;
				}
			}
			//time is not present in input-date?
			else {
				timeEleList.add(0, "00");
				timeEleList.add(1, "00");
				timeEleList.add(2, "00");
			}
			
			//init str with "year"
			StringBuilder newfullDateStr = new StringBuilder(dateEleList.get(0));
			//build date part
			for (int i = 1; i <= 2; i++) {
				//append month and day
				newfullDateStr.append("-").append(dateEleList.get(i));
			}
			//build time part
			//append hours
			newfullDateStr.append(" ").append(timeEleList.get(0));
			for (int i = 1; i <= 2; i++) {
				//append minutes and seconds
				newfullDateStr.append(":").append(timeEleList.get(i));
			}
			fullDateStr = newfullDateStr.toString();
		}
		
		return Response.builder().build()
				.putParam(Param.DataType.DateTime.DATE_AVAILABLE.name(), isDateAvailable.toString())
				.putParam(Param.DataType.DateTime.FULL_DATE_AVAILABLE.name(), isFullDateAvailable.toString())
				.putParam(Param.DATA.name(), fullDateStr);
	}	
	
	public static String compressStr(String str) throws IOException {
		if(str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		String outStr = out.toString("UTF-8");
		return outStr;
	}	
	
	public static String getEleFromAry(String[] ipAry, int idx){
		String val= null;
		if(ipAry!=null && ipAry.length>0){
			try {
				val = ipAry[idx];
			}
			catch (IndexOutOfBoundsException e) {
				val= null;
			}			
		}
		return val;
	}
	
	public static int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
	    String string = bigDecimal.stripTrailingZeros().toPlainString();
	    int index = string.indexOf(".");
	    return index < 0 ? 0 : string.length() - index - 1;
	}	
	
	public static int getNumberOfDecimalPlaces001(String valStr) {
	    int index = valStr.indexOf(".");
	    return index < 0 ? 0 : valStr.length() - index - 1;
	}	
}
