/*
 *
 * all code by uwe geercken - 2014
 */
package com.datamelt.util;

public class FileUtility
{
    public static String addTrailingSlash(String value)
    {
        if(!value.endsWith("/")&& !value.endsWith("\\"))
        {
            value = value + "/";
        }
        return value;
    }
    
    public static String removeTrailingSlash(String value)
    {
        if(value.endsWith("/"))
        {
            value = value.substring(0,value.length()-1);
        }
        if(value.endsWith("\\"))
        {
            value = value.substring(0,value.length()-2);
        }
        return value;
    }
}
