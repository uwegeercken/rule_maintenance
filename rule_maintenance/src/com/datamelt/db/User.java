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
import java.util.ArrayList;

public class User extends DatabaseRecord implements Loadable
{
    private String userid;
    private String name;
    private String password;
    private String lastLogin;
    private int deleted;
    private String deletedDate;
    private ArrayList<Group> groups=new ArrayList<Group>();
    
    public static final String UPDATE_PASSWORD_SQL          = "update user set password=password(?) where id =?";
    public static final String UPDATE_LASTLOGIN_SQL         = "update user set lastlogin=? where id =?";
    public static final String UPDATE_SQL       		    = "update user set userid=?, name=? where id =?";
    public static final String DELETE_SQL       		    = "update user set deleted=?, deleted_date=? where id =?";
    public static final String ADD_GROUP_MEMBERSHIP  	    = "insert into groupuser (groups_id,user_id) values (?,?)";
    public static final String DELETE_GROUP_MEMBERSHIP	    = "delete from groupuser where groups_id=? and user_id=?";
    public static final String DELETE_ALL_GROUP_MEMBERSHIPS = "delete from groupuser where user_id=?";
    public static final String INSERT_SQL       		    = "insert into user (userid, name, password) values (?,?,password(?))";
    public static final String ADMINISTRATOR                = "admin";
    public static final String READ_WRITE_USER              = "user";
    public static final String READ_USER		            = "user_ro";
    
    public void load() throws Exception
    {
        String sql="select * from user where id=" + getId();
        ResultSet rs = getConnection().getResultSet(sql);
		if(rs.next())
		{
	        this.userid = rs.getString("userid");
	        this.name = rs.getString("name");
	        this.password = rs.getString("password");
	        this.lastLogin = rs.getString("lastlogin");
	        
	        this.deleted = rs.getInt("deleted");
	        if(deleted==1)
	        {
	            this.deletedDate = rs.getString("deleted_date");
	        }
	        
	        setLastUpdate(rs.getString("last_update"));
	        
	        try
	        {
	            loadGroups();
	        }
	        catch(Exception ex)
	        {
	            
	        }
	        
		}
        rs.close();
    }

    public void loadGroups() throws Exception
    {
        groups.clear();
        String sql="select groups.id" +
        		" from groupuser,user,groups" +
        		" where groupuser.user_id=user.id" +
        		" and groupuser.groups_id=groups.id" +
        		" and groupuser.user_id=" + getId() + 
        		" order by groups.name";
        ResultSet rs = getConnection().getResultSet(sql);
		while(rs.next())
		{
	        Group group = new Group();
	        group.setConnection(getConnection());
	        group.setId(rs.getLong("id"));
	        group.load();
	        groups.add(group);
		}
        rs.close();
    }
    
    public String getGroupsAsString()
    {
    	StringBuffer buffer = new StringBuffer();
    	for(int i=0;i<groups.size();i++)
    	{
    		Group group = groups.get(i);
    		buffer.append(group.getDescription());
    		if(i<groups.size()-1)
    		{
    			buffer.append(", ");
    		}
    		
    	}
    	return buffer.toString();
    }
    
    public void insert(PreparedStatement p, String userPassword) throws Exception
    {
        p.setString(1,userid);
        p.setString(2,name);
        p.setString(3, userPassword);
        p.execute();
        
        setId(getConnection().getLastInsertId());
    }

    public void insertUserGroup(PreparedStatement p,long groupId) throws Exception
    {
        p.setLong(1,getId());
        p.setLong(2,groupId);
        p.execute();
    }

    public void delete(PreparedStatement p) throws Exception
    {
        p.setInt(1,deleted);
        p.setString(2,deletedDate);
        p.setLong(3,getId());
        p.execute();
        
        setId(getConnection().getLastInsertId());
    }

    public void deleteUserGroup(PreparedStatement p,long groupsId) throws Exception
    {
        p.setLong(1,groupsId);
        p.setLong(2,getId());
        p.execute();
    }

    public void deleteAllGroupMemberships(PreparedStatement p) throws Exception
    {
        p.setLong(1,getId());
        p.execute();
    }

    public void loadByUserid() throws Exception
    {
        String sql="select * from user where userid='" + getUserid() +"' and deleted=0";
        ResultSet rs = getConnection().getResultSet(sql);
		if(rs.next())
		{
	        this.setId(rs.getLong("id"));
		    this.userid = rs.getString("userid");
	        this.name = rs.getString("name");
	        this.password = rs.getString("password");
	        this.lastLogin = rs.getString("lastlogin");
	        
	        setLastUpdate(rs.getString("last_update"));
	        try
	        {
	            loadGroups();
	        }
	        catch(Exception ex)
	        {
            
	        }
	        
		}
        rs.close();
    }

	public void updateLastLogin(PreparedStatement p, String date) throws Exception
	{
		p.setString(1,date);
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
	
	public void update(PreparedStatement p) throws Exception
	{
		p.setString(1,userid);
		p.setString(2,name);

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
	
	public void updatePassword(PreparedStatement p,String password) throws Exception
	{
		p.setString(1,password);

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
	
	public void addGroupMembership(PreparedStatement p,long groupId) throws Exception
	{
		p.setLong(1,groupId);
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
	
	public void deleteGroupMembership(PreparedStatement p,long groupId) throws Exception
	{
		p.setLong(1,groupId);
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
	
    public boolean isPasswordOk(String password) throws Exception
    {
        return checkDbLoginOk(password);
    }
    
    public boolean isLocalPasswordOk(String password) throws Exception
    {
        return checkDbLoginOk(password);
    }
    
    private boolean checkDbLoginOk(String password) throws Exception
    {
        String sql="select password=password('" + password + "') as pwdok from user where userid='" + getUserid()+"'";
        ResultSet rs = getConnection().getResultSet(sql);
        int passwordOk=0;
        if(rs.next())
        {
            passwordOk = rs.getInt("pwdok");
        }
        rs.close();
        return passwordOk==1;
    }
    
    public boolean exist(String uid) throws Exception
    {
        String sql="select id from user where userid ='" + uid + "' and deleted=0";
        ResultSet rs = getConnection().getResultSet(sql);
		boolean exists=false;
        if(rs.next())
		{
	        exists = true;
		}
        rs.close();
        return exists;
    }
    
    public boolean isInGroup(long groupId)
    {
        boolean inGroup=false;
        for(int i=0;i<groups.size();i++)
        {
            Group group = (Group)groups.get(i);
            if(group.getId()==groupId)
            {
                inGroup=true;
                break;
            }
        }
        return inGroup;
    }
    
    public boolean isInGroup(String groupName)
    {
        boolean inGroup=false;
        for(int i=0;i<groups.size();i++)
        {
            Group group = (Group)groups.get(i);
            if(group.getName().toLowerCase().equals(groupName.toLowerCase()))
            {
                inGroup=true;
                break;
            }
        }
        return inGroup;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getUserid()
    {
        return userid;
    }
    
    public void setUserid(String userid)
    {
        this.userid = userid;
    }
    
    public String getLastLogin()
    {
        return lastLogin;
    }
    
    public void setLastLogin(String lastLogin)
    {
        this.lastLogin = lastLogin;
    }
    
    public ArrayList<Group> getGroups()
    {
        return groups;
    }
    
    public int getDeleted()
    {
        return deleted;
    }
    
    public void setDeleted(int deleted)
    {
        this.deleted = deleted;
    }
    
    public String getDeletedDate()
    {
        return deletedDate;
    }
    
    public void setDeletedDate(String deletedDate)
    {
        this.deletedDate = deletedDate;
    }
    
}
