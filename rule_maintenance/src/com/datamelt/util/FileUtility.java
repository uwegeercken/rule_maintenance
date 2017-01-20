/*
 *
 * all code by uwe geercken - 2014
 */
package com.datamelt.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtility
{
	private static final String BACKUPFILE_PREFIX 		= "_backup_";
	private static final String BACKUPFILE_DATEFORMAT	= "yyyyMMddHHmmss";
	
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
    
    public static void backupFile(String filename)
    {
    	final SimpleDateFormat sdf = new SimpleDateFormat(BACKUPFILE_DATEFORMAT);
    			
    	String backupFilename = filename + BACKUPFILE_PREFIX + sdf.format(new Date());
    	
    	File file = new File(filename);
    	File backupFile = new File(backupFilename);
    	
    	if(file.exists())
    	{
    		file.renameTo(backupFile);
    	}
    }
    
    public static void copyFile(String filename, String copyFilename) throws Exception
    {
    	Path source = Paths.get(filename);
		Path destination = Paths.get(copyFilename);
    	
    	Files.copy(source, destination);
    }
}
