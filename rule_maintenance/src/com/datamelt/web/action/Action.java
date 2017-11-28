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
package com.datamelt.web.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

import com.datamelt.db.MySqlConnection;
import com.datamelt.plugin.BeanshellPlugin;

import bsh.Interpreter;

public abstract class Action  
{
    private MySqlConnection connection;
    private boolean databaseConnectionOk = false;
    private boolean hostConnectionOk = false;
    public static final String DEFAULT_VELOCITY_OBJECT = "dbObject";
    
    public abstract String execute(HttpServletRequest request, HttpServletResponse response, Context context, Interpreter interpreter,String scriptName, ArrayList<BeanshellPlugin> plugins );
    
	public String adjustInvalidCharacters(String value)
	{
	    String adjustedString="";
	    if(value!=null)
		{
	        adjustedString = value.replaceAll("'", "\'\'");
	        adjustedString = adjustedString.replaceAll("\"", "\"\"");
		}
	    return adjustedString;
	}
	
	public void setConnection(MySqlConnection con)
	{
	    this.connection = con;
	}
	
	protected MySqlConnection getConnection()
	{
	    return connection;
	}
	
	protected boolean getDatabaseConnectionOk()
	{
		return databaseConnectionOk;
	}
	
	public void setDatabaseConnectionOk(boolean databaseConnectionOk)
	{
		this.databaseConnectionOk = databaseConnectionOk;
	}
	
	protected boolean getHostConnectionOk()
	{
		return hostConnectionOk;
	}
	
	public void setHostConnectionOk(boolean hostConnectionOk)
	{
		this.hostConnectionOk = hostConnectionOk;
	}

}

