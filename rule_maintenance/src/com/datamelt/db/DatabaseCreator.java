package com.datamelt.db;

import java.sql.Statement;

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
	 * create also all required basic data.
	 */
    public static void createDatabaseTables(MySqlConnection connection, String databaseName) throws Exception
    {
        Statement statement = connection.getStatement();

        // use the given database
    	statement.execute("use " + databaseName);
        
    	// create all required tables
        statement.execute(CreateDatabase.CREATE_TABLE_ACTION_METHOD_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_ACTION_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_ACTIVITY_LOG_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_CHECK_METHOD_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_CHECK_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_GROUPS_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_GROUPUSER_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_HISTORY_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_PROJECT_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_REFERENCE_FIELDS_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_RULE_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_RULEGROUP_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_RULEGROUPACTION_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_RULESUBGROUP_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_TYPES_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_USER_SQL);
        statement.execute(CreateDatabase.CREATE_TABLE_RULEGROUP_TESTDATA);
        
        statement.close();
        
    }
    
    public static void createDatabaseTablesData(MySqlConnection connection, String databaseName) throws Exception
    {
    	 Statement statement = connection.getStatement();

         // use the given database
     	statement.execute("use " + databaseName);
     	
    	// create all required base data
        statement.execute(CreateDatabase.CREATE_GROUPS);
        statement.execute(CreateDatabase.CREATE_USERS);
        statement.execute(CreateDatabase.CREATE_GROUPUSERS);
        statement.execute(CreateDatabase.CREATE_TYPES);
        statement.execute(CreateDatabase.CREATE_CHECKS);
        statement.execute(CreateDatabase.CREATE_CHECK_METHODS);
        statement.execute(CreateDatabase.CREATE_ACTIONS);
        statement.execute(CreateDatabase.CREATE_ACTION_METHODS);
        
        statement.close();
    }
}
