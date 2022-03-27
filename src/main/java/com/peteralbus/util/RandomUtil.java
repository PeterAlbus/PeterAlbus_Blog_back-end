package com.peteralbus.util;

import java.util.Random;

/**
 * The type Md 5 util.
 * @author PeterAlbus
 * Created on 2022/3/26.
 */
public class RandomUtil
{
    /**
     * Get salt string.
     *
     * @param n the n
     * @return the string
     */
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+".toCharArray();
        int length = chars.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++){
            char c = chars[new Random().nextInt(length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateVerifyCode(int n){
        Random r = new Random();
        StringBuilder stringBuffer =new StringBuilder();
        for(int i = 0;i < n;i ++){
            int ran1 = r.nextInt(10);
            stringBuffer.append(ran1);
        }
        return stringBuffer.toString();
    }

}
