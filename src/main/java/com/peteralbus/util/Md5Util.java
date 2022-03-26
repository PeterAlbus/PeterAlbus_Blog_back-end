package com.peteralbus.util;

import java.util.Random;

/**
 * The type Md 5 util.
 * @author PeterAlbus
 * Created on 2022/3/26.
 */
public class Md5Util
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
}
