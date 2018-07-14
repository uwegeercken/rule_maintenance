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
import java.util.HashMap;
import java.util.Map;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;

public class RuleGroupTestData extends DatabaseRecord implements Loadable
{
	private long ruleGroupId;
	private User user = new User();
	private String value;
	private Map<String,String> keyValuePairs = new HashMap<>();
	
	private static final String TABLENAME="rulegroup_testdata";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (rulegroup_id, user_id, value) values (?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set rulegroup_id=?, user_id=?, value=? where id =?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	public RuleGroupTestData()
	{
		
	}
	
	public RuleGroupTestData(long id)
	{
		this.setId(id);
	}
	
	public void load() throws Exception
	{
		ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.ruleGroupId = rs.getLong("rulegroup_id");
	        this.value = rs.getString("value");
	        
	        this.user.setId(rs.getLong("user_id"));
	        this.user.setConnection(getConnection());
	        this.user.load();
	        
	        setLastUpdate(rs.getString("last_update"));
	        
	        splitValue();
	        
		}
        rs.close();
	}
	
	private void splitValue()
	{
		value = value.substring(1, value.length()-1);
		String[] values = value.split(",");
		for(int i=0;i<values.length;i++)
		{
			String keyValue= values[i];
			String[] parts = keyValue.split("=");
			String pairKey = parts[0].trim();
			String pairValue="";
			if(parts.length>1)
			{
				pairValue = parts[1].trim();
			}
			keyValuePairs.put(pairKey, pairValue);
		}
	}
	
	public void update(PreparedStatement p, Project project, User user) throws Exception
	{
		p.setLong(1,ruleGroupId);
		p.setLong(2,user.getId());
		p.setString(3,value);
		
		p.setLong(4,getId());

		try
		{
			if(user.canUpdateProject(project))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowd to update the rulegroup");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement p) throws Exception
    {
		p.setLong(1,ruleGroupId);
		p.setLong(2,user.getId());
		p.setString(3,keyValuePairs.toString());
		
		p.execute();
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		p.executeUpdate();
	}

	private ResultSet selectRecordById(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}
	
	public long getRuleGroupId()
	{
		return ruleGroupId;
	}

	public void setRuleGroupId(long ruleGroupId)
	{
		this.ruleGroupId = ruleGroupId;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public Map<String, String> getKeyValuePairs()
	{
		return keyValuePairs;
	}

	public void setKeyValuePairs(Map<String, String> keyValuePairs)
	{
		this.keyValuePairs = keyValuePairs;
	}
	
	public String getKeyValuePair(String key)
	{
		return keyValuePairs.get(key);
	}
}
