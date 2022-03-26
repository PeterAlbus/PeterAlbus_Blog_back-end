package com.peteralbus.util;


import java.util.HashSet;
import java.util.Set;

/**
 * The type Type util.
 * @author PeterAlbus
 * Created on 2022/3/26.
 */
public class TypeUtil
{
    static public boolean isImg(String type)
    {
        final Set<String> allowTypes = new HashSet<String>(){{
            add(".jpg");
            add(".jpeg");
            add(".png");
            add(".JPG");
            add(".JPEG");
            add(".PNG");
            add(".webp");
            add(".tif");
            add(".WEBP");
            add(".TIF");
        }};
        return allowTypes.contains(type);
    }
}
