package com.createTemplate.api.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 
 * 生成二维码 author：wangjintao version ：V1.00 Create date：2014-11-14 上午09:56:27
 */
@SuppressWarnings("unchecked")
@Controller
@Scope(value = "prototype")
public class EncoderAction {

	/**
	 * 删除文件夹 param folderPath 文件夹完整绝对路径
	 * 
	 * @param folderPath
	 *            author：wangjintao version ：V1.00 Create date：2014-11-17
	 *            上午10:45:43
	 */

	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *删除指定文件夹下所有文件 param path 文件夹完整绝对路径
	 * 
	 * @param folderPath
	 *            author：wangjintao version ：V1.00 Create date：2014-11-17
	 *            上午10:45:43
	 */

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 删除两天前的文件夹和里面的文件
	 * 
	 * @param dest
	 *            author：wangjintao version ：V1.00 Create date：2014-12-2
	 *            下午04:35:28
	 */
	public void deleteDirectory(String dest) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar calendarzuotian = Calendar.getInstance();// 日历对象
		calendarzuotian.setTime(new Date());// 设置当前日期
		calendarzuotian.add(Calendar.DATE, -1);// 减1天
		String zuotian = formatter.format(calendarzuotian.getTime());
		Calendar calendarjintian = Calendar.getInstance();// 日历对象
		calendarjintian.setTime(new Date());// 设置当前日期
		String jintian = formatter.format(calendarjintian.getTime());
		File f = new File(dest);
		if (f.exists()) {
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				if (fs.length > 0) {
					for (File file : fs) {
						if (file.getName().equals(jintian)
								|| file.getName().equals(zuotian)) {
						} else {
							deleteDirectory(file.getAbsolutePath());
							file.delete();
						}
					}
				}
			}
			// f.delete();
		}
	}
	// public static void main(String args[]){
	// delFolder("E:/ewm/img/");
	// System.out.println(CommonConstant.REPORTIMAGE);
	// }

}