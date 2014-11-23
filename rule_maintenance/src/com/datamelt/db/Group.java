/*
 * 
 * Use of this software and code is only allowed after
 * prior permission by GeeNic Software Solutions Ltd. Copying,
 * redistribution or re-selling of this software is prohibited
 * and protected by national and international laws and/or patents.
 * 
 * All intellectual property rights remain with GeeNic Software SolutonsLtd.
 *
 * Created on 04.11.2004
 * Author uwe
 */

package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Group extends DatabaseRecord implements Loadable
{
    private String name;
    private String description;
    
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
    
    public void insert(PreparedStatement p) throws Exception
    {
        p.setString(1,name);
        p.setString(2,description);
        p.execute();
        
        setId(getConnection().getLastInsertId());
    }

    public void delete(PreparedStatement p) throws Exception
    {
        p.setLong(1,getId());
        p.execute();
        
        setId(getConnection().getLastInsertId());
    }

    public void removeAllAssignedUsers(PreparedStatement p) throws Exception
    {
        p.setLong(1,getId());
        p.execute();
    }

	public void update(PreparedStatement p) throws Exception
	{
		p.setString(1,name);
		p.setString(2,description);
		p.setLong(3,getId());

		try
		{
			p.executeUpdate();
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void updatePagesForRemovedGroup(PreparedStatement p,long newGroupsId) throws Exception
	{
		p.setLong(1,newGroupsId);
		p.setLong(2,getId());

		try
		{
			p.executeUpdate();
			
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
}
