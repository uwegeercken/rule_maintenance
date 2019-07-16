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


public class JareVersion extends DatabaseRecord implements Loadable
{
	private String version;
	
	private static final String TABLENAME="jare_version";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	private static final String SELECT_LATEST_SQL="select * from " + TABLENAME + " order by last_update desc limit 1";
	private static final String SELECT_VERSION_SQL="select * from " + TABLENAME + " where version=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (version) values (?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set version=? where id =?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	public JareVersion()
	{
	}
	
	public JareVersion(long id)
	{
		this.setId(id);
	}
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.version = rs.getString("version");
	        setLastUpdate(rs.getString("last_update"));
		}
        rs.close();
	}
	
	public void loadLatest() throws Exception
	{
        ResultSet rs = selectLatestRecord(getConnection().getPreparedStatement(SELECT_LATEST_SQL));
		if(rs.next())
		{
	        this.setId(rs.getLong("id"));
			this.version = rs.getString("version");
	        setLastUpdate(rs.getString("last_update"));
		}
        rs.close();
	}
	
	public void loadByVersion() throws Exception
	{
        ResultSet rs = selectRecordByVersion(getConnection().getPreparedStatement(SELECT_VERSION_SQL));
		if(rs.next())
		{
			this.setId(rs.getLong("id"));
			this.version = rs.getString("version");
	        setLastUpdate(rs.getString("last_update"));
		}
        rs.close();
	}
	
	private ResultSet selectRecordById(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}
	
	private ResultSet selectLatestRecord(PreparedStatement p) throws Exception
	{
		return p.executeQuery();
	}
	
	private ResultSet selectRecordByVersion(PreparedStatement p) throws Exception
	{
		p.setString(1,getVersion());
		return p.executeQuery();
	}

	public void update(PreparedStatement p) throws Exception
	{
		p.setString(1,version);
		p.setLong(2,getId());

		p.executeUpdate();
	}
	
	public void insert(PreparedStatement p) throws Exception
    {
		p.setString(1,version);
		
		p.execute();
        
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p, Project project) throws Exception
	{
		p.setLong(1,getId());
		p.executeUpdate();
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
	

}
