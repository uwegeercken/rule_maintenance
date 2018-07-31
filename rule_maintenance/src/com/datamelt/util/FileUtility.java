/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
        else if(value.endsWith("\\"))
        {
            value = value.substring(0,value.length()-2);
        }
        return value;
    }
    
    public static void backupFile(String fileFolder, String filename, String backupFolder)
    {
    	final SimpleDateFormat sdf = new SimpleDateFormat(BACKUPFILE_DATEFORMAT);
    	
    	if(backupFolder==null || backupFolder.trim().equals(""))
    	{
    		backupFolder = fileFolder;
    	}
    	
    	String backupFilename = filename + BACKUPFILE_PREFIX + sdf.format(new Date());
    	String fullBackupFilename = addTrailingSlash(backupFolder) + backupFilename;
    	
    	
    	File file = new File(addTrailingSlash(fileFolder) + filename);
    	File backupFile = new File(fullBackupFilename);
    	
    	String fullBackupFilenamePath = backupFile.getAbsolutePath().substring(0,backupFile.getAbsolutePath().lastIndexOf(File.separator));
    	File destinationPath = new File(fullBackupFilenamePath);
    	destinationPath.mkdirs();
    	
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
