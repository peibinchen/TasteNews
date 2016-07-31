package com.example.asus.tastenews.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 从Github上找到的MD5Encoder的类
 */
public class MD5Encoder {
    public static String encode(String password){
        try {

            //获取到数字消息的摘要器

            MessageDigest digest = MessageDigest.getInstance("MD5");

            //执行加密操作

            byte[] result = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            //将每个byte字节的数据转换成16进制的数据

            for(int i= 0 ;i<result.length;i++){

                int number = result[i]&0xff;//加盐
                String str = Integer.toHexString(number);//将十进制的number转换成十六进制数据
                if(str.length()==1){//判断加密后的字符的长度，如果长度为1，则在该字符前面补0

                    sb.append("0");
                    sb.append(str);

                }else{

                    sb.append(str);

                }

            }

            return sb.toString();//将加密后的字符转成字符串返回

        } catch (NoSuchAlgorithmException e) {//加密器没有被找到，该异常不可能发生。因为我们填入的“MD5”是正确的

            e.printStackTrace();
            //CNA'T REACH;

            return "";
        }
    }
}
