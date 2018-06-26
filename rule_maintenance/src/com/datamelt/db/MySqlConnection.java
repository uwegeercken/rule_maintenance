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
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

import java.sql.DatabaseMetaData;

public class MySqlConnection 
{
    private static final String connect    			= "jdbc:mysql://";
    private static final String connectParameters 	= "useUnicode=false&characterEncoding=UTF-8";
    private final String jdbcDriver					= "com.mysql.jdbc.Driver";
    private String hostname;
    private String databaseName;
	private Connection connection;
	private String username;
    private String password;
    private int port = 3306;
    
	private static final String EXCEPTION_CONNECTION_UNDEFINED = "connection object undefined";
	
	public MySqlConnection() throws Exception
	{
		loadProperties();
	}
    
	public MySqlConnection(String hostname, String databaseName, String username, String password) throws Exception
	{
		this.username = username;
		this.password = password;
		this.hostname = hostname;
		this.databaseName = databaseName;
		connect();
	}
	
	public MySqlConnection(String hostname, int port,String databaseName, String username, String password) throws Exception
	{
		this.username = username;
		this.password = password;
		this.hostname = hostname;
		this.databaseName = databaseName;
		this.port = port;
		connect();
	}
	
	public MySqlConnection(String hostname, int port, String username, String password) throws Exception
	{
		this.username = username;
		this.password = password;
		this.hostname = hostname;
		this.port = port;
		connect();
	}
	
	public MySqlConnection(String hostname, String databaseName) throws Exception
	{
		this.hostname = hostname;
		this.databaseName = databaseName;
	} 

	private void loadProperties() throws Exception
	{
		Properties props = new Properties();
		File f = new File(getConfigFilePath());
		FileInputStream input = new FileInputStream(f);
		props.load(input);
		hostname       = props.getProperty(Constants.PROPERTY_HOSTNAME);
		databaseName   = props.getProperty(Constants.PROPERTY_DATABASENAME);
		input.close();
		if (hostname==null || hostname.trim().equals(""))
		{
			throw new Exception(Constants.PROPERTY_HOSTNAME + " undefined in " + f);
		}
		if (databaseName==null || databaseName.trim().equals(""))
		{
			throw new Exception(Constants.PROPERTY_DATABASENAME + " undefined in " + f);
		}
	}

	public String getConfigFilePath()
	{
		return this.getClass().getResource(Constants.DATABASE_PROPERTIES_FILE).getPath();
	}

    public void connect() throws Exception
    {
        this.connection = getConnection();
    }
    
    public String getHostname()
    {
        return hostname;
    }
    
    public String getDatabaseName()
    {
        return databaseName;
    }
    
    public ResultSet getResultSet(String sql) throws Exception
    {
        if (connection!=null)
        {
        	Statement stmt = null;
        	stmt = getStatement();
        	return stmt.executeQuery(sql);
        }
        else
        {
        	throw new Exception(EXCEPTION_CONNECTION_UNDEFINED);
        }
    }

    /**
	 * pass a valid sql string to this method and it returns a java sql resultset.
	 * throws an error if the connection object is undefined 
	 * 
	 * resultsetType and resultsetConcurrency are the eqivalent values fron the$
	 * recordset class
	 */
	public ResultSet getResultSet(int resultsetType, int resultsetConcurrency,String sql) throws Exception
	{
		if(connection!=null)
		{
			return connection.createStatement(resultsetType,resultsetConcurrency).executeQuery(sql);
		}
		else
		{
			throw new Exception(EXCEPTION_CONNECTION_UNDEFINED);
		}
	}
	
    public Statement getStatement() throws Exception
    {
		if (connection!=null)
		{
			return connection.createStatement();
		}
		else
		{
			throw new Exception(EXCEPTION_CONNECTION_UNDEFINED);
		}
    }
    
    public PreparedStatement getPreparedStatement(String sql) throws Exception
    {
		if (connection!=null)
		{
			return connection.prepareStatement(sql);
		}
		else
		{
			throw new Exception(EXCEPTION_CONNECTION_UNDEFINED);
		}
    }

    public void close() throws Exception
    {
		connection.close();
    }
    
    private Connection getConnection() throws Exception
    {
        Class.forName(jdbcDriver).newInstance();
        Connection con;
        if(databaseName!=null)
        {
        	con = DriverManager.getConnection(connect + hostname + ":" + port + "/" + databaseName + "?" + connectParameters, username,password);
        }
        else
        {
        	con = DriverManager.getConnection(connect + hostname + ":" + port, username,password);
        }
        
        return con;
        
    }
    
    public DatabaseMetaData getMetaData() throws Exception
    {
    	return getConnection().getMetaData();
    }
    
    
    public static String formatDbDate(String dbDate)
    {
        return dbDate.substring(0,4) + "-" + dbDate.substring(4,6) + "-" + dbDate.substring(6,8) + " " + dbDate.substring(8,10) + ":" + dbDate.substring(10,12) + "." + dbDate.substring(12);
    }

    /**
	 * returns the last_insert_id. this is that id that was last assigned to
	 * an autoincrement column of the mysql database.
	 * so if you insert a record into a table and the table has an autoincrement
	 * column then the id of this autoincrement column will be returned. 
	 */
	public long getLastInsertId() throws Exception
	{
		ResultSet rs = getResultSet("select last_insert_id() as lastid");
		rs.next();
		long lastId = rs.getLong("lastid");
		rs.close();
		return lastId;
	}
    
	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	public void setAutoCommit(boolean status) throws Exception
	{
		connection.setAutoCommit(status);
	}

	public void commit() throws Exception
	{
		connection.commit();
	}

	public void rollback() throws Exception
	{
		connection.rollback();
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public int getPort()
	{
		return port;
	}

	public void setHostname(String hostname) 
	{
		this.hostname = hostname;
	}

	public void setDatabaseName(String databaseName) 
	{
		this.databaseName = databaseName;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}
	
	

}

