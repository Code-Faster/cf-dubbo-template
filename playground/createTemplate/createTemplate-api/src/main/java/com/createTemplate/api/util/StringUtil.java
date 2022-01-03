package com.createTemplate.api.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	private static Map<String, String> numToStringMap = new HashMap();

	static {
		numToStringMap.put("0", "");
		numToStringMap.put("1", "一");
		numToStringMap.put("2", "二");
		numToStringMap.put("3", "三");
		numToStringMap.put("4", "四");
		numToStringMap.put("5", "五");
		numToStringMap.put("6", "六");
		numToStringMap.put("7", "七");
		numToStringMap.put("8", "八");
		numToStringMap.put("9", "九");
		numToStringMap.put("10", "十");
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getString(Object obj) {
		if ((obj instanceof Long) || (obj instanceof Integer)) {
			return obj == null ? null : obj.toString();
		}
		return ((String) obj);
	}

	public static String generateCaptcha(int numLength) {
		String str = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
		String[] str2 = str.split(",");
		Random rand = new Random();
		int index = 0;
		String randStr = "";
		randStr = "";
		for (int i = 0; i < numLength; ++i) {
			index = rand.nextInt(str2.length - 1);
			randStr = randStr + str2[index];
		}
		return randStr;
	}

	public static List<Long> stringToLongList(String str) {
		String[] arrayStr = str.split("\\,");
		List list = new ArrayList();
		for (String tempStr : arrayStr) {
			list.add(Long.valueOf(Long.parseLong(tempStr)));
		}

		return list;
	}

	public static Long[] stringToLongArray(String str) {
		String[] arrayStr = str.split("\\,");
		Long[] array = new Long[arrayStr.length];
		int index = 0;
		for (String tempStr : arrayStr) {
			array[index] = Long.valueOf(Long.parseLong(tempStr));
			++index;
		}
		return array;
	}

	public static String queNumberToString(int queNumber) {
		String queNumberStr = queNumber + "";
		int length = queNumberStr.length();
		if (length == 1){
			return ((String) numToStringMap.get(queNumber));
		}
		int first = queNumber / 10;
		int second = queNumber % 10;
		if (first == 1){
			return ((String) numToStringMap.get("10"))
					+ ((String) numToStringMap.get(new StringBuilder(String.valueOf(second)).toString()));
		}
		return ((String) numToStringMap.get(new StringBuilder(String.valueOf(first)).toString()))
				+ ((String) numToStringMap.get("10"))
				+ ((String) numToStringMap.get(new StringBuilder(String.valueOf(second)).toString()));
	}

	public static boolean checkNull(Object obj) {
		return ((obj == null) || (obj.toString().length() == 0));
	}

	public static boolean checkNotNull(Object obj) {
		return ((obj != null) && (obj.toString().length() != 0));
	}

	public static String generateNumberCaptcha(int length) {
		String str = "0,1,2,3,4,5,6,7,8,9";
		String[] str2 = str.split(",");
		Random rand = new Random();
		int index = 0;
		String randStr = "";
		randStr = "";
		for (int i = 0; i < length; ++i) {
			index = rand.nextInt(str2.length - 1);
			randStr = randStr + str2[index];
		}
		return randStr;
	}

	public static String objectToString(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	public static Integer objectToInteger(Object obj) {
		if (obj == null) {
			return null;
		}
		return Integer.valueOf(Integer.parseInt(obj.toString()));
	}

	public static Long objectToLong(Object obj) {
		if (obj == null) {
			return null;
		}
		return Long.valueOf(Long.parseLong(obj.toString()));
	}

	private static String encrypt(String inputText, String algorithmName) {
		if ((inputText == null) || ("".equals(inputText.trim()))) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}

		if ((algorithmName == null) || ("".equals(algorithmName.trim()))) {
			algorithmName = "md5";
		}

		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte[] s = m.digest();

			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString(arr[i] & 0xFF | 0x100).substring(1, 3));
		}

		return sb.toString();
	}

	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}

	public static boolean isBank(String temp) {
		return ((temp == null) || ("".equals(temp)));
	}

	public static BigDecimal amountToYuan(Integer amount) {
		BigDecimal temp = new BigDecimal(amount.intValue()).divide(new BigDecimal(100)).setScale(2, 4);
		return temp;
	}

	public static boolean checkInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
     * 加密显示电话号码
     * @param phoneNO
     * @return
     */
    public static String encryptPhoneNO(String phoneNO) {
        return phoneNO.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
    
    /**
     * 加密显示邮箱
     * @param phoneNO
     * @return
     */
    public static String encryptEmail(String email) {
        return email.replaceAll("(\\w)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$4");
    }

	public static boolean checkLetter(String str) {
		if (str == null) {
			return false;
		}
		return str.matches("[A-Z]+");
	}

	public static boolean checkNumber(String str) {
		if (str == null) {
			return false;
		}
		return str.matches("[0-9]+");
	}

	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static void main(String[] args) {
		new StringUtil();
		System.out.println(isMobileNO("17089116690"));
	}

	public static String Html2Text(String inputString) {
		String htmlStr = inputString;
		String textStr = "";

		String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
		String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
		String regEx_html = "<[^>]+>";

		Pattern p_script = Pattern.compile(regEx_script, 2);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll("");

		Pattern p_style = Pattern.compile(regEx_style, 2);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll("");

		Pattern p_html = Pattern.compile(regEx_html, 2);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll("");

		textStr = htmlStr;
		return textStr;
	}
}