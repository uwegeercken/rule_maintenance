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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
* The templates "ruleengine.vm" and "referencefields.vm" have to be located in the specified template folder. These two files are part of the
* Business Rules Maintenance Tool - the web application - and can be found in the "templates" folder.
*
* all code by uwe geercken - 2014-2018
*/

public class RuleGroupFileCreator {

	private String projectName=null;
	private String templatePath=null;
	private String outputPath=null;
	private String backupPath=null;
	private String temporaryPath=null;
	private String environment="dev";
	private String selectedDate=null;
	private String dbServerHostname=null;
	private int    dbServerPort;
	private String dbName=null;
	private String dbUser=null;
	private String dbPassword=null;

	private static SimpleDateFormat sdf 						= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String FILE_EXTENSION 					= ".xml";
	
	public static final String ZIP_EXTENSION 					= ".zip";
	
	public static final String ENVIRONMENT_DEVELOPMENT 			= "dev";
	public static final String ENVIRONMENT_QUALITY_ASSURANCE 	= "qa";
	public static final String ENVIRONMENT_PRODUCTION  			= "prod";

	public static final String TEMPLATE_RULEENGINE				= "ruleengine.vm";
	public static final String TEMPLATE_REFERENCEFIELDS			= "referencefields.vm";
	
	private static final String LEVEL_INFO						= "INFO";
	private static final String LEVEL_WARNING					= "WARNING";
	private static final String LEVEL_ERROR						= "ERROR";
	
	public RuleGroupFileCreator()
	{
		
	}
	
	public static void main(String[] args) throws Exception
	{
		RuleGroupFileCreator fileCreator = new RuleGroupFileCreator();
		
		if (args.length<9)
        {
        	help();
        }
        else
        {
			for(int i=0;i<args.length;i++)
	    	{
	    		// project name is optional
				if (args[i].startsWith("-n="))
	    		{
	    			fileCreator.projectName = args[i].substring(3);
	    		}
	    		else if (args[i].startsWith("-p="))
	    		{
	    			fileCreator.templatePath = args[i].substring(3);
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
				// environment is optional
	    		else if (args[i].startsWith("-e="))
	    		{
	    			fileCreator.environment = args[i].substring(3);
	    		}
				// backup folder is optional
	    		else if (args[i].startsWith("-l="))
	    		{
	    			fileCreator.backupPath = args[i].substring(3);
	    		}
	    	}
			
			if(fileCreator.templatePath==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -p: path to the template folder must be specified"));
	    	}
			if(fileCreator.outputPath==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -o: path to the output folder must be specified"));
	    	}
			if(fileCreator.temporaryPath==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -y: path to the temporary folder must be specified"));
	    	}
			if(fileCreator.selectedDate==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -v: validity date for the rule group must be specified"));
	    	}
			if(fileCreator.dbServerHostname==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -s: database server hostname must be specified"));
	    	}
			if(fileCreator.dbServerPort==0)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -r: database server port must be specified (integer value)"));
	    	}
			if(fileCreator.dbName==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -b: database name must be specified"));
	    	}
			if(fileCreator.dbUser==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -u: database user must be specified"));
	    	}
			if(fileCreator.dbPassword==null)
	    	{
	    		throw new Exception(getSystemMessage(LEVEL_ERROR,"parameter -w: database user password must be specified"));
	    	}
			
			System.out.println(getSystemMessage(LEVEL_INFO,"start of program..."));
			System.out.println(getSystemMessage(LEVEL_INFO,"getting connection to database: " +fileCreator.dbName));
			MySqlConnection connection = new MySqlConnection(fileCreator.dbServerHostname,fileCreator.dbServerPort,fileCreator.dbName,fileCreator.dbUser,fileCreator.dbPassword);
			
			ArrayList<Project> projects = new ArrayList<Project>();
			if(fileCreator.projectName!=null)
			{
				System.out.println(getSystemMessage(LEVEL_INFO,"getting project: " + fileCreator.projectName));
				Project project = new Project();
				project.setConnection(connection);
				project.setName(fileCreator.projectName);
				project.loadByName();
				if(project.getId()>0)
				{
					projects.add(project);
				}
				else
				{
					System.out.println(getSystemMessage(LEVEL_ERROR,"project not found: " + fileCreator.projectName));
				}
			}
			else
			{
				System.out.println(getSystemMessage(LEVEL_INFO,"retrieving the list of projects"));
				projects = DbCollections.getAllProjects(connection);
			}
			
			if(projects.size()>0)
			{
				for(int f=0;f<projects.size();f++)
				{
					Project project = projects.get(f);
					System.out.println(getSystemMessage(LEVEL_INFO,"processing project: " + project.getName()));
					System.out.println(getSystemMessage(LEVEL_INFO,"loading valid rule groups for date: " + fileCreator.selectedDate));
					project.loadRuleGroups(fileCreator.selectedDate);
					project.loadFields();
						
					if(fileCreator.backupPath!=null && !fileCreator.backupPath.equals(""))
					{
						System.out.println(getSystemMessage(LEVEL_INFO,"backing up project zip file to: " + fileCreator.backupPath));
					}
					else
					{
						System.out.println(getSystemMessage(LEVEL_INFO,"backing up project zip file to: " + fileCreator.outputPath));
					}
					FileUtility.backupFile(fileCreator.outputPath, fileCreator.getZipFileName(project),fileCreator.backupPath);				
					
					System.out.println(getSystemMessage(LEVEL_INFO,"writing file for reference fields. number of fields: " + project.getFields().size()));
					ReferenceFieldsFileCreator referenceFieldFileCreator = new ReferenceFieldsFileCreator(project,fileCreator.temporaryPath,fileCreator.templatePath, TEMPLATE_REFERENCEFIELDS);
					String referenceFieldsFileName = referenceFieldFileCreator.writeFile();
					
					System.out.println(getSystemMessage(LEVEL_INFO,"found rule groups: " + project.getRulegroups().size()));
					if(project.getRulegroups()!=null && project.getRulegroups().size()>0)
					{
						System.out.println(getSystemMessage(LEVEL_INFO,"writing rule group files: " + project.getRulegroups().size()));
						String tempPath = fileCreator.writeFiles(project);
						
						System.out.println(getSystemMessage(LEVEL_INFO,"creating zip file: " + fileCreator.getZipFileName(project)));
						fileCreator.zipFiles(project,tempPath);
					}

					// delete the referencefields file for the project
					referenceFieldFileCreator.deleteFile(referenceFieldsFileName);
				}
			}
			else
			{
				System.out.println(getSystemMessage(LEVEL_WARNING,"no projects to process"));
			}
			System.out.println(getSystemMessage(LEVEL_INFO,"end of program."));
        }		
	}

	private static String getExecutionDateTime()
	{
		return sdf.format(new Date());
	}

	private static String getSystemMessage(String type, String text)
	{
		return "[" + getExecutionDateTime() + "] " + type + " " + text;
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
			fw.write(project.mergeWithTemplate(rulegroup, templatePath, TEMPLATE_RULEENGINE));
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
    	System.out.println("An XML File is generated for each rulegroup and written to the temporary folder. Then the files are put/compressed into a single zip file, ");
    	System.out.println("together with the file which contains the reference fields definitions,");
    	System.out.println();
    	System.out.println("If no project name is specified, then zip files for all projects will be created.");
    	System.out.println();
    	System.out.println("Only Rule Groups which are valid for the given date are considered for the output.");
    	System.out.println();
    	System.out.println("The templates ruleengine.vm and referencefields.vm have to be located in the specified template folder. These two files are part of the");
    	System.out.println("Business Rules Maintenance Tool - the web application - and can be found in the \"templates\" folder.");
    	System.out.println();
    	System.out.println();
    	System.out.println("RuleGroupFileCreator -n=[project name] -p=[template folder] -t=[template name] -o=[output folder] -y=[temporary folder] -v=[validity date] -s=[db server hostname] -r=[db server port] -b=[db name] -u=[db user] -w=[db password] -e=[environment] -l=[backup folder]");
    	System.out.println("where [project name]     : optional. name of the project for which files shall be generated. If no name is specified then zip files for all projects are generated.");
    	System.out.println("      [environment]      : optional. the environment the file is targeted for. default is dev.");
    	System.out.println("      [backup folder]    : optional. path to folder where copies of the project zip files are created. default is the output folder");
    	System.out.println("      [template folder]  : required. path to the folder containing the template file.");
    	System.out.println("      [output folder]    : required. path to the output folder where the zip file is created.");
    	System.out.println("      [temporary folder] : required. path to the temporary folder where rulegroup files are temporarily created.");
    	System.out.println("      [validity date]    : required. validity date - determines which rule groups to be included.");
    	System.out.println("      [db server hostname: required. hostname or IP adress of the database server");
    	System.out.println("      [db server port]   : required. port on which the database server is listening");
    	System.out.println("      [db name]          : required. name of the database");
    	System.out.println("      [db user]          : required. user with read access the database");
    	System.out.println("      [db password]      : required. user password to access the database");
    	System.out.println();
    	System.out.println("example: RuleGroupFileCreator -n=\"Project 1\" -p=/home/user/templates -o=/home/user/testoutput -y=/home/user/temp -v=2018-07-01 -s=localhost -r=3306 -b=ruleengine_rules -u=tom -w=mysecret -e=dev -l=/home/user/testoutput/backup");
    	System.out.println();
    	System.out.println("published as open source under the Apache License, Version 2.0.");
    	System.out.println("all code by uwe geercken, 2014-2018. uwe.geercken@web.de");
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
