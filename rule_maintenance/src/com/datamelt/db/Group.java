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

public class Group extends DatabaseRecord implements Loadable
{
    private String name;
    private String description;
    
    private ArrayList<User> users = new ArrayList<User>();
    
    public static final String ALL_USERS                      = "All Users";

    public static final String INSERT_SQL                     = "insert into groups (name,description) values (?,?)";
    public static final String DELETE_SQL                     = "delete from groups where id=?";
    public static final String REMOVE_ALL_ASSIGNED_USERS_SQL  = "delete from groupuser where groups_id=?";
    public static final String UPDATE_SQL                     = "update groups set name=?,description=? where id=?";
    public static final String UPDATE_REMOVED_GROUP_PAGES_SQL = "update page set groups_id=? where groups_id=?";

    public void load() throws Exception
    {
        String sql="select * from groups where id=" + getId();
        ResultSet rs = getConnection().getResultSet(sql);
		if(rs.next())
		{
	        this.description = rs.getString("description");
	        this.name = rs.getString("name");
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
    }

    public void loadByName() throws Exception
    {
        String sql="select * from groups where name='" + getName() +"'";
        ResultSet rs = getConnection().getResultSet(sql);
		if(rs.next())
		{
	        this.description = rs.getString("description");
	        setId(rs.getLong("id"));
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
    }

    public void loadUsers() throws Exception
    {
        String sql="select user_id from groupuser where groups_id=" + getId();
        ResultSet rs = getConnection().getResultSet(sql);
		while(rs.next())
		{
			User user = new User();
			user.setConnection(getConnection());
			user.setId(rs.getLong("user_id"));
			user.load();
			
			users.add(user);
		}
        rs.close();
    }
    
    public boolean exist(String gname) throws Exception
    {
        String sql="select id from groups where name ='" + gname + "'";
        ResultSet rs = getConnection().getResultSet(sql);
		boolean exists=false;
        if(rs.next())
		{
	        exists = true;
		}
        rs.close();
        return exists;
    }
    
    public void insert(PreparedStatement p, User user) throws Exception
    {
        p.setString(1,name);
        p.setString(2,description);
        try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.executeUpdate();
				setId(getConnection().getLastInsertId());
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to insert the user");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }

    public void delete(PreparedStatement p, User user) throws Exception
    {
        p.setLong(1,getId());
        try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to delete the group");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
        
    }

    public void removeAllAssignedUsers(PreparedStatement p, User user) throws Exception
    {
        p.setLong(1,getId());
        try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to remove users from the group");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }

	public void update(PreparedStatement p, User user) throws Exception
	{
		p.setString(1,name);
		p.setString(2,description);
		p.setLong(3,getId());
		try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to update the group");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
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
    
    public boolean equals(Object group)
    {
    	return this.getId()== ((Group)group).getId();
    }

	public ArrayList<User> getUsers() 
	{
		return users;
	}

	public void setUsers(ArrayList<User> users) 
	{
		this.users = users;
	}
    
	public String getUsersAsString()
    {
    	StringBuffer buffer = new StringBuffer();
    	for(int i=0;i<users.size();i++)
    	{
    		User user = users.get(i);
    		buffer.append(user.getName());
    		if(i<users.size()-1)
    		{
    			buffer.append(", ");
    		}
    		
    	}
    	return buffer.toString();
    }
    
}
