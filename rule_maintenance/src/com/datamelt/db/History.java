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


public class History extends DatabaseRecord implements Loadable
{
	private String type;
	private long typeId;
	private User user = new User();
	private long parent1;
	private long parent2;
	private long parent3;
	
	private Project project;
	private Object historyEntry;
	
	private static final String TABLENAME="history";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (type, type_id, user_id, parent_1,parent_2,parent_3) values (?,?,?,?,?,?)";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";
	
	public History()
	{
		
	}
	
	public History(long id)
	{
		this.setId(id);
	}
	
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.type = rs.getString("type");
	        this.typeId = rs.getLong("type_id");
	        this.parent1 = rs.getLong("parent_1");
	        this.parent2 = rs.getLong("parent_2");
	        this.parent3 = rs.getLong("parent_3");
	        
	        this.user.setId(rs.getLong("user_id"));
	        this.user.setConnection(getConnection());
	        this.user.load();
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
	}
	
	private ResultSet selectRecordById(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}
	
	public void insert(PreparedStatement p) throws Exception
    {
        p.setString(1,type);
		p.setLong(2,typeId);
		p.setLong(3,user.getId());
		p.setLong(4,parent1);
		p.setLong(5,parent2);
		p.setLong(6,parent3);
		
		p.execute();
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		p.executeUpdate();
	}
	
	public String getType()
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public long getTypeId()
	{
		return typeId;
	}

	public void setTypeId(long typeId)
	{
		this.typeId = typeId;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user) 
	{
		this.user = user;
	}

	public long getParent1() 
	{
		return parent1;
	}

	public void setParent1(long parent1) 
	{
		this.parent1 = parent1;
	}

	public long getParent2() 
	{
		return parent2;
	}

	public void setParent2(long parent2) 
	{
		this.parent2 = parent2;
	}

	public long getParent3() 
	{
		return parent3;
	}

	public void setParent3(long parent3) 
	{
		this.parent3 = parent3;
	}

	public Project getProject()
	{
		return project;
	}

	public void setProject(Project project) 
	{
		this.project = project;
	}

	public Object getHistoryEntry()
	{
		return historyEntry;
	}

	public void setHistoryEntry(Object historyEntry) 
	{
		this.historyEntry = historyEntry;
	}

}
