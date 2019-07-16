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


import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class Action extends DatabaseRecord implements Loadable
{
	private String description;
	private String classname;
	private String methodname;
	private String methoddisplayname;
	
	private static final String TABLENAME="action";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (description, classname, methodname, methoddisplayname) values (?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + "  set description=?, classname=?, methodname=?, methoddisplayname=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + "  where classname=? and methodname=?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	public Action()
	{
		
	}
	
	public Action(long id)
	{
		this.setId(id);
	}
	
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.description = rs.getString("description");
			this.classname = rs.getString("classname");
	        this.methodname = rs.getString("methodname");
	        this.methoddisplayname = rs.getString("methoddisplayname");
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
	}
	
	private ResultSet selectRecordById(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}

	private ResultSet selectExistAction(PreparedStatement p) throws Exception
	{
		p.setString(1,classname);
		p.setString(2,methodname);
		return p.executeQuery();
	}
	
	public void update(PreparedStatement p) throws Exception
	{
		p.setString(1, description);
		p.setString(2,classname);
		p.setString(3,methodname);
		p.setString(4,methoddisplayname);
		
		p.setLong(5,getId());

		try
		{
			p.executeUpdate();
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement p) throws Exception
    {
		p.setString(1, description);
		p.setString(2,classname);
		p.setString(3,methodname);
		p.setString(4,methoddisplayname);
		
        p.execute();
        
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		try
		{
			p.executeUpdate();
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean exist() throws Exception
    {
        ResultSet rs = selectExistAction(getConnection().getPreparedStatement(EXIST_SQL));
		boolean exists=false;
        if(rs.next())
		{
	        this.setId(rs.getLong("id"));
        	exists = true;
		}
        rs.close();
        return exists;
    }

	public String getClassname()
	{
		return classname;
	}
	
	public String getClassnameShort()
	{
		if(classname!=null && classname.length()>0)
		{
			int position = classname.lastIndexOf(".");
			return classname.substring(position+1);
		}
		else
		{
			return classname;
		}
		
	}

	public void setClassname(String classname) 
	{
		this.classname = classname;
	}

	public String getMethodname() 
	{
		return methodname;
	}

	public void setMethodname(String methodname) 
	{
		this.methodname = methodname;
	}

	public String getMethoddisplayname() 
	{
		return methoddisplayname;
	}

	public void setMethoddisplayname(String methoddisplayname) 
	{
		this.methoddisplayname = methoddisplayname;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	
}
