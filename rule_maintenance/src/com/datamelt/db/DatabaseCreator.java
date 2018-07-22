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

import java.sql.Statement;

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
        Statement statement = connection.getStatement();
        statement.execute("create database if not exists " + databaseName);
        
        createDatabaseTables(connection, databaseName);
        createDatabaseTablesData(connection, databaseName);
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
        try
        {
        	statement.execute(CreateDatabase.CREATE_CHECKS);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_CHECK);
        }
        try
        {
        	statement.execute(CreateDatabase.CREATE_CHECK_METHODS);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_CHECK_METHOD);
        }
        try
        {
        	statement.execute(CreateDatabase.CREATE_ACTIONS);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_ACTION);
        }
        try
        {
        	statement.execute(CreateDatabase.CREATE_ACTION_METHODS);
        }
        catch(Exception ex)
        {
        	throw new Exception("error creating base data for table: " + CreateDatabase.TABLE_ACTION_METHOD);
        }

        finally
        {
        	statement.close();
        }
    }
}
