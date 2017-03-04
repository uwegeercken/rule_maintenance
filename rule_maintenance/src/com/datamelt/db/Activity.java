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
import java.sql.Timestamp;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class Activity extends DatabaseRecord implements Loadable
{
	private Timestamp date;
	private User user = new User();
	private String message;
	
	private static final String TABLENAME="activity_log";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (activity_date, user_id, message) values (?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set activity_date=?, user_id=?, message=? where id =?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	public Activity()
	{
	}
	
	public Activity(long id)
	{
		this.setId(id);
	}
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.date = rs.getTimestamp("activity_date");
	        this.message = rs.getString("message");
	        
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

	public void update(PreparedStatement p, Project project, User user) throws Exception
	{
		java.util.Date now = new java.util.Date();
		
		p.setTimestamp(1,new java.sql.Timestamp(now.getTime()));
		p.setLong(2,user.getId());
		p.setString(3,message);
		p.setLong(4,getId());

		p.executeUpdate();
	}
	
	public void insert(PreparedStatement p) throws Exception
    {
		java.util.Date now = new java.util.Date();
		
		p.setTimestamp(1,new java.sql.Timestamp(now.getTime()));
		p.setLong(2,user.getId());
		p.setString(3,message);
		
		p.execute();
        
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p, Project project, User user) throws Exception
	{
		p.setLong(1,getId());
		p.executeUpdate();
	}

	public Timestamp getDate()
	{
		return date;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
	

}
