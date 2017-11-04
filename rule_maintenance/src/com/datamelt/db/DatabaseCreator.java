package com.datamelt.db;

import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseCreator
{
	/**
	 * get a list of all projects 
	 */
    public static void createDatabase(MySqlConnection connection, String databaseName, String fileName) throws Exception
    {
        connection.getStatement().execute("create database " + databaseName);
        connection.getStatement().execute("use " + databaseName);
    	
    	String sql="source " + fileName + ";";
        connection.getStatement().execute(sql);
    }
}
