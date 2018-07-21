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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.datamelt.db.DbCollections;
import com.datamelt.db.MySqlConnection;
import com.datamelt.db.Project;
import com.datamelt.db.RuleGroup;

/**
* RuleGroupFileCreator. Program to generate a zip file for the JaRe - Java Ruleengine - from the database of the Business Rules Maintenance Tool.
* An XML File is generated for each rulegroup and written to the temporary folder. Then the files are put/compressed into a single zip file.
* If no project name is specified, then zip files for all projects will be created.
* Only Rule Groups which are valid for the given date are considered for the output.
*
*
* all code by uwe geercken - 2014-2018
*/

public class RuleGroupFileCreator {

	private String projectName=null;
	private String templatePath=null;
	private String templateName=null;
	private String outputPath=null;
	private String backupPath=null;
	private String temporaryPath=null;
	private String environment="";
	private String selectedDate=null;
	private String dbServerHostname=null;
	private int dbServerPort;
	private String dbName=null;
	private String dbUser=null;
	private String dbPassword=null;

	private static final String FILE_EXTENSION 			= ".xml";
	
	public static final String ZIP_EXTENSION 			= ".zip";
	
	public static final String ENVIRONMENT_DEVELOPMENT 	= "dev";
	public static final String ENVIRONMENT_PRODUCTION  	= "prod";
	
	public RuleGroupFileCreator()
	{
		
	}
	
	public static void main(String[] args) throws Exception
	{
		RuleGroupFileCreator fileCreator = new RuleGroupFileCreator();
		
		if (args.length<6)
        {
        	help();
        }
        else
        {
			for(int i=0;i<args.length;i++)
	    	{
	    		if (args[i].startsWith("-n="))
	    		{
	    			fileCreator.projectName = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-p="))
	    		{
	    			fileCreator.templatePath = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-t="))
	    		{
	    			fileCreator.templateName = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-o="))
	    		{
	    			fileCreator.outputPath = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-y="))
	    		{
	    			fileCreator.temporaryPath = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-v="))
	    		{
	    			fileCreator.selectedDate = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-s="))
	    		{
	    			fileCreator.dbServerHostname = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-r="))
	    		{
	    			fileCreator.dbServerPort = Integer.parseInt(args[i].substring(3));
	    		}
	    		else if (args[i].startsWith("-u="))
	    		{
	    			fileCreator.dbUser = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-b="))
	    		{
	    			fileCreator.dbName = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-w="))
	    		{
	    			fileCreator.dbPassword = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-e="))
	    		{
	    			fileCreator.environment = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-l="))
	    		{
	    			fileCreator.backupPath = args[i].substring(3);
	    		}
	    	}
			
			if(fileCreator.templatePath==null)
	    	{
	    		throw new Exception("parameter -p: path to the template folder must be specified");
	    	}
			if(fileCreator.templateName==null)
	    	{
	    		throw new Exception("parameter -t: template filename must be specified");
	    	}
			if(fileCreator.outputPath==null)
	    	{
	    		throw new Exception("parameter -o: path to the output folder must be specified");
	    	}
			if(fileCreator.temporaryPath==null)
	    	{
	    		throw new Exception("parameter -y: path to the temporary folder must be specified");
	    	}
			if(fileCreator.selectedDate==null)
	    	{
	    		throw new Exception("parameter -v: validity date for the rule group must be specified");
	    	}
			if(fileCreator.dbServerHostname==null)
	    	{
	    		throw new Exception("parameter -s: database server hostname must be specified");
	    	}
			if(fileCreator.dbServerPort==0)
	    	{
	    		throw new Exception("parameter -r: database server port must be specified (integer value)");
	    	}
			if(fileCreator.dbName==null)
	    	{
	    		throw new Exception("parameter -b: database name must be specified");
	    	}
			if(fileCreator.dbUser==null)
	    	{
	    		throw new Exception("parameter -u: database user must be specified");
	    	}
			if(fileCreator.dbPassword==null)
	    	{
	    		throw new Exception("parameter -w: database user password must be specified");
	    	}
			
			System.out.println("start of program...");
			System.out.println("getting connection to database");
			MySqlConnection connection = new MySqlConnection(fileCreator.dbServerHostname,fileCreator.dbServerPort,fileCreator.dbName,fileCreator.dbUser,fileCreator.dbPassword);
			
			ArrayList<Project> projects = new ArrayList<Project>();
			if(fileCreator.projectName!=null)
			{
				System.out.println("getting project:" + fileCreator.projectName);
				Project project = new Project();
				project.setConnection(connection);
				project.setName(fileCreator.projectName);
				project.loadByName();
				projects.add(project);
			}
			else
			{
				System.out.println("retrieving the list of projects");
				projects = DbCollections.getAllProjects(connection);
			}
			
			for(int f=0;f<projects.size();f++)
			{
				Project project = projects.get(f);
				System.out.println("processing project: " + project.getName());
				System.out.println("loading valid rule groups for date: " + fileCreator.selectedDate);
				project.loadRuleGroups(fileCreator.selectedDate);
					
				if(fileCreator.backupPath!=null && !fileCreator.backupPath.equals(""))
				{
					System.out.println("backing up project zip file to: " + fileCreator.backupPath);
				}
				else
				{
					System.out.println("backing up project zip file to: " + fileCreator.outputPath);
				}
				FileUtility.backupFile(fileCreator.outputPath, fileCreator.getZipFileName(project),fileCreator.backupPath);				
				
				System.out.println("found rule groups: " + project.getRulegroups().size());
				if(project.getRulegroups()!=null && project.getRulegroups().size()>0)
				{
					System.out.println("writing files");
					String tempPath = fileCreator.writeFiles(project);
					
					System.out.println("creating zip file: " + fileCreator.getZipFileName(project));
					fileCreator.zipFiles(project,tempPath);
				}
			}
			System.out.println("end of program.");
        }		
	}

	public String writeFiles(Project project) throws Exception
	{
		String tempPath = FileUtility.addTrailingSlash(getTemporaryPath()) + project.getName();
		File folder = new File(tempPath);
		folder.mkdirs();
		
		for (int i=0;i<project.getRulegroups().size();i++)
		{
			RuleGroup rulegroup = project.getRulegroups().get(i);
			rulegroup.loadRuleGroupActions();
			rulegroup.loadDependentRuleGroup();
			String fileName=FileUtility.addTrailingSlash(tempPath) + rulegroup.getName() + RuleGroupFileCreator.FILE_EXTENSION;
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file);
			fw.write(project.mergeWithTemplate(rulegroup, templatePath, templateName));
			fw.close();
		}
		return tempPath;
	}
	
	public String getZipFileName(Project project)
	{
		String filename = project.getExportFilename();
		if(filename==null || filename.trim().equals(""))
		{
			filename = project.getName().toLowerCase().trim();
		}
		if(environment!=null && !environment.trim().equals(""))
		{
			filename = filename + "_" + environment;
		}
		filename = filename + RuleGroupFileCreator.ZIP_EXTENSION;
		return filename;
	}
	
	public String zipFiles(Project project, String temporaryFolder) throws Exception
	{
		String zipFileName = getZipFileName(project);
		String fullZipFileName = null;
		if(outputPath!=null && !outputPath.trim().equals(""))
		{
			fullZipFileName = FileUtility.addTrailingSlash(outputPath) + zipFileName;
		}
		else
		{
			fullZipFileName = FileUtility.addTrailingSlash(temporaryPath) + zipFileName;
		}
		
		File zipFile = new File(fullZipFileName);
		String zipFileAbsolutePath = zipFile.getAbsolutePath().substring(0,zipFile.getAbsolutePath().lastIndexOf(File.separator));
		
		File zipFilePath = new File(zipFileAbsolutePath);
		zipFilePath.mkdirs();
		
		byte[] buffer = new byte[1024];
		
		File toDelete = new File(fullZipFileName);
		toDelete.delete();
		
		File tempFolder = new File(temporaryFolder);
		
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		File[] files = tempFolder.listFiles();
		for (int i = 0; i < files.length; i++) 
		{
			FileInputStream fis = new FileInputStream(files[i]);
			if(files[i].isFile()&& files[i].getName().endsWith(".xml"))
			{
				zos.putNextEntry(new ZipEntry(files[i].getName()));
				int length;
				while ((length = fis.read(buffer)) > 0) 
				{	
					zos.write(buffer, 0, length);
				}
				zos.closeEntry();
			}
			fis.close();
		}
		zos.close();
		fos.close();
		
		File[] filesToDelete = tempFolder.listFiles();
		for(File file : filesToDelete)
		{
			file.delete();
		}
		tempFolder.delete();
		return fullZipFileName;
	}
	
	public static void help()
    {
    	System.out.println("RuleGroupFileCreator. Program to generate a zip file for the JaRe - Java Ruleengine - from the database of the Business Rules Maintenance Tool.");
    	System.out.println("An XML File is generated for each rulegroup and written to the temporary folder. Then the files are put/compressed into a single zip file.");
    	System.out.println("If no project name is specified, then zip files for all projects will be created.");
    	System.out.println("Only Rule Groups which are valid for the given date are considered for the output.");
    	System.out.println();
    	System.out.println();
    	System.out.println("RuleGroupFileCreator -n=[project name] -p=[template folder] -t=[template name] -o=[output folder] -y=[temporary folder] -v=[validity date] -s=[db server hostname] -r=[db server port] -b=[db name] -u=[db user] -w=[db password] -e=[environment] -l=[backup folder]");
    	System.out.println("where [project name]     : optional. name of the project for which files shall be generated. If no name is specified then zip files for all projects are generated.");
    	System.out.println("      [environment]      : optional. the environment the file is targeted for");
    	System.out.println("      [backup folder]    : optional. path to folder where copies of the project zip files are created.");
    	System.out.println("      [template folder]  : required. path to the folder containing the template file.");
    	System.out.println("      [template name]    : required. filename of the template.");
    	System.out.println("      [output folder]    : required. path to the output folder where the zip file is created.");
    	System.out.println("      [temporary folder] : required. path to the temporary folder where rulegroup files are temporarily created.");
    	System.out.println("      [validity date]    : required. validity date - determines which rule groups to be included.");
    	System.out.println("      [db server hostname: required. hostname or IP adress of the database server");
    	System.out.println("      [db server port]   : required. port on which the database server is listening");
    	System.out.println("      [db name]          : required. name of the database");
    	System.out.println("      [db user]          : required. user with read access the database");
    	System.out.println("      [db password]      : required. user password to access the database");
    	System.out.println();
    	System.out.println("example: RuleGroupFileCreator -n=\"Project 1\" -p=/home/user/templates -t=template1.vm -o=/home/user/testoutput -y=/home/user/temp -v=2017-01-15 -s=localhost -r=3306 -b=ruleengine_rules -u=tom -p=mysecret -e=dev -l=/home/user/testoutput/backup");
    	System.out.println();
    	System.out.println("published as open source under the GPL3.");
    	System.out.println("all code by uwe geercken, 2014-2017. uwe.geercken@web.de");
    	System.out.println();
    }

	public String getProjectName() 
	{
		return projectName;
	}

	public void setProjectName(String projectName) 
	{
		this.projectName = projectName;
	}

	public String getTemplatePath() 
	{
		return templatePath;
	}

	public void setTemplatePath(String templatePath) 
	{
		this.templatePath = templatePath;
	}

	public String getTemplateName() 
	{
		return templateName;
	}

	public void setTemplateName(String templateName) 
	{
		this.templateName = templateName;
	}

	public String getOutputPath() 
	{
		return outputPath;
	}

	public void setOutputPath(String outputPath) 
	{
		this.outputPath = outputPath;
	}

	public String getSelectedDate() 
	{
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) 
	{
		this.selectedDate = selectedDate;
	}

	public String getDbUser() 
	{
		return dbUser;
	}

	public void setDbUser(String dbUser) 
	{
		this.dbUser = dbUser;
	}

	public void setDbPassword(String dbPassword) 
	{
		this.dbPassword = dbPassword;
	}

	public String getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(String environment)
	{
		this.environment = environment;
	}

	public String getDbServerHostname()
	{
		return dbServerHostname;
	}

	public int getDbServerPort()
	{
		return dbServerPort;
	}

	public String getDbName()
	{
		return dbName;
	}

	public String getBackupPath()
	{
		return backupPath;
	}

	public void setBackupPath(String backupPath)
	{
		this.backupPath = backupPath;
	}

	public String getTemporaryPath()
	{
		return temporaryPath;
	}

	public void setTemporaryPath(String temporaryPath)
	{
		this.temporaryPath = temporaryPath;
	}
}
