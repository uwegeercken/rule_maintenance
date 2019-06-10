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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Used to do updates (patching) on the users database. Either tables or data itself.
 * 
 * When the web app is started, the changes are applied, so that the user has
 * a working database and does not need to do the patching himself/herself.
 * 
 * The idea is to remove this code after a few releases.
 * 
 * @author uwe geercken
 *
 */
public class DatabaseUpdate
{
	private static final String CHECK_TABLE_RULEGROUP 									= "select disabled from " + CreateDatabase.TABLE_RULEGROUP + " limit 1";
	private static final String UPDATE_TABLE_RULEGROUP 									= "alter table rulegroup add column disabled tinyint(2) default 0 after dependent_rulegroup_id";
	
	private static final String ADD_ACTION_CONVERT_TO_INTEGER   						= "INSERT INTO " + CreateDatabase.TABLE_ACTION + " VALUES (52,'Convert a String to an Integer value','com.datamelt.rules.core.action.ConvertAction','toInteger','convert to integer',now())";
	private static final String ADD_ACTION_METHOD_CONVERT_TO_INTEGER					= "INSERT INTO " + CreateDatabase.TABLE_ACTION_METHOD + " VALUES (137,52,'integer','String',null,null,null,NULL,NULL,NULL,NULL,now())";

	private static final String ADD_ACTION_CONVERT_TO_LONG								= "INSERT INTO " + CreateDatabase.TABLE_ACTION + " VALUES (53,'Convert a String to a Long value','com.datamelt.rules.core.action.ConvertAction','toLong','convert to long',now())";
	private static final String ADD_ACTION_METHOD_CONVERT_TO_LONG						= "INSERT INTO " + CreateDatabase.TABLE_ACTION_METHOD + " VALUES (138,53,'long','String',null,null,null,NULL,NULL,NULL,NULL,now())";
	
	private static final String ADD_ACTION_CONVERT_TO_DOUBLE							= "INSERT INTO " + CreateDatabase.TABLE_ACTION + " VALUES (54,'Convert a String to a Double value','com.datamelt.rules.core.action.ConvertAction','toDouble','convert to double',now())";
	private static final String ADD_ACTION_METHOD_CONVERT_TO_DOUBLE						= "INSERT INTO " + CreateDatabase.TABLE_ACTION_METHOD + " VALUES (139,54,'double','String',null,null,null,NULL,NULL,NULL,NULL,now())";
	
	private static final String ADD_ACTION_CONVERT_TO_FLOAT								= "INSERT INTO " + CreateDatabase.TABLE_ACTION + " VALUES (55,'Convert a String to a Float value','com.datamelt.rules.core.action.ConvertAction','toFloat','convert to float',now())";
	private static final String ADD_ACTION_METHOD_CONVERT_TO_FLOAT						= "INSERT INTO " + CreateDatabase.TABLE_ACTION_METHOD + " VALUES (140,55,'float','String',null,null,null,NULL,NULL,NULL,NULL,now())";
	
	private static final String ADD_ACTION_CONVERT_4CHARACTERS_TIME						= "INSERT INTO " + CreateDatabase.TABLE_ACTION + " VALUES (56,'Convert a four character String which represents time (format: hhmm) to an Integer value, representing minutes','com.datamelt.rules.core.action.ConvertAction','fourDigitTimetoMinutes','convert 4 characters time',now())";
	private static final String ADD_ACTION_METHOD_CONVERT_4CHARACTERS_TIME				= "INSERT INTO " + CreateDatabase.TABLE_ACTION_METHOD + " VALUES (141,56,'integer','String',null,null,null,NULL,NULL,NULL,NULL,now())";
	
	private static final String ADD_ACTION_CONVERT_5CHARACTERS_TIME						= "INSERT INTO " + CreateDatabase.TABLE_ACTION + " VALUES (57,'Convert a five character String which represents time (format: hh:mm) to an Integer value, representing minutes','com.datamelt.rules.core.action.ConvertAction','fiveDigitTimetoMinutes','convert 5 characters time',now())";
	private static final String ADD_ACTION_METHOD_CONVERT_5CHARACTERS_TIME				= "INSERT INTO " + CreateDatabase.TABLE_ACTION_METHOD + " VALUES (142,57,'integer','String',null,null,null,NULL,NULL,NULL,NULL,now())";

	private static final String ADD_CHECK_DATE_TIME_IS_BETWEEN							= "INSERT INTO " + CreateDatabase.TABLE_CHECK + " VALUES (41,'Check Date Time Is Between','Checks if the time part of a date is between two given time value specified in the format HH:mm:ss (hours, minutes, seconds). Separate the time values using a comma.','time part is between','com.datamelt.rules.implementation','CheckDateTimeIsBetween',0,now())";
	private static final String ADD_CHECK_METHOD_DATE_TIME_IS_BETWEEN					= "INSERT INTO " + CreateDatabase.TABLE_CHECK_METHOD + " VALUES (169,41,'Date','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,now())";

	public static void alterDatabaseTables(MySqlConnection connection, String dbName)
	{
		// add disabled field. implemented 2018-07-20
		//alterTable(connection, CHECK_TABLE_RULEGROUP, UPDATE_TABLE_RULEGROUP);
		
		// added 2018-07-28
		try
		{
			//insertData(connection, CreateDatabase.TABLE_ACTION, ADD_ACTION_CONVERT_TO_INTEGER);
			//insertData(connection, CreateDatabase.TABLE_ACTION_METHOD, ADD_ACTION_METHOD_CONVERT_TO_INTEGER);
		}
		catch(Exception psex)
		{
			psex.printStackTrace();
		}

		try
		{
			//insertData(connection, CreateDatabase.TABLE_ACTION, ADD_ACTION_CONVERT_TO_LONG);
			//insertData(connection, CreateDatabase.TABLE_ACTION_METHOD, ADD_ACTION_METHOD_CONVERT_TO_LONG);
		}
		catch(Exception psex)
		{
			psex.printStackTrace();
		}
	
		try
		{
			//insertData(connection, CreateDatabase.TABLE_ACTION, ADD_ACTION_CONVERT_TO_DOUBLE);
			//insertData(connection, CreateDatabase.TABLE_ACTION_METHOD, ADD_ACTION_METHOD_CONVERT_TO_DOUBLE);
		}
		catch(Exception psex)
		{
			psex.printStackTrace();
		}
	
		try
		{
			//insertData(connection, CreateDatabase.TABLE_ACTION, ADD_ACTION_CONVERT_TO_FLOAT);
			//insertData(connection, CreateDatabase.TABLE_ACTION_METHOD, ADD_ACTION_METHOD_CONVERT_TO_FLOAT);
		}
		catch(Exception psex)
		{
			psex.printStackTrace();
		}
		
		try
		{
			//insertData(connection, CreateDatabase.TABLE_ACTION, ADD_ACTION_CONVERT_4CHARACTERS_TIME);
			//insertData(connection, CreateDatabase.TABLE_ACTION_METHOD, ADD_ACTION_METHOD_CONVERT_4CHARACTERS_TIME);
		}
		catch(Exception psex)
		{
			psex.printStackTrace();
		}
		
		try
		{
			//insertData(connection, CreateDatabase.TABLE_ACTION, ADD_ACTION_CONVERT_5CHARACTERS_TIME);
			//insertData(connection, CreateDatabase.TABLE_ACTION_METHOD, ADD_ACTION_METHOD_CONVERT_5CHARACTERS_TIME);
		}
		catch(Exception psex)
		{
			psex.printStackTrace();
		}
		
		try
		{
			//insertData(connection, CreateDatabase.TABLE_CHECK, ADD_CHECK_DATE_TIME_IS_BETWEEN);
			//insertData(connection, CreateDatabase.TABLE_CHECK_METHOD, ADD_CHECK_METHOD_DATE_TIME_IS_BETWEEN);
		}
		catch(Exception psex)
		{
			psex.printStackTrace();
		}
	}
	
	private static void alterTable(MySqlConnection connection, String checkSql, String alterSql)
	{
		try
		{
			connection.getResultSet(checkSql);
	        
		}
		catch(Exception ex)
		{
			try
			{
				PreparedStatement ps = connection.getPreparedStatement(alterSql);
				ps.execute();
			}
			catch(Exception psex)
			{
				
			}
		}
	}
	
	private static void insertData(MySqlConnection connection, String tableName, String insertSql) throws Exception
	{
		PreparedStatement psId = connection.getPreparedStatement("select max(id) as maxid from " + tableName);
		ResultSet rs = psId.executeQuery();
		rs.next();
		long maxId = rs.getLong("maxid");
		
		PreparedStatement ps = connection.getPreparedStatement(insertSql);
		ps.execute();
	}
}
