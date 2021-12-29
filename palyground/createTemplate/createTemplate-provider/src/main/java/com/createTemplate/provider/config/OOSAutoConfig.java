package com.createTemplate.provider.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.createTemplate.api.util.StringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OOSAutoConfig
 * @Author libiqi
 * @Description TODO
 * @Date 2019/11/6 3:34 下午
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.oos")
@Data
public class OOSAutoConfig {
    private static String appKey;

    private static String appSecret;

    private static String endpointInternet;

    private static String commonBucket;

    private static String commonObjectKey;

    private static String commonUrl;

    /**
     * @Description:上传文件
     * @author aijian
     * @date 2016-6-29 下午03:30:55
     * @version V1.0
     */
    public static Map uploadOldSysFile(File file, String bucket, String fileFolder) throws IOException {
        Map returnMap = new HashMap();
        String filename = file.getName();
        String folder = commonObjectKey + fileFolder + "/";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpointInternet, appKey, appSecret);
        // 上传文件
        PutObjectResult result = ossClient.putObject(bucket, folder + filename, file);
        // 关闭client
        ossClient.shutdown();
        returnMap.put("fileUrl", folder + filename);
        returnMap.put("fileSize", file.length());
        returnMap.put("fileSizeString", getFileSize(file.length()));
        return returnMap;
    }

    /**
     * @Description:上传文件
     * @author aijian
     * @date 2016-6-29 下午03:30:55
     * @version V1.0
     */
    public static Map upload(MultipartFile file, String bucket) throws IOException {
        Map returnMap = new HashMap();
        String fileFix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);// 文件扩展名
        String filename = StringUtil.getUUID() + "." + fileFix;
        String folder = commonObjectKey + new SimpleDateFormat("yyyyMM").format(new Date()) + "/";
        String fileUrl = folder + filename;// 文件在OSS上的key
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpointInternet, appKey, appSecret);
        // 上传文件流
        InputStream inputStream = file.getInputStream();
        PutObjectResult result = ossClient.putObject(bucket, fileUrl, inputStream);
        // 关闭client
        ossClient.shutdown();
        returnMap.put("fileUrl", fileUrl);
        returnMap.put("fileSize", file.getSize());
        returnMap.put("fileSizeString", getFileSize(file.getSize()));
        returnMap.put("fileFix", fileFix);
        return returnMap;
    }

    /**
     * 上传二维码到OOS
     *
     * @param inputStream
     * @param bucket
     * @param fileFix
     * @return
     */
    public static Map upload(ByteArrayInputStream inputStream, String bucket, String fileFix) {
        Map returnMap = new HashMap();
        String filename = StringUtil.getUUID() + "." + fileFix;
        String folder = commonObjectKey + new SimpleDateFormat("yyyyMM").format(new Date()) + "/";
        String fileUrl = folder + filename;// 文件在OSS上的key
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpointInternet, appKey, appSecret);
        PutObjectResult result = ossClient.putObject(bucket, fileUrl, inputStream);
        // 关闭client
        ossClient.shutdown();
        returnMap.put("fileUrl", fileUrl);
        returnMap.put("fileFix", fileFix);
        return returnMap;
    }

    /**
     * @Description:上传文件
     * @author aijian
     * @date 2016-6-29 下午03:30:55
     * @version V1.0
     */
    public static Map upload(File file, String bucket) throws IOException {
        Map returnMap = new HashMap();
        String fileFix = file.getName().substring(file.getName().lastIndexOf(".") + 1);// 文件扩展名
        String filename = StringUtil.getUUID() + "." + fileFix;
        String folder = commonObjectKey + new SimpleDateFormat("yyyyMM").format(new Date()) + "/";

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpointInternet, appKey, appSecret);
        // 上传文件
        PutObjectResult result = ossClient.putObject(bucket, folder + filename, file);
        // 关闭client
        ossClient.shutdown();
        returnMap.put("fileUrl", folder + filename);
        returnMap.put("fileSize", file.length());
        returnMap.put("fileSizeString", getFileSize(file.length()));
        returnMap.put("fileFix", fileFix);
        return returnMap;
    }

    /**
     * @Description:删除文件
     * @author aijian
     * @date 2016-6-29 下午03:30:55
     * @version V1.0
     */
    public static void upload(String key, String bucket) throws IOException {

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpointInternet, appKey, appSecret);

        // 删除Object
        ossClient.deleteObject(bucket, key);
        // 关闭client
        ossClient.shutdown();
    }

    public static String getFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * @Description:生成文件地址 minutes fen
     * @author aijian
     * @date 2016-7-8 下午03:02:12
     * @version V1.0
     */
    public static String generateUrl(String key, String bucket, int minutes) {
        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + minutes * 60 * 1000);

        OSSClient ossClient = new OSSClient(endpointInternet, appKey, appSecret);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucket, key, expiration);
        return url.toString();
    }

    public static void delete(String key, String bucket) {
        // endpoint以杭州为例，其它region请按实际情况填写

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpointInternet, appKey, appSecret);
        // 删除Object
        ossClient.deleteObject(bucket, key);

        // 关闭client
        ossClient.shutdown();
    }

    public static String getCommonBucket() {
        return commonBucket;
    }
}
