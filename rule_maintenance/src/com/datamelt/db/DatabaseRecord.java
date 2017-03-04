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

/**
 * @author uwegeercken
 *
 */
public abstract class DatabaseRecord
{
	private long id;
	private String lastUpdate;
	private MySqlConnection connection;
	
	/**
	 * returns the database id of the database record
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * sets the database id of the database record.
	 * 
	 * @param id database id of the record
	 */
	public void setId(long id)
	{
		this.id = id;
	}
	
	/**
	 * returns the date when the database record was last updated
	 */
	public String getLastUpdate()
	{
		return lastUpdate;
	}
	
	/**
	 * returns the date when the database record was last updated
	 */
	public String getLastUpdate(int length)
	{
		return lastUpdate.substring(0,length);
	}
	
	/**
	 * Method used to set the last update date of the database object.
	 * On update of the database record, the last_update field is updated
	 * automatically and does not need to be set. This method shall only be used
	 * when you want to overwrite this behaviour.<br>
	 * <br>
	 * The date format needs to be in the format that the database
	 * is expecting. In MySql this field is defined as 'timestamp' and expects following
	 * format: YYYYMMDDHHNNSS (y=year, m=month, d=day, h=hour, n=minute, s=second).<br>
	 * <br>
	 * example: 20031016114618
	 * 
	 * @param date a date in the expected database date format
	 */
	public void setLastUpdate(String lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * Method used to hand back a database connection object.
	 * If the database connection object has not been set for the
	 * database record, then data can not be retrieved and an
	 * exception is thrown.
	 * 
	 */
	public MySqlConnection getConnection() throws Exception
	{
		if (connection!=null)
		{
			return connection;
		}
		else
		{
			throw new Exception("database connection object undefined");	
		}
	}
	
	/**
	 * Method used to set the database connection object
	 * for the database record.
	 * 
	 * @param db a database connection object
	 */
	public void setConnection(MySqlConnection connection)
	{
		this.connection = connection;
	}
	
	
	public String adjustQuotes(String value)
	{
		if(value==null || value.trim().equals(""))
		{
		    return value;
		}
		else
		{
		    return value.replaceAll("'", "''");
		}
	}
	
	public String adjustCopyright(String value)
	{
		if(value==null || value.trim().equals(""))
		{
		    return value;
		}
		else
		{
		    return value.replaceAll("�", "");
		}
	}

}
