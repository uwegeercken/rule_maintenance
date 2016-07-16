package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class RuleGroup extends DatabaseRecord implements Loadable
{
	private String name;
	private String description;
	private String validFrom;
	private String validUntil;
	private long projectId;
	private User lastUpdateUser = new User();
	
	private ArrayList<RuleSubgroup> ruleSubgroups;
	private ArrayList<RuleGroupAction> actions;
	
	private static final String TABLENAME="rulegroup";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	private static final String SELECT_BY_NAME_SQL="select * from " + TABLENAME + " where name=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (name, description, valid_from, valid_until, project_id, last_update_user_id) values (?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set name=?, description=?, valid_from=?, valid_until=?, project_id=?, last_update_user_id=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + " where name =? and project_id=?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	
	public RuleGroup()
	{
		
	}
	
	public RuleGroup(long id)
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
	        this.projectId = rs.getLong("project_id");
	        this.validFrom = rs.getString("valid_from");
	        this.validUntil = rs.getString("valid_until");
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
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
	        this.projectId = rs.getLong("project_id");
	        this.validFrom = rs.getString("valid_from");
	        this.validUntil = rs.getString("valid_until");

	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
	        setLastUpdate(rs.getString("last_update"));
		}
        rs.close();
	}
	
	public void loadRuleSubgroups() throws Exception
	{
		ruleSubgroups = new ArrayList<RuleSubgroup>();
		ruleSubgroups = DbCollections.getAllRuleSubgroups(getConnection(), getId());
	}
	
	public void loadRuleGroupActions() throws Exception
	{
		actions = new ArrayList<RuleGroupAction>();
		actions = DbCollections.getAllRuleGroupActions(getConnection(), getId());
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
	
	private ResultSet selectExistRuleGroup(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		p.setLong(2, projectId);
		return p.executeQuery();
	}
	
	public boolean isValid() throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFrom = sdf.parse(validFrom);
		Date dateUntil = sdf.parse(validUntil);
		
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		Calendar from = Calendar.getInstance();
		from.setTime(dateFrom);
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		
		Calendar until = Calendar.getInstance();
		until.setTime(dateUntil);
		until.set(Calendar.HOUR_OF_DAY, 0);
		until.set(Calendar.MINUTE, 0);
		until.set(Calendar.SECOND, 0);
		until.set(Calendar.MILLISECOND, 0);

		
		boolean valid = today.compareTo(from)>=0 && today.compareTo(until)<=0;
		return valid;  
	}
	
	public void update(PreparedStatement p, Project project, User user) throws Exception
	{
		p.setString(1,name);
		p.setString(2,description);
		p.setString(3,validFrom);
		p.setString(4,validUntil);
		p.setLong(5,projectId);
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
				throw new Exception("user " + user.getUserid() + " is not allowd to update the rulegroup");	
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
		p.setString(3,validFrom);
		p.setString(4,validUntil);
		p.setLong(5,projectId);
		p.setLong(6,lastUpdateUser.getId());
		
		try
		{
			if(user.canUpdateProject(project))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowd to insert the rulegroup");	
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
				throw new Exception("user " + user.getUserid() + " is not allowd to delete the rulegroup");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean exist(String newName) throws Exception
    {
        ResultSet rs = selectExistRuleGroup(getConnection().getPreparedStatement(EXIST_SQL),newName);
		boolean exists=false;
        if(rs.next())
		{
	        exists = true;
		}
        rs.close();
        return exists;
    }
	
	public Set<String> getReferencedFields()
	{
		Set<String> fields = new LinkedHashSet<String>();
		for(int i=0;i<ruleSubgroups.size();i++)
		{
			RuleSubgroup subgroup = ruleSubgroups.get(i);
			fields.addAll(subgroup.getReferencedFields());
		}
		
		for(int f=0;f<actions.size();f++)
		{
			RuleGroupAction action = actions.get(f);
			if(action.getObject1Parameter()!=null)
			{
				fields.add(action.getObject1Parameter());
			}
		}
		
		return fields;
	}
	
	public Set<String> getReferencedFieldsToUpdate()
	{
		Set<String> fields = new LinkedHashSet<String>();
		for(int f=0;f<actions.size();f++)
		{
			RuleGroupAction action = actions.get(f);
			if(action.getObject2Parameter()!=null)
			{
				fields.add(action.getObject2Parameter());
			}
		}
		
		return fields;
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

	public long getProjectId()
	{
		return projectId;
	}

	public void setProjectId(long projectId)
	{
		this.projectId = projectId;
	}

	public String getValidFrom()
	{
		return validFrom;
	}

	public void setValidFrom(String validFrom) 
	{
		this.validFrom = validFrom;
	}

	public String getValidUntil() 
	{
		return validUntil;
	}

	public void setValidUntil(String validUntil) 
	{
		this.validUntil = validUntil;
	}

	public ArrayList<RuleSubgroup> getRuleSubgroups()
	{
		return ruleSubgroups;
	}

	public void setRuleSubgroups(ArrayList<RuleSubgroup> ruleSubgroups) 
	{
		this.ruleSubgroups = ruleSubgroups;
	}

	public ArrayList<RuleGroupAction> getActions()
	{
		return actions;
	}

	public void setActions(ArrayList<RuleGroupAction> actions) 
	{
		this.actions = actions;
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
