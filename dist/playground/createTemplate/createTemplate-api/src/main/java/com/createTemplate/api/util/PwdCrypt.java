/**
 * 作者    
 * 日期    2011/12/08
 * 功能    密码加密解密函数
 * 修改历史
 * 修改日期:     	修改人:      修改目的:
 */
package com.createTemplate.api.util;

import java.io.IOException;
import java.util.UUID;
// 该用 Apache Common 包 . jdk8 有换行异常
import org.apache.commons.codec.binary.Base64;

/**
 * 功能描述：
 */

public class PwdCrypt {

    final String str = "osc@123456";

    public static PwdCrypt getInstance() {
        return new PwdCrypt();
    }

    /**
     * 加密操作 入参 data 明文
     * 
     * @return 密文
     */
    public String encrypt(String data) {
        return Base64.encodeBase64String(simplecrypt(data).getBytes());
    }

    /**
     * 解密操作 入参 data密文
     * 
     * @return 明文
     * @throws IOException
     */
    public String decrypt(String data) {
        return simplecrypt(new String(Base64.decodeBase64(data)));
    }

    /**
     * 
     * 功能描述：进行常量异或
     * 
     * @author lianglp
     * @param
     * @return String @throws原文：
     * @version 1.0
     * @date Mar 9, 200911:44:02 AM
     */
    public String simplecrypt(String data) {
        char[] a = data.toCharArray();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < str.length(); j++) {
                char c = str.charAt(j);
                a[i] = (char) (a[i] ^ c);
            }
        }
        String s = new String(a);
        return s;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(UUID.randomUUID().toString());
        String old = "123456";
        String miwen = PwdCrypt.getInstance().encrypt("123456");
        System.out.println("原文：" + old);
        System.out.println("加密后：" + miwen);
        System.out.println("解密后：" + PwdCrypt.getInstance().decrypt(miwen));
    }
}
