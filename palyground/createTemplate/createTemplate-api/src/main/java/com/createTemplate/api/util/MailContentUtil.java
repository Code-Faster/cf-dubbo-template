package com.createTemplate.api.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MailContentUtil {

	/**
	  * @Description：获取绑定邮箱的邮件内容
	  * @author aijian
	  * @Version: V1.00 
	  * @Create Date: 2014-2-26
	  * @Parameters：
	  * @return：content 文本；filePathMap 需要的文件路径
	 */
	public static Map getBindEmailContent(String realName,String userCode,String url){
		if(realName == null || "".equals(realName)){
			realName = userCode;
		}
		int year = Calendar.getInstance().get(Calendar.YEAR);
		StringBuffer buffer = new StringBuffer();
		buffer
		.append("<html>")
		.append("	<body>")
		.append("		<div id='wrap' style='width:700px;height:540px;margin:auto;background:#FFFFFF;padding:0 100px;' >") 
		.append("			<div id='top' style='position:relative;height:44px;line-height:44px;border-bottom:#CCCCCC 1px solid;'>")
		.append("				<div class='left' style='position:absolute;left:20px;top:2px;height:40px;width:240px;'></div>")
		.append("				<div class='right' style='position:absolute;right:10px;font-size:14px;top:2px' ><span><a href='http://www.kwhlnet.com' style='color:#0077FF;text-decoration:none'>访问首页</a></span></div>") 
		.append("			</div>") 
		.append("			<div class='member' style='font-size:16px;font-weight:bold;margin-top:32px;margin-bottom:20px;' >")
		.append("				亲爱的会员用户：")
		.append("			</div>")
		.append("			<div>您注册的账号 " + userCode + " 已经申请绑定邮箱，请点击以下链接确认绑定！</div>")
		.append("			<div class='login'>")
		.append("				<a href='" + url + "' style='color:#FF6600;margin-top:30px;margin-bottom:30px;display:block;font-weight:bold;font-size:14px;'>") 
		.append(" 				点此绑定邮箱</a>")
		.append("			</div>")
		.append("			<div class='click' style='margin-bottom:30px;' >")
		.append("				如果上述文字点击无效，请把下面的网址复制到浏览器地址栏中打开<br />") 
		.append("				<a href='" + url + "' style='color:#FF6600' >" + url + "</a>") 
		.append("			</div>")
		.append("			<div style='clear:both'></div>")
		.append("			<div class='tips' style='height:280px;border-top:#CCCCCC 1px dotted;padding-top:24px;margin-top:80px;color:#7F7F7F;line-height:30px;font-size:13px;'>")
		.append("				<div>此为系统邮件，请勿回复</div>")
		.append("				<div>请保管好您的邮箱，避免账户被他人盗用</div>")
		.append("			  	<div style='margin-top:20px;'>")
		.append("					 如有任何疑问，可查看启程网相关规则，启程网网站访问 <a href='http://www.kwhlnet.com'>http://www.kwhlnet.com</a>" )
		.append("				</div>") 
		.append("				<div>Copyright启程网2017-" + year + " All Right Reserved</div>")
		.append("			</div>") 
		.append("		</div>") 
		.append("	</body>")
		.append("</html>");
		Map map = new HashMap();
		Map<String, String> filePathMap = new HashMap<String, String>();
		//filePathMap.put("logoImg", "logo.png");
		map.put("content", buffer.toString());
		map.put("filePathMap", filePathMap);
		return map;
	}
	
	/**
	  * @Description：获取校验邮箱的邮件内容
	  * @author aijian
	  * @Version: V1.00 
	  * @Create Date: 2014-2-26
	  * @Parameters：
	  * @return：content 文本；filePathMap 需要的文件路径
	 */
	public static Map getCheckEmailContent(String realName,String userCode,String url){
		if(realName == null || "".equals(realName)){
			realName = userCode;
		}
		int year = Calendar.getInstance().get(Calendar.YEAR);
		StringBuffer buffer = new StringBuffer();
		buffer
		.append("<html>")
		.append("	<body>")
		.append("		<div id='wrap' style='width:700px;height:540px;margin:auto;background:#FFFFFF;padding:0 100px;' >") 
		.append("			<div id='top' style='position:relative;height:44px;line-height:44px;border-bottom:#CCCCCC 1px solid;'>")
		.append("				<div class='left' style='position:absolute;left:20px;top:2px;height:40px;width:240px;'></div>")
		.append("				<div class='right' style='position:absolute;right:10px;font-size:14px;top:2px' ><span><a href='http://www.kwhlnet.com' style='color:#0077FF;text-decoration:none'>访问首页</a></span></div>") 
		.append("			</div>") 
		.append("			<div class='member' style='font-size:16px;font-weight:bold;margin-top:32px;margin-bottom:20px;' >")
		.append("				亲爱的会员用户：")
		.append("			</div>")
		.append("			<div>您注册的账号 " + userCode + " ，请点击以下链接确认校验邮箱！</div>")
		.append("			<div class='login'>")
		.append("				<a href='" + url + "' style='color:#FF6600;margin-top:30px;margin-bottom:30px;display:block;font-weight:bold;font-size:14px;'>") 
		.append(" 				点此校验邮箱</a>")
		.append("			</div>")
		.append("			<div class='click' style='margin-bottom:30px;' >")
		.append("				如果上述文字点击无效，请把下面的网址复制到浏览器地址栏中打开<br />") 
		.append("				<a href='" + url + "' style='color:#FF6600' >" + url + "</a>") 
		.append("			</div>")
		.append("			<div style='clear:both'></div>")
		.append("			<div class='tips' style='height:280px;border-top:#CCCCCC 1px dotted;padding-top:24px;margin-top:80px;color:#7F7F7F;line-height:30px;font-size:13px;'>")
		.append("				<div>此为系统邮件，请勿回复</div>")
		.append("				<div>请保管好您的邮箱，避免账户被他人盗用</div>")
		.append("			  	<div style='margin-top:20px;'>")
		.append("					 如有任何疑问，可查看启程网相关规则，启程网网站访问 <a href='http://www.kwhlnet.com'>http://www.kwhlnet.com</a>" )
		.append("				</div>") 
		.append("				<div>Copyright启程网2017-" + year + " All Right Reserved</div>")
		.append("			</div>") 
		.append("		</div>") 
		.append("	</body>")
		.append("</html>");
		Map map = new HashMap();
		Map<String, String> filePathMap = new HashMap<String, String>();
		//filePathMap.put("logoImg", "logo.png");
		map.put("content", buffer.toString());
		map.put("filePathMap", filePathMap);
		return map;
	}
	
	/**
	  * @Description：获取找回密码的邮件内容
	  * @author aijian
	  * @Version: V1.00 
	  * @Create Date: 2014-2-26
	  * @Parameters：
	  * @return：content 文本；filePathMap 需要的文件路径
	 */
	public static Map getForgetPwdContent(String realName,String userCode,String url){
		if(realName == null || "".equals(realName)){
			realName = userCode;
		}
		int year = Calendar.getInstance().get(Calendar.YEAR);
		StringBuffer buffer = new StringBuffer();
		buffer
		.append("<html>")
		.append("	<body>")
		.append("		<div id='wrap' style='width:700px;height:540px;margin:auto;background:#FFFFFF;padding:0 100px;' >") 
		.append("			<div id='top' style='position:relative;height:44px;line-height:44px;border-bottom:#CCCCCC 1px solid;'>")
		.append("				<div class='left' style='position:absolute;left:20px;top:2px;height:40px;width:240px;'></div>")
		.append("				<div class='right' style='position:absolute;right:10px;font-size:14px;top:2px' ><span><a href='http://www.kwhlnet.com' style='color:#0077FF;text-decoration:none'>访问首页</a></span></div>") 
		.append("			</div>") 
		.append("			<div class='member' style='font-size:16px;font-weight:bold;margin-top:32px;margin-bottom:20px;' >")
		.append("				亲爱的会员用户：")
		.append("			</div>")
		.append("			<div>您注册的账号 " + userCode + " 已申请找回密码，请点击以下链接修改密码！</div>")
		.append("			<div class='login'>")
		.append("				<a href='" + url + "' style='color:#FF6600;margin-top:30px;margin-bottom:30px;display:block;font-weight:bold;font-size:14px;'>") 
		.append(" 				点此修改密码</a>")
		.append("			</div>")
		.append("			<div class='click' style='margin-bottom:30px;' >")
		.append("				如果上述文字点击无效，请把下面的网址复制到浏览器地址栏中打开<br />") 
		.append("				<a href='" + url + "' style='color:#FF6600' >" + url + "</a>") 
		.append("			</div>")
		.append("			<div style='clear:both'></div>")
		.append("			<div class='tips' style='height:280px;border-top:#CCCCCC 1px dotted;padding-top:24px;margin-top:80px;color:#7F7F7F;line-height:30px;font-size:13px;'>")
		.append("				<div>此为系统邮件，请勿回复</div>")
		.append("				<div>请保管好您的邮箱，避免账户被他人盗用</div>")
		.append("			  	<div style='margin-top:20px;'>")
		.append("					 如有任何疑问，可查看启程网相关规则，启程网网站访问 <a href='http://www.kwhlnet.com'>http://www.kwhlnet.com</a>" )
		.append("				</div>") 
		.append("				<div>Copyright启程网2017-" + year + " All Right Reserved</div>")
		.append("			</div>") 
		.append("		</div>") 
		.append("	</body>")
		.append("</html>");
		Map map = new HashMap();
		Map<String, String> filePathMap = new HashMap<String, String>();
		//filePathMap.put("logoImg", "logo.png");
		map.put("content", buffer.toString());
		map.put("filePathMap", filePathMap);
		return map;
	}
	
	/**
	 * 
	 * @description:发送验证码
	 *@param captcha  验证码
	 *@param 0 绑定邮箱  1 找回密码 2 修改邮箱账号 3 邮箱注册
	 *@return
	 *@author: 
	 *@version: V1.00
	 */
	public static Map getSendCaptchaContent(String captcha,Integer flag){
		int year = Calendar.getInstance().get(Calendar.YEAR);
		StringBuffer buffer = new StringBuffer();
		buffer
		.append("<html>")
		.append("	<body>")
		.append("		<div id='wrap' style='width:700px;height:540px;margin:auto;background:#FFFFFF;padding:0 100px;' >") 
		.append("			<div id='top' style='position:relative;height:44px;line-height:44px;border-bottom:#CCCCCC 1px solid;'>")
		.append("				<div class='left' style='position:absolute;left:20px;top:2px;height:40px;width:240px;'></div>")
		.append("				<div class='right' style='position:absolute;right:10px;font-size:14px;top:2px' ><span><a href='http://www.kwhlnet.com' style='color:#0077FF;text-decoration:none'>访问首页</a></span></div>") 
		.append("			</div>") 
		.append("			<div class='member' style='font-size:16px;font-weight:bold;margin-top:32px;margin-bottom:20px;' >")
		.append("				亲爱的会员用户：")
		.append("			</div>");
		
		switch (flag) {
		case 0:
			/**绑定邮箱*/
			buffer.append("			<div>您好！感谢您使用启程网，您正在绑定邮箱，本次请求的验证码为：" + captcha + "</div>");
			break;
		case 1:
			/**找回密码*/
			buffer.append("			<div>您好！感谢您使用启程网，您正在找回密码，本次请求的验证码为：" + captcha + "</div>");
			break;
		case 2:
			/**更换邮箱*/
			buffer.append("			<div>您好！感谢您使用启程网，您正在更换邮箱账号，本次请求的验证码为：" + captcha + "</div>");
			break;	
		case 3:
			/**邮箱注册*/
			buffer.append("			<div>您好！感谢您使用启程网，您正在注册邮箱账号，本次请求的验证码为：" + captcha + "</div>");
			break;		
		default:
			/**找回密码*/
			buffer.append("			<div>您好！感谢您使用启程网，您正在找回密码，本次请求的验证码为：" + captcha + "</div>");
			break;
		}
	
		buffer.append("			<div style='clear:both'></div>")
		.append("			<div class='tips' style='height:280px;border-top:#CCCCCC 1px dotted;padding-top:24px;margin-top:80px;color:#7F7F7F;line-height:30px;font-size:13px;'>")
		.append("				<div>此为系统邮件，请勿回复</div>")
		.append("				<div>请保管好您的邮箱，避免账户被他人盗用</div>")
		.append("			  	<div style='margin-top:20px;'>")
		.append("					 如有任何疑问，可查看启程网相关规则，启程网网站访问 <a href='http://www.kwhlnet.com'>http://www.kwhlnet.com</a>" )
		.append("				</div>") 
		.append("				<div>Copyright启程网2017-" + year + " All Right Reserved</div>")
		.append("			</div>") 
		.append("		</div>") 
		.append("	</body>")
		.append("</html>");
		Map map = new HashMap();
		Map<String, String> filePathMap = new HashMap<String, String>();
		//filePathMap.put("logoImg", "logo.png");
		map.put("content", buffer.toString());
		map.put("filePathMap", filePathMap);
		return map;
	}
	
	
}
