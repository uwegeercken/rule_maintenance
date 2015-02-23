package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.datamelt.util.Ldap;
 
public class User extends DatabaseRecord implements Loadable
{
    private String userid;
    private String name;
    private String password;
    private String lastLogin;
    private int deactivated;
    private String deactivatedDate;
    private boolean hasAvatar;
    private boolean ldapUser=false;
    private ArrayList<Group> groups=new ArrayList<Group>();
    
    private static final String TABLENAME					= "user";
    private static final String TABLENAME_GROUPUSER			= "groupuser";
    
    public static final String UPDATE_PASSWORD_SQL          = "update " + TABLENAME + " set password=password(?) where id =?";
    public static final String UPDATE_LASTLOGIN_SQL         = "update " + TABLENAME + " set lastlogin=? where id =?";
    public static final String UPDATE_SQL       		    = "update " + TABLENAME + " set userid=?, name=? where id =?";
    public static final String ACTIVATE_DEACTIVATE_SQL	    = "update " + TABLENAME + " set deactivated=?, deactivated_date=? where id =?";
    public static final String DELETE_SQL	    			= "delete from " + TABLENAME + " where id =?";
    public static final String ADD_GROUP_MEMBERSHIP  	    = "insert into " + TABLENAME_GROUPUSER + " (groups_id,user_id) values (?,?)";
    public static final String DELETE_GROUP_MEMBERSHIP	    = "delete from " + TABLENAME_GROUPUSER + " where groups_id=? and user_id=?";
    public static final String DELETE_ALL_GROUP_MEMBERSHIPS = "delete from " + TABLENAME_GROUPUSER + " where user_id=?";
    public static final String INSERT_SQL       		    = "insert into " + TABLENAME + " (userid, name, password) values (?,?,password(?))";
    public static final String ADMINISTRATOR                = "admin";

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
	        
	        this.deactivated = rs.getInt("deactivated");
	        if(deactivated==1)
	        {
	            this.deactivatedDate = rs.getString("deactivated_date");
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
    		buffer.append(group.getName());
    		if(i<groups.size()-1)
    		{
    			buffer.append(", ");
    		}
    		
    	}
    	return buffer.toString();
    }
    
    public void insert(PreparedStatement p, String userPassword, User user) throws Exception
    {
        p.setString(1,userid);
        p.setString(2,name);
        p.setString(3, userPassword);
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
				throw new Exception("user " + user.getUserid() + " is not allowed to delete the user");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }
    
    public void deactivate(PreparedStatement p, User user) throws Exception
    {
        p.setInt(1,deactivated);
        p.setString(2,deactivatedDate);
        p.setLong(3,getId());
        try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to deactivate the user");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
			
    }
    
    public void activate(PreparedStatement p, User user) throws Exception
    {
        p.setInt(1,0);
        p.setString(2,null);
        p.setLong(3,getId());
        try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to activate the user");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }
    
    public void deleteAllGroupMemberships(PreparedStatement p, User user) throws Exception
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
				throw new Exception("user " + user.getUserid() + " is not allowed to delete the users group memberships");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }

    public void loadByUserid() throws Exception
    {
        String sql="select * from user where userid='" + getUserid() +"' and deactivated=0";
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
	
	public void update(PreparedStatement p, User user) throws Exception
	{
		p.setString(1,userid);
		p.setString(2,name);

		p.setLong(3,getId());
		try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to update the user");	
			}
			
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
	
	public void addGroupMembership(PreparedStatement p,long groupId, User user) throws Exception
	{
		p.setLong(1,groupId);
		p.setLong(2,getId());

		try
		{
			if(user.isInGroup(User.ADMINISTRATOR))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to add group membership for the user");	
			}
			
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
	
    public boolean isPasswordOk(String password, Ldap ldap) throws Exception
    {
    	if(ldap!=null && checkLdapLoginOk(password, ldap))
    	{
    		ldapUser=true;
    		return true;
    	}
    	else
    	{
    		return checkDbLoginOk(password);
    	}
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
    
    private boolean checkLdapLoginOk(String password, Ldap ldap) throws Exception
    {
        return ldap.authenticate(this.userid, password);
    }

    public boolean exist(String uid) throws Exception
    {
        String sql="select id from user where userid ='" + uid + "' and deactivated=0";
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
    
    public boolean isInProjectGroup(Group projectGroup)
    {
    	boolean isInProjectGroup=false;
    	for(int i=0;i<groups.size();i++)
    	{
    		if(groups.get(i).getId()==projectGroup.getId())
    		{
    			isInProjectGroup = true;
    			break;
    		}
    	}
    	return isInProjectGroup;
    }
    
    public boolean canUpdateProject(Project project)
	{
		if(this.isInProjectGroup(project.getGroup()) ||this.isInGroup(User.ADMINISTRATOR) || project.getOwnerUser().getId()==this.getId())
    	{
    		return true;
    	}
		return false;
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
    
    public int getDeactivated()
    {
        return deactivated;
    }
    
    public void setDeactivated(int deactivated)
    {
        this.deactivated = deactivated;
    }
    
    public String getDeactivatedDate()
    {
        return deactivatedDate;
    }
    
    public void setDeactivatedDate(String deactivatedDate)
    {
        this.deactivatedDate = deactivatedDate;
    }

	public boolean isLdapUser() 
	{
		return ldapUser;
	}

	public boolean hasAvatar()
	{
		return hasAvatar;
	}
	
	public void setHasAvatar(boolean hasAvatar)
	{
		this.hasAvatar = hasAvatar;
	}
}
