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


public class Check extends DatabaseRecord implements Loadable
{
	private String name;
	private String description;
	private String packageName;
	private String className;
	private String nameDescriptive;
	private int checkSingleField;
	
	private static final String TABLENAME="`check`";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	private static final String SELECT_BY_NAME_SQL="select * from " + TABLENAME + " where name=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (name, description, name_descriptive, package, class, check_single_field) values (?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set name=?, description=?, name_descriptive=?, package=?, class=?, check_single_field=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + "  where name =?";
    public static final String EXIST_CLASS_SQL  = "select id from " + TABLENAME + "  where class=?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	public Check()
	{
		
	}
	
	public Check(long id)
	{
		this.setId(id);
	}
	
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.name = rs.getString("name");
	        this.description = rs.getString("description");
	        this.nameDescriptive = rs.getString("name_descriptive");
	        this.packageName = rs.getString("package");
	        this.className = rs.getString("class");
	        this.checkSingleField = rs.getInt("check_single_field");
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
	}
	
	public void loadByName() throws Exception
	{
        ResultSet rs = selectRecordByName(getConnection().getPreparedStatement(SELECT_BY_NAME_SQL));
		if(rs.next())
		{
	        this.setId(rs.getLong("id"));
	        this.description = rs.getString("description");
	        this.nameDescriptive = rs.getString("name_descriptive");
	        this.packageName = rs.getString("package");
	        this.className = rs.getString("class");
	        this.checkSingleField = rs.getInt("check_single_field");
		}
        rs.close();
	}
	
	private ResultSet selectRecordById(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}

	private ResultSet selectRecordByName(PreparedStatement p) throws Exception
	{
		p.setString(1,getName());
		return p.executeQuery();
	}
	
	private ResultSet selectExistCheck(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		return p.executeQuery();
	}
	
	private ResultSet selectExistClass(PreparedStatement p) throws Exception
	{
		p.setString(1,className);
		return p.executeQuery();
	}
	
	public void update(PreparedStatement p) throws Exception
	{
		p.setString(1,name);
		p.setString(2,description);
		p.setString(3,nameDescriptive);
		p.setString(4,packageName);
		p.setString(5,className);
		p.setInt(6,checkSingleField);
		
		p.setLong(7,getId());

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
        p.setString(1,name);
		p.setString(2,description);
		p.setString(3,nameDescriptive);
		p.setString(4,packageName);
		p.setString(5,className);
		p.setInt(6,checkSingleField);
		
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
	
	public boolean exist(String newName) throws Exception
    {
        ResultSet rs = selectExistCheck(getConnection().getPreparedStatement(EXIST_SQL),newName);
		boolean exists=false;
        if(rs.next())
		{
	        exists = true;
		}
        rs.close();
        return exists;
    }
	
	public boolean existClass() throws Exception
    {
        ResultSet rs = selectExistClass(getConnection().getPreparedStatement(EXIST_CLASS_SQL));
		boolean exists=false;
        if(rs.next())
		{
        	this.setId(rs.getLong("id"));
	        exists = true;
		}
        rs.close();
        return exists;
    }
	
	public String getName()
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public String getNameDescriptive() {
		return nameDescriptive;
	}

	public void setNameDescriptive(String nameDescriptive)
	{
		this.nameDescriptive = nameDescriptive;
	}

	public int getCheckSingleField()
	{
		return checkSingleField;
	}

	public void setCheckSingleField(int checkSingleField) 
	{
		this.checkSingleField = checkSingleField;
	}

	public String getPackageName() 
	{
		return packageName;
	}

	public void setPackageName(String packageName) 
	{
		this.packageName = packageName;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className) 
	{
		this.className = className;
	}



}
