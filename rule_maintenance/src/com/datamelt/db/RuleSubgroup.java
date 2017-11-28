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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class RuleSubgroup extends DatabaseRecord implements Loadable
{
	private String name;
	private String description;
	private long rulegroupId;
	private String intergroupOperator;
	private String ruleOperator;
	private ArrayList<Rule> rules = new ArrayList<Rule>();
	private User lastUpdateUser = new User();
	
	private static final String TABLENAME="rulesubgroup";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	private static final String SELECT_RULES_SQL="select * from " + Rule.TABLENAME + " where rulesubgroup_id=? order by id";
	private static final String SELECT_BY_NAME_SQL="select * from " + TABLENAME + " where name=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (name, description, rulegroup_id, intergroupoperator,ruleoperator,last_update_user_id) values (?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set name=?, description=?, rulegroup_id=?, intergroupoperator=?, ruleoperator=?, last_update_user_id=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + "  where name =? and rulegroup_id=?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	public RuleSubgroup()
	{
		
	}
	
	public RuleSubgroup(long id)
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
	        this.rulegroupId = rs.getLong("rulegroup_id");
	        this.intergroupOperator = rs.getString("intergroupoperator");
	        this.ruleOperator = rs.getString("ruleoperator");
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
	        setLastUpdate(rs.getString("last_update"));
	        
	        loadRules();
	        
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
	        this.rulegroupId = rs.getLong("rulegroup_id");
	        this.intergroupOperator = rs.getString("intergroupoperator");
	        this.ruleOperator = rs.getString("ruleoperator");
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
	        setLastUpdate(rs.getString("last_update"));
	        
	        loadRules();

		}
        rs.close();
	}
	
	private void loadRules() throws Exception
	{
		ResultSet rs = selectRuleRecords(getConnection().getPreparedStatement(SELECT_RULES_SQL));
		while(rs.next())
		{
	        Rule rule = new Rule();
	        rule.setId(rs.getLong("id"));
	        rule.setConnection(getConnection());
	        rule.load();
	        
	        rules.add(rule);

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
	
	private ResultSet selectExistRuleSubgroup(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		p.setLong(2,rulegroupId);
		return p.executeQuery();
	}
	
	public ResultSet selectGetInstancesCount(PreparedStatement p) throws Exception
    {
		p.setLong(1,this.getId());
		return p.executeQuery();
    }
	
	private ResultSet selectRuleRecords(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}
	
	public void update(PreparedStatement p, Project project, User user) throws Exception
	{
		p.setString(1,name);
		p.setString(2,description);
		p.setLong(3,rulegroupId);
		p.setString(4,intergroupOperator);
		p.setString(5,ruleOperator);
		p.setLong(6,lastUpdateUser.getId());
		
		p.setLong(7,getId());

		try
		{
			if(user.canUpdateProject(project))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to update the subgroup");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement p, Project project, User user) throws Exception
    {
        p.setString(1,name);
		p.setString(2,description);
		p.setLong(3,rulegroupId);
		p.setString(4,intergroupOperator);
		p.setString(5,ruleOperator);
		p.setLong(6,lastUpdateUser.getId());
		
		try
		{
			if(user.canUpdateProject(project))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to insert the subgroup");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
        
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p, Project project, User user) throws Exception
	{
		p.setLong(1,getId());
		try
		{
			if(user.canUpdateProject(project))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to delete the subgroup");	
			}
			
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean exist(String newName) throws Exception
    {
        ResultSet rs = selectExistRuleSubgroup(getConnection().getPreparedStatement(EXIST_SQL),newName);
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

	public String getNameXMLEscaped()
	{
		return StringEscapeUtils.escapeXml(name);
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getDescription() 
	{
		return description;
	}

	public String getDescriptionXMLEscaped() 
	{
		return StringEscapeUtils.escapeXml(description);
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public long getRulegroupId()
	{
		return rulegroupId;
	}

	public void setRulegroupId(long rulegroupId)
	{
		this.rulegroupId = rulegroupId;
	}

	public String getIntergroupOperator()
	{
		return intergroupOperator;
	}

	public void setIntergroupOperator(String intergroupOperator) 
	{
		this.intergroupOperator = intergroupOperator;
	}

	public String getRuleOperator() 
	{
		return ruleOperator;
	}

	public void setRuleOperator(String ruleOperator) 
	{
		this.ruleOperator = ruleOperator;
	}

	public ArrayList<Rule> getRules() 
	{
		return rules;
	}
	
	public Set<String> getReferencedFields()
	{
		Set<String> fields = new LinkedHashSet<String>();
		//ArrayList<String> fields = new ArrayList<String>();
		for(int i=0;i<rules.size();i++)
		{
			Rule rule = getRules().get(i);
			if(rule.getObject1Parameter()!=null)
			{
				fields.add(rule.getObject1Parameter());
			}
			if(rule.getObject2Parameter()!=null)
			{
				fields.add(rule.getObject2Parameter());
			}

		}
		
		return fields;
	}

	public User getLastUpdateUser()
	{
		return lastUpdateUser;
	}

	public void setLastUpdateUser(User user) 
	{
		this.lastUpdateUser = user;
	}

}
