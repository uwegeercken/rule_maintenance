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

public class RuleGroupFileCreator {

	public String projectName=null;
	public String templatePath=null;
	public String templateName=null;
	public String outputPath=null;
	public String selectedDate=null;
	public String dbServerHostname=null;
	public int dbServerPort;
	public String dbName=null;
	public String dbUser=null;
	public String dbPassword=null;

	private static final String FILE_EXTENSION = ".xml";
	private static final String ZIP_EXTENSION = ".zip";
	
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
				System.out.println("retrieving the list of project");
				projects = DbCollections.getAllProjects(connection);
			}
			
			for(int f=0;f<projects.size();f++)
			{
				Project project = projects.get(f);
				System.out.println("processing project: " + project.getName());
				System.out.println("loading valid rule groups for date: " + fileCreator.selectedDate);
				project.loadRuleGroups(fileCreator.selectedDate);
				System.out.println("found rule groups: " + project.getRulegroups().size());
				
				if(project.getRulegroups()!=null && project.getRulegroups().size()>0)
				{
					System.out.println("writing files");
					fileCreator.writeFiles(project);
					
					System.out.println("creating zip file: " + fileCreator.getTempfolder(project) + "/" + project.getName().toLowerCase() + "_" + fileCreator.selectedDate + RuleGroupFileCreator.ZIP_EXTENSION);
					fileCreator.zipFiles(project);
					
					System.out.println("deleting temp folder and files: " + fileCreator.getTempfolder(project));
				}
			}
			System.out.println("end of program.");
        }		
	}

	private String getTempfolder(Project project)
	{
		return outputPath + "/" + project.getName() + "_" + selectedDate;
	}
	
	public void writeFiles(Project project) throws Exception
	{
		File folder = new File(getTempfolder(project));
		folder.mkdirs();
		for (int i=0;i<project.getRulegroups().size();i++)
		{
			RuleGroup rulegroup = project.getRulegroups().get(i);
			rulegroup.loadRuleGroupActions();
			rulegroup.loadDependentRuleGroup();
			String fileName=getTempfolder(project) + "/" + rulegroup.getName() + RuleGroupFileCreator.FILE_EXTENSION;
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file);
			fw.write(project.mergeWithTemplate(rulegroup, templatePath, templateName));
			fw.close();
		}
	}
	
	public String zipFiles(Project project) throws Exception
	{
		String zipfilename=outputPath +"/" + project.getName().toLowerCase() + "_" + selectedDate + RuleGroupFileCreator.ZIP_EXTENSION;

		byte[] buffer = new byte[1024];
		File folder = new File(getTempfolder(project));
		
		File toDelete = new File(zipfilename);
		toDelete.delete();
		
		FileOutputStream fos = new FileOutputStream(zipfilename);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) 
		{
			FileInputStream fis = new FileInputStream(files[i]);
			zos.putNextEntry(new ZipEntry(files[i].getName()));
			int length;
			while ((length = fis.read(buffer)) > 0) 
			{	
				zos.write(buffer, 0, length);
			}
			zos.closeEntry();
			fis.close();
		}
		zos.close();
		fos.close();
		
		File[] filesToDelete = folder.listFiles();
		for(File file : filesToDelete)
		{
			file.delete();
		}
		folder.delete();
		return zipfilename;
	}
	
	public static void help()
    {
    	System.out.println("RuleGroupFileCreator. program to generate rules files in xml format for the JaRe - Java Ruleengine - from a MySql database.");
    	System.out.println("At the end of the process the files are be put/compressed into a zip file.");
    	System.out.println("Only Rule Groups which are valid for the given date are output.");
    	System.out.println();
    	System.out.println();
    	System.out.println("RuleGroupFileCreator -n=[project name] -p=[template folder] -t=[template name] -o=[output folder]-v=[validity date] -s=[db server hostname] -r=[db server port] -b=[db name] -u=[db user] -w=[db password]");
    	System.out.println("where [project name]     : optional. name of the project for which files shall be generated.");
    	System.out.println("      [template folder]  : required. path to the folder containing the template file.");
    	System.out.println("      [template name]    : required. filename of the template.");
    	System.out.println("      [output folder]    : required. path to the output folder where files are created.");
    	System.out.println("      [validity date]    : required. validity date for the rule groups.");
    	System.out.println("      [db server hostname: required. hostname or IP adress of the database server");
    	System.out.println("      [db server port]   : required. port the database server is listening");
    	System.out.println("      [db name]          : required. name of the database");
    	System.out.println("      [db user]          : required. user to access the database");
    	System.out.println("      [db password]      : required. user password to access the database");
    	System.out.println();
    	System.out.println("example: RuleGroupFileCreator -n=\"Project 1\" -p=/home/user/templates -t=template1.vm -o=/home/user/testoutput -v=2014-02-28 -s=localhost -r=3306 -b=ruleengine_rules -u=tom -p=mysecret");
    	System.out.println();
    	System.out.println("published as open source under the GPL3.");
    	System.out.println("all code by uwe geercken, 2014-2016. uwe.geercken@web.de");
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
}
