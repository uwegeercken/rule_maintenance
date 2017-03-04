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

public class Field extends DatabaseRecord implements Loadable
{
	private long projectId;
	private String name;
	private String nameDescriptive;
	private String description;
	private long javaTypeId;
	private User lastUpdateUser = new User();
	private String testValue;

	private Type type = new Type();
	
	private static final String TABLENAME						= "reference_fields";
	private static final String SELECT_SQL						= "select * from " + TABLENAME + " where id=?";
	private static final String SELECT_BY_NAME_SQL				= "select * from " + TABLENAME + " where name=? and project_id=?";
	
	public static final String INSERT_SQL 						= "insert into " + TABLENAME + " (project_id, name, name_descriptive, description, java_type_id,last_update_user_id) values (?,?,?,?,?,?)";
    public static final String UPDATE_SQL 						= "update " + TABLENAME + " set project_id=?, name=?, name_descriptive=?, description=?,java_type_id=?,last_update_user_id=? where id=?";
    public static final String EXIST_SQL  						= "select id from  " + TABLENAME + "  where name=? and project_id=?";
    public static final String DELETE_SQL 						= "delete from " + TABLENAME + " where id=?";
	
	public Field()
	{
		
	}
	
	public Field(long id)
	{
		this.setId(id);
	}
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.projectId = rs.getLong("project_id");
			this.name = rs.getString("name");
			this.nameDescriptive = rs.getString("name_descriptive");
			this.description = rs.getString("description");
	        this.javaTypeId = rs.getInt("java_type_id");
	        
	        this.type.setConnection(getConnection());
	        this.type.setId(javaTypeId);
	        this.type.load();
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
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
	        this.nameDescriptive = rs.getString("name_descriptive");
	        this.description = rs.getString("description");
	        this.javaTypeId = rs.getInt("java_type_id");
	        
	        this.type.setConnection(getConnection());
	        this.type.setId(javaTypeId);
	        this.type.load();
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
	        setLastUpdate(rs.getString("last_update"));
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
		p.setLong(2, projectId);
		return p.executeQuery();
	}
	
	private ResultSet selectExistField(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		p.setLong(2, projectId);
		return p.executeQuery();
	}
	
	public void update(PreparedStatement p) throws Exception
	{
		p.setLong(1, projectId);
		p.setString(2,name);
		p.setString(3,nameDescriptive);
		p.setString(4,description);
		p.setLong(5, javaTypeId);
		p.setLong(6,lastUpdateUser.getId());
		p.setLong(7,getId());
		
		p.executeUpdate();
	}
	
	public void insert(PreparedStatement p) throws Exception
    {
		p.setLong(1, projectId);
        p.setString(2,name);
        p.setString(3,nameDescriptive);
        p.setString(4,description);
		p.setLong(5, javaTypeId);
		p.setLong(6,lastUpdateUser.getId());
        
		p.execute();
		
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		p.executeUpdate();
	}
	
	public boolean exist(String newName) throws Exception
    {
        ResultSet rs = selectExistField(getConnection().getPreparedStatement(EXIST_SQL),newName);
		boolean exists=false;
        if(rs.next())
		{
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

	
	public String getNameDescriptive() 
	{
		return nameDescriptive;
	}

	public void setNameDescriptive(String nameDescriptive) 
	{
		this.nameDescriptive = nameDescriptive;
	}

	public long getJavaTypeId() 
	{
		return javaTypeId;
	}

	public long getProjectId() 
	{
		return projectId;
	}

	public void setProjectId(long projectId) 
	{
		this.projectId = projectId;
	}

	public void setJavaTypeId(long javaTypeId)
	{
		this.javaTypeId = javaTypeId;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public User getLastUpdateUser()
	{
		return lastUpdateUser;
	}

	public void setLastUpdateUser(User user) 
	{
		this.lastUpdateUser = user;
	}

	public Type getType() 
	{
		return type;
	}

	public String getTestValue() 
	{
		return testValue;
	}

	public void setTestValue(String testValue) 
	{
		this.testValue = testValue;
	}
	
}
