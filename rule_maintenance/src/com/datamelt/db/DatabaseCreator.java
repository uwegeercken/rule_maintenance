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
package com.datamelt.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.datamelt.rules.core.action.GenericAction;
import com.datamelt.rules.implementation.GenericCheck;
import com.datamelt.util.CheckAnnotation;
import com.datamelt.util.CheckMethodAnnotation;
import com.datamelt.util.ActionAnnotation;  
import com.datamelt.util.ActionMethodAnnotation;

import com.datamelt.util.ClassUtility;
import com.datamelt.web.Controller;

/**
 * Used to create the database tables and fields required by this web application.
 * 
 * When the web application is started the first time or when the database configuration
 * was changed, this class is used.
 * 
 * @author uwe geercken
 *
 */
public class DatabaseCreator
{
	/**
	 * check if the database exists
	 */
    public static boolean checkExistDatabase(MySqlConnection connection, String databaseName) throws Exception
    {
    	return DbCollections.getCheckDatabaseExists(connection,databaseName);
    }
    
	/**
	 * create the database on the server - if it does not exist
	 */
    public static void createDatabase(MySqlConnection connection, String databaseName) throws Exception
    {
        Statement statementDatabase = connection.getStatement();
        statementDatabase.execute("create database if not exists " + databaseName);
        
        File jareJarFile = getJareJarFile();
        
        createDatabaseTables(connection, databaseName);
        createDatabaseTablesData(connection, databaseName);
        
        if(jareJarFile!=null && jareJarFile.exists() && jareJarFile.canRead())
		{
			String jareJarFilename = jareJarFile.getName();
			
			createOrUpdateDatabaseTablesChecks(connection, jareJarFile, databaseName);
			createOrUpdateDatabaseTablesActions(connection,jareJarFile, databaseName);
			
			JareVersion jareVersion = new JareVersion();
			jareVersion.setConnection(connection);
			jareVersion.setVersion(jareJarFilename);

			PreparedStatement statement = connection.getPreparedStatement(JareVersion.INSERT_SQL);
			jareVersion.insert(statement);
		}
    }
    
	/**
	 * create the database tables on the server - if they do not exist.
	 * 
	 */
    public static void createDatabaseTables(MySqlConnection connection, String databaseName) throws Exception
    {
        Statement statement = connection.getStatement();

        // use the given database
    	statement.execute("use " + databaseName);
        
    	// create all required tables

    	try
        {
        	statement.execute(CreateDatabase.CREATE_TABLE_JARE_VERSION);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_JARE_VERSION);
        }
    	try
        {
        	statement.execute(CreateDatabase.CREATE_TABLE_ACTION_METHOD_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_ACTION_METHOD);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_ACTION_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_ACTION);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_ACTIVITY_LOG_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_ACTIVITY_LOG);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_CHECK_METHOD_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_CHECK_METHOD);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_CHECK_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_CHECK);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_GROUPS_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_GROUPS);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_GROUPUSER_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_GROUPUSER);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_HISTORY_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_HISTORY);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_PROJECT_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_PROJECT);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_REFERENCE_FIELDS_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_REFERENCE_FIELDS);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_RULE_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_RULE);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_RULEGROUP_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_RULEGROUP);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_RULEGROUPACTION_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_RULEGROUPACTION);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_RULESUBGROUP_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_RULESUBGROUP);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_TYPES_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_TYPES);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_USER_SQL);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_USER);
        }
    	try
    	{
    		statement.execute(CreateDatabase.CREATE_TABLE_RULEGROUP_TESTDATA);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating table: " + CreateDatabase.TABLE_RULEGROUP_TESTDATA);
        }

    	finally
    	{
    		statement.close();
    	}
        
    }
    
	/**
	 * create the database tables base data on the server.
	 * 
	 */
    public static void createDatabaseTablesData(MySqlConnection connection, String databaseName) throws Exception
    {
    	 Statement statement = connection.getStatement();

         // use the given database
     	statement.execute("use " + databaseName);
     	
    	// create all required base data
        try
        {
        	statement.execute(CreateDatabase.CREATE_GROUPS);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_GROUPS);
        }
        try
        {
        	statement.execute(CreateDatabase.CREATE_USERS);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_USER);
        }
        try
        {
        	statement.execute(CreateDatabase.CREATE_GROUPUSERS);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_GROUPUSER);
        }
        try
        {
        	statement.execute(CreateDatabase.CREATE_TYPES);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_TYPES);
        }
        
        finally
        {
        	statement.close();
        }
    }
    
    /**
	 * create or update the ruleengine checks and check methods.
	 * 
	 */
    public static void createOrUpdateDatabaseTablesChecks(MySqlConnection connection, File jareFile, String databaseName) throws Exception
    {
    	 ArrayList<Class> checks = getClasses(jareFile, Constants.PACKAGE_RULEENGINE_CHECKS);
         
         try
         {
         	for(int i=0;i<checks.size();i++)
         	{
         		createOrUpdateCheck(connection, checks.get(i));
         	}
         }
         catch(Exception ex)
         {
         	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_CHECK);
         }
         try
         {
         	for(int i=0;i<checks.size();i++)
         	{
         		createOrUpdateCheckMethod(connection, checks.get(i));
         	}
         }
         catch(Exception ex)
         {
         	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_CHECK_METHOD);
         }
    }
    
    /**
	 * create or update the ruleengine checks and check methods.
	 * 
	 */
    public static void createOrUpdateDatabaseTablesActions(MySqlConnection connection, File jareFile, String databaseName) throws Exception
    {
    	 ArrayList<Class> actions = getClasses(jareFile, Constants.PACKAGE_RULEENGINE_ACTIONS);
         
         try
         {
         	for(int i=0;i<actions.size();i++)
         	{
         		createOrUpdateAction(connection, actions.get(i));
         	}
         }
         catch(Exception ex)
         {
         	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_CHECK);
         }
         try
         {
         	for(int i=0;i<actions.size();i++)
         	{
         		//createOrUpdateActionMethod(connection, actions.get(i));
         	}
         }
         catch(Exception ex)
         {
         	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_CHECK_METHOD);
         }
    }
    
    
    public static void createOrUpdateCheck(MySqlConnection connection, Class<GenericCheck> genericCheck) throws Exception
	{
		if(genericCheck.isAnnotationPresent(CheckAnnotation.class))
		{
			CheckAnnotation ca = genericCheck.getAnnotation(CheckAnnotation.class);
			
			Check check = new Check();
			check.setConnection(connection);
			check.setClassName(genericCheck.getSimpleName());
	    	boolean checkExists = check.existClass();
			
	    	check.setName(ca.name());
    		check.setDescription(ca.description());
    		check.setNameDescriptive(ca.nameDescriptive());
    		check.setPackageName(Constants.PACKAGE_RULEENGINE_CHECKS);
    		check.setClassName(genericCheck.getSimpleName());
    		check.setCheckSingleField(ca.checkSingleField());
    		
	    	if(!checkExists)
	    	{
	    		PreparedStatement statement = connection.getPreparedStatement(Check.INSERT_SQL);
	    		check.insert(statement);
	    	}
	    	else
	    	{
	    		PreparedStatement statement = connection.getPreparedStatement(Check.UPDATE_SQL);
	    		check.update(statement);
	    	}
		}
	}
    
    public static void createOrUpdateCheckMethod(MySqlConnection connection, Class<GenericCheck> genericCheck) throws Exception
	{
    	Check check = new Check();
		check.setConnection(connection);
		check.setClassName(genericCheck.getSimpleName());
    	boolean checkExists = check.existClass();
    	
    	if(checkExists)
    	{
	    	Method[] methods = genericCheck.getMethods(); 
	    	
			for(int i=0;i<methods.length;i++)
			{
		    	Method method = methods[i];
		    	if(method.getName().equals("evaluate"))
		    	{
			    	Parameter[] parameters = method.getParameters();
					CheckMethodAnnotation cma = method.getAnnotation(CheckMethodAnnotation.class);
		    	
			    	CheckMethod checkMethod = new CheckMethod();
			    	checkMethod.setConnection(connection);
			    	checkMethod.setCheckId(check.getId());
			    	checkMethod.setCompare(parameters[0].getType().getSimpleName());
			    	if(parameters.length>=2)
		    		{
			    		checkMethod.setCompareTo(parameters[1].getType().getSimpleName());
		    		}
		    		if(parameters.length>=3)
		    		{
		    			checkMethod.setParameter1(parameters[2].getType().getSimpleName());
		    		}
		    		if(parameters.length>=4)
		    		{
		    			checkMethod.setParameter2(parameters[3].getType().getSimpleName());
		    		}
		    		if(parameters.length>=5)
		    		{
		    			checkMethod.setParameter3(parameters[4].getType().getSimpleName());
		    		}
		    		
		    		if(method.isAnnotationPresent(CheckMethodAnnotation.class))
					{
			    		checkMethod.setNote(cma.note());
			    		if(cma.noteParameter().length>=1)
			    		{
			    			checkMethod.setParameter1Explanation(cma.noteParameter()[0]);
			    		}
			    		if(cma.noteParameter().length>=2)
			    		{
			    			checkMethod.setParameter2Explanation(cma.noteParameter()[1]);
			    		}
			    		if(cma.noteParameter().length>=3)
			    		{
			    			checkMethod.setParameter3Explanation(cma.noteParameter()[2]);
			    		}
					}
				
			    	boolean checkMethodExists = checkMethod.existMethod();
			    	
			    	//check_id, compare, compare_to, note, parameter1, parameter1_explanation, parameter2, parameter2_explanation, parameter3, parameter3_explanation
			    	if(!checkMethodExists)
			    	{
			    		PreparedStatement statement = connection.getPreparedStatement(CheckMethod.INSERT_SQL);
			    		checkMethod.insert(statement);
			    	}
			    	else
			    	{
			    		PreparedStatement statement = connection.getPreparedStatement(CheckMethod.UPDATE_SQL);
			    		checkMethod.update(statement);
			    	}
		    	}
	    	}
    	}
	}
    
    public static void createOrUpdateAction(MySqlConnection connection, Class<GenericAction> genericAction) throws Exception 
	{

    	Method[] methods = genericAction.getMethods(); 
    	for(int i=0;i<methods.length;i++)
		{
	    	Method method = methods[i];
	    	if(method.isAnnotationPresent(ActionAnnotation.class))
	    	{
		    	Parameter[] parameters = method.getParameters();
		    	String parameterString="";
		    	for(int f=1;f<parameters.length;f++)
		    	{
		    		parameterString = parameterString + parameters[f].getType().getSimpleName();
		    		if(f<parameters.length-1)
		    		{
		    			parameterString = parameterString +", ";
		    		}
		    	}
		    	
		    	Action action = new Action();
				action.setConnection(connection);
				action.setClassname(Constants.PACKAGE_RULEENGINE_CHECKS + "." + genericAction.getSimpleName());
				action.setMethodname(method.getName());
	
				boolean actionExists = action.exist();
	    	
		    	if(method.isAnnotationPresent(ActionAnnotation.class))
				{
					ActionAnnotation aa = method.getAnnotation(ActionAnnotation.class);
					action.setDescription(aa.description());
			    	action.setMethoddisplayname(aa.methodDisplayname());
			    	if(aa.methodDisplayname().equals("concat values"))
			    	{
			    		System.out.println();
			    	}
				}
	    		
		    	
		    	
		    	if(!actionExists)
		    	{
		    		PreparedStatement statement = connection.getPreparedStatement(Action.INSERT_SQL);
		    		action.insert(statement);
		    	}
		    	else
		    	{
		    		
		    		PreparedStatement statement = connection.getPreparedStatement(Action.UPDATE_SQL);
		    		action.update(statement);
		    	}
		    	
		    	ActionMethod actionMethod = new ActionMethod();
		    	actionMethod.setConnection(connection);
		    	actionMethod.setActionId(action.getId());
		    	actionMethod.setReturnType(method.getReturnType().getSimpleName());
		    	actionMethod.setMethodTypes(parameterString);
		    	
		    	// find method with same name and same return type but different parameters
		    	// to detect optional types (check logic....)
		    	// get the method with the least parameters first
		    	
		    	if(method.isAnnotationPresent(ActionMethodAnnotation.class))
				{
		    		ActionMethodAnnotation ama = method.getAnnotation(ActionMethodAnnotation.class);
		    		
		    		actionMethod.setNote(ama.note());
				}
		    	
		    	boolean actionMethodExists = actionMethod.existMethod();
		    	
		    	if(!actionMethodExists)
		    	{
		    		PreparedStatement statement = connection.getPreparedStatement(ActionMethod.INSERT_SQL);
		    		actionMethod.insert(statement);
		    	}
		    	else
		    	{
		    		PreparedStatement statement = connection.getPreparedStatement(ActionMethod.UPDATE_SQL);
		    		actionMethod.update(statement);
		    	}
	    	}    	
	    	
		}
	}
    
    public static File getJareJarFile()
    {
	    String location = Controller.getProperty(Controller.CONTEXT_PATH)+ "WEB-INF/lib";
	    File folder = new File(location);
	    File jareJarFile = null;
	    if(folder.isDirectory())
	    {
	    	File files[] = folder.listFiles();
	    	for(File file : files)
	    	{
	    		if(file.getName().matches(Constants.JARE_JAR_FILE_PATTERN))
	    		{
	    			jareJarFile = file;
	    			break;
	    		}
	    	}
	    }
	    return jareJarFile;
    }
    
    /**
	 * Find all classes that are present in the given package.
	 * 
	 * 
	 * @param 	packageName
	 * @return	array of classes
	 * @throws 	ClassNotFoundException
	 * @throws 	IOException
	 */
	private static ArrayList<Class> getClasses(File jareFile,String packageName) throws ClassNotFoundException, IOException, Exception 
	{
		
		ArrayList<Class> classes = new ArrayList<>();
		
		String packageFolder = packageName.replace(".", "/");
	    JarFile jarFile = new JarFile(jareFile);
	    
	    return getJarFileClasses(jarFile, packageFolder);
	}
	
	private static JarFile getJarFile(String folder, String filenamePattern) throws Exception
	{
		JarFile jarFile = null;
		File jareFileFolder = new File(folder);
		if(jareFileFolder.isDirectory())
	    {
	    	File[] files = jareFileFolder.listFiles();
	    	for(int i=0;i<files.length;i++)
	    	{
	    		if(files[i].getName().matches(filenamePattern))
	    		{
	    			jarFile = new JarFile(files[i]);
	    			break;
	    		}
	    	}
	    }
		return jarFile;
	}
	
	private static ArrayList<Class> getJarFileClasses(JarFile jarFile,String packageFolder) throws Exception
	{
		ArrayList<Class> classes = new ArrayList<>();
		if(jarFile != null)
	    {
			Enumeration<JarEntry> entry = jarFile.entries();
		    while (entry.hasMoreElements()) 
		    {
		        JarEntry je = entry.nextElement();
		        if(!je.isDirectory() && je.getName().endsWith(".class") && je.getName().startsWith(packageFolder))
		        {
			        String className = je.getName().substring(0,je.getName().length()-6);
			        className = className.replace('/', '.');
		        	classes.add(Class.forName(className));
		        }
		    }
	    }
		return classes;
	}
}
