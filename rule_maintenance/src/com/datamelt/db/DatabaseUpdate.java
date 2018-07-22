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

/**
 * Used to do updates (patching) on the users database.
 * 
 * when the web app is started, the changes are applied, so that the user has
 * a working database and does not need to do the patching himself/herself.
 * 
 * @author uwe geercken
 *
 */
public class DatabaseUpdate
{
	private static final String CHECK_TABLE_RULEGROUP 			= "select disabled from rulegroup limit 1";
	private static final String UPDATE_TABLE_RULEGROUP 			= "alter table rulegroup add column disabled tinyint(2) default 0 after dependent_rulegroup_id";
	
	public static void alterDatabaseTables(MySqlConnection connection, String dbName)
	{
		// add disabled field. implemented 2018-07-20
		alterTable(connection, CHECK_TABLE_RULEGROUP, UPDATE_TABLE_RULEGROUP);
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
				ex.printStackTrace();
			}
		}
	}
}
