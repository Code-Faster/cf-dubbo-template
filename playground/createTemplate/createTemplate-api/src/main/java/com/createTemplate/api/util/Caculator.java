package com.createTemplate.api.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;  
  
public class Caculator {  
    int totalFolder = 0;  
    int totalFile = 0;  
    public static void main(String args[]) {  
        String folder = "D:/apache-tomcat-7.0.52/webapps/reportTask/ewm/pdf/20141119";  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        System.out.println("统计日期："+sdf.format(new Date()));  
        try {  
            Caculator size = new Caculator();  
            long fileSizeByte = size.getFileSize(new File(folder));  
            DecimalFormat df=(DecimalFormat)DecimalFormat.getInstance();  
            System.out.println("总文件夹数: "+ df.format(size.getTotalFolder()));  
            System.out.println("总文件数: " + df.format(size.getTotalFile()));  
            df.setGroupingSize(3);  
            System.out.println("文件夹大小（单位字节）: " + df.format(fileSizeByte) + " Bytes");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }   
    public String getFileSize2(String folder) {  
    	 //folder = "D:/apache-tomcat-7.0.52/bin";  
             SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
             Caculator size = new Caculator();  
             long fileSizeByte = size.getFileSize(new File(folder));  
             DecimalFormat df=(DecimalFormat)DecimalFormat.getInstance();  
             df.setGroupingSize(3);  
        return df.format(size.getTotalFile()).toString();  
    }  
    public long getFileSize(File folder) {  
        long foldersize = 0;  
        File[] filelist = folder.listFiles();  
        for (int i = 0; i < filelist.length; i++) {  
            if (filelist[i].isDirectory()) {  
                totalFolder++;  
                foldersize += getFileSize(filelist[i]);  
            } else {  
                totalFile++;  
                foldersize += filelist[i].length();  
            }  
        }  
        return foldersize;  
    }  
  
    public int getTotalFolder() {  
        return totalFolder;  
    }  
  
    public int getTotalFile() {  
        return totalFile;  
    }  
  
}  