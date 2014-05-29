package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;
import com.datamelt.util.VelocityDataWriter;


public class Project extends DatabaseRecord implements Loadable
{
	private String name;
	private String description;
	private String databaseHostname;
	private String databaseName;
	private String databaseTableName;
	private String databaseUserid;
	private String databaseUserPassword;
	private long numberOfRuleGroups;

	private ArrayList<RuleGroup> rulegroups;
	private User user = new User();
	
	private static final String TABLENAME="project";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	private static final String SELECT_BY_NAME_SQL="select * from " + TABLENAME + " where name=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (name, description, database_hostname, database_name, database_tablename, database_userid, database_user_password, last_update_user_id) values (?,?,?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set name=?, description=?, database_hostname=?, database_name=?, database_tablename=?, database_userid=?, database_user_password=?, last_update_user_id=? where id=?";
    public static final String EXIST_SQL  = "select id from  " + TABLENAME + "  where name =?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	
	public Project()
	{
		
	}
	
	public Project(long id)
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
	        this.databaseHostname = rs.getString("database_hostname");
	        this.databaseName = rs.getString("database_name");
	        this.databaseTableName = rs.getString("database_tablename");
	        this.databaseUserid = rs.getString("database_userid");
	        this.databaseUserPassword = rs.getString("database_user_password");
	        
	        this.user.setId(rs.getLong("last_update_user_id"));
	        this.user.setConnection(getConnection());
	        this.user.load();
	        
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
	        this.description = rs.getString("description");
	        this.databaseHostname = rs.getString("database_hostname");
	        this.databaseName = rs.getString("database_name");
	        this.databaseTableName = rs.getString("database_tablename");
	        this.databaseUserid = rs.getString("database_userid");
	        this.databaseUserPassword = rs.getString("database_user_password");
	        
	        this.user.setId(rs.getLong("last_update_user_id"));
	        this.user.setConnection(getConnection());
	        this.user.load();
	        
	        setLastUpdate(rs.getString("last_update"));
		}
        rs.close();
	}
	
	public void loadRuleGroups(String date) throws Exception
	{
		rulegroups = new ArrayList<RuleGroup>();
		rulegroups = DbCollections.getAllValidRuleGroups(getConnection(), this.getId(), date);
		
		for(int i=0;i<rulegroups.size();i++)
		{
			RuleGroup rulegroup = rulegroups.get(i);
			rulegroup.loadRuleSubgroups();
		}
	}
	
	public void loadRuleGroupsCount() throws Exception
	{
		numberOfRuleGroups = DbCollections.getAllRuleGroupsCount(getConnection(), this.getId());
	}
	
	public String mergeWithTemplate(RuleGroup rulegroup,String templatePath, String templateName) throws Exception
	{
		VelocityDataWriter dataWriter = new VelocityDataWriter(templatePath, templateName);
		dataWriter.addObject("rulegroup", rulegroup);
		return dataWriter.merge();
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
	
	private ResultSet selectExistProject(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		return p.executeQuery();
	}
	
	public ResultSet selectGetInstancesCount(PreparedStatement p) throws Exception
    {
		p.setLong(1,this.getId());
		return p.executeQuery();
    }
	
	public void update(PreparedStatement p) throws Exception
	{
		p.setString(1,name);
		p.setString(2,description);
		p.setLong(3,user.getId());
		p.setString(3,databaseHostname);
		p.setString(4,databaseName);
		p.setString(5,databaseTableName);
		p.setString(6,databaseUserid);
		p.setString(7,databaseUserPassword);
		
		p.setLong(8,getId());

		try
		{
			p.executeUpdate();
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement p) throws Exception
    {
        p.setString(1,name);
		p.setString(2,description);
		p.setString(3,databaseHostname);
		p.setString(4,databaseName);
		p.setString(5,databaseTableName);
		p.setString(6,databaseUserid);
		p.setString(7,databaseUserPassword);
		p.setLong(8,user.getId());
		
        p.execute();
        
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		try
		{
			p.executeUpdate();
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean exist(String newName) throws Exception
    {
        ResultSet rs = selectExistProject(getConnection().getPreparedStatement(EXIST_SQL),newName);
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

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public ArrayList<RuleGroup> getRulegroups()
	{
		return rulegroups;
	}

	public void setRulegroups(ArrayList<RuleGroup> rulegroups) 
	{
		this.rulegroups = rulegroups;
	}

	public long getNumberOfRuleGroups()
	{
		return numberOfRuleGroups;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user) 
	{
		this.user = user;
	}

	public String getDatabaseHostname()
	{
		return databaseHostname;
	}

	public void setDatabaseHostname(String databaseHostname) 
	{
		this.databaseHostname = databaseHostname;
	}

	public String getDatabaseName() 
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName) 
	{
		this.databaseName = databaseName;
	}

	public String getDatabaseUserid() 
	{
		return databaseUserid;
	}

	public void setDatabaseUserid(String databaseUserid) 
	{
		this.databaseUserid = databaseUserid;
	}

	public String getDatabaseUserPassword() 
	{
		return databaseUserPassword;
	}

	public void setDatabaseUserPassword(String databaseUserPassword) 
	{
		this.databaseUserPassword = databaseUserPassword;
	}

	public String getDatabaseTableName()
	{
		return databaseTableName;
	}

	public void setDatabaseTableName(String databaseTableName) 
	{
		this.databaseTableName = databaseTableName;
	}

}
