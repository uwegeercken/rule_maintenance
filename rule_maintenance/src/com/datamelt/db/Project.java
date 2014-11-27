package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;
import com.datamelt.rules.engine.BusinessRulesEngine;
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
	private int privateProject;

	private ArrayList<RuleGroup> rulegroups;
	private Group group = new Group();
	private User lastUpdateUser = new User();
	private User ownerUser = new User();
	
	private static final String TABLENAME						= "project";
	private static final String SELECT_SQL						= "select * from " + TABLENAME + " where id=?";
	private static final String SELECT_BY_NAME_SQL				= "select * from " + TABLENAME + " where name=?";
	
	public static final String INSERT_SQL 						= "insert into " + TABLENAME + " (name, description, database_hostname, database_name, database_tablename, database_userid, database_user_password, last_update_user_id,owner_user_id, group_id,is_private) values (?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_SQL 						= "update " + TABLENAME + " set name=?, description=?, database_hostname=?, database_name=?, database_tablename=?, database_userid=?, database_user_password=?, last_update_user_id=?, owner_user_id=?, group_id=?, is_private=? where id=?";
    public static final String EXIST_SQL  						= "select id from  " + TABLENAME + "  where name =?";
    public static final String DELETE_SQL 						= "delete from " + TABLENAME + " where id=?";
	
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
	        this.privateProject = rs.getInt("is_private");
	        this.databaseHostname = rs.getString("database_hostname");
	        this.databaseName = rs.getString("database_name");
	        this.databaseTableName = rs.getString("database_tablename");
	        this.databaseUserid = rs.getString("database_userid");
	        this.databaseUserPassword = rs.getString("database_user_password");
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
	        this.ownerUser.setId(rs.getLong("owner_user_id"));
	        this.ownerUser.setConnection(getConnection());
	        this.ownerUser.load();
	        
	        this.group.setId(rs.getLong("group_id"));
	        this.group.setConnection(getConnection());
	        this.group.load();
	        
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
	        this.privateProject = rs.getInt("is_private");
	        this.databaseHostname = rs.getString("database_hostname");
	        this.databaseName = rs.getString("database_name");
	        this.databaseTableName = rs.getString("database_tablename");
	        this.databaseUserid = rs.getString("database_userid");
	        this.databaseUserPassword = rs.getString("database_user_password");
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
	        this.ownerUser.setId(rs.getLong("owner_user_id"));
	        this.ownerUser.setConnection(getConnection());
	        this.ownerUser.load();
	        
	        this.group.setId(rs.getLong("group_id"));
	        this.group.setConnection(getConnection());
	        this.group.load();
	        
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
	
//	name, description, database_hostname, database_name, database_tablename, database_userid, database_user_password, last_update_user_id) values (?,?,?,?,?,?,?,?)";

	public void update(PreparedStatement p, User user) throws Exception
	{
		p.setString(1,name);
		p.setString(2,description);
		p.setString(3,databaseHostname);
		p.setString(4,databaseName);
		p.setString(5,databaseTableName);
		p.setString(6,databaseUserid);
		p.setString(7,databaseUserPassword);
		p.setLong(8,lastUpdateUser.getId());
		p.setLong(9,ownerUser.getId());
		p.setLong(10,group.getId());
		p.setLong(11,privateProject);
		
		p.setLong(12,getId());

		try
		{
			if(user.canUpdateProject(this))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowd to update the project");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement p, User user) throws Exception
    {
        p.setString(1,name);
		p.setString(2,description);
		p.setString(3,databaseHostname);
		p.setString(4,databaseName);
		p.setString(5,databaseTableName);
		p.setString(6,databaseUserid);
		p.setString(7,databaseUserPassword);
		p.setLong(8,lastUpdateUser.getId());
		p.setLong(9,ownerUser.getId());
		p.setLong(10,group.getId());
		p.setLong(11,privateProject);
		
		try
		{
			if(user.canUpdateProject(this))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to insert the project");	
			}
		}
        catch (Exception ex)
		{
			ex.printStackTrace();
		}
        
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p, User user) throws Exception
	{
		p.setLong(1,getId());
		try
		{
			if(user.canUpdateProject(this))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to delete the project");	
			}
			
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

	public User getLastUpdateUser()
	{
		return lastUpdateUser;
	}

	public void setLastUpdateUser(User user) 
	{
		this.lastUpdateUser = user;
	}
	
	public User getOwnerUser()
	{
		return ownerUser;
	}

	public void setOwnerUser(User user) 
	{
		this.ownerUser = user;
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
	
	/**
	 * import a set of rulegroups, subgroups, rules and actions
	 * from a zipfile into the database
	 * 
	 * some adjustments might need to be required if importing old files that where hand written. but an import of a project file
	 * that was previously exported using the wqeb interface should work correctly.
	 * 
	 * @param zipFile
	 * @throws Exception
	 */
	public void importProject(ZipFile zipFile,User user) throws Exception
	{
		BusinessRulesEngine ruleEngine = new BusinessRulesEngine(zipFile);
		ArrayList <com.datamelt.rules.core.RuleGroup>groups = ruleEngine.getGroups();
		for (int i=0;i<groups.size();i++)
		{
			com.datamelt.rules.core.RuleGroup group = groups.get(i);
			RuleGroup dbRuleGroup = new RuleGroup();
			dbRuleGroup.setProjectId(this.getId());
			dbRuleGroup.setName(group.getId());
			dbRuleGroup.setDescription(group.getDescription());
			if(group.getValidFrom()==null)
			{
				dbRuleGroup.setValidFrom("2014-01-01");
			}
			else
			{
				dbRuleGroup.setValidFrom(group.getValidFrom());
			}
			if(group.getValidUntil()==null)
			{
				dbRuleGroup.setValidUntil("2014-12-31");
				
			}
			else
			{
				dbRuleGroup.setValidUntil(group.getValidUntil());
			}
			dbRuleGroup.setLastUpdateUser(lastUpdateUser);
			dbRuleGroup.setConnection(getConnection());
			dbRuleGroup.insert(getConnection().getPreparedStatement(RuleGroup.INSERT_SQL),this,user);
			
			ArrayList <Type>dbTypes = DbCollections.getAllTypes(getConnection());
			ArrayList <com.datamelt.rules.core.XmlAction>actions =group.getActions();
			for (int k=0;k<actions.size();k++)
			{
				com.datamelt.rules.core.XmlAction action = actions.get(k);
				RuleGroupAction dbAction = new RuleGroupAction();
				dbAction.setRulegroupId(dbRuleGroup.getId());
				if(action.getId()==null || action.getId().trim().equals(""))
				{
					dbAction.setName("[undefined]");
				}
				else
				{
					dbAction.setName(action.getId());
				}
				if(action.getDescription()==null || action.getDescription().trim().equals(""))
				{
					dbAction.setDescription("[undefined]");
				}
				else
				{
					dbAction.setDescription(action.getDescription());
				}
				if(action.getExecuteIf()==0)
				{
					dbAction.setExecuteIf("passed");
				}
				else
				{
					dbAction.setExecuteIf("failed");
				}
				
				ArrayList <Action>dbActions = DbCollections.getAllActions(getConnection());
				for(int g=0;g<dbActions.size();g++)
				{
					Action oneAction = dbActions.get(g);
					if(action.getClassName().equals(oneAction.getClassname())&& action.getMethodName().equals(oneAction.getMethodname()))
					{
						dbAction.setAction(oneAction);
						break;
					}
				}
				if(action.getParameters().size()>0)
				{
					for(int h=0;h< dbTypes.size();h++)
					{
						Type type = dbTypes.get(h);
						if(type.getName().equals(action.getParameters().get(0).getType()))
						{
							dbAction.setParameter1(action.getParameters().get(0).getValue());
							dbAction.setParameter1Type(type);
							break;
						}
					}
				}
				if(action.getParameters().size()>1)
				{
					for(int h=0;h< dbTypes.size();h++)
					{
						Type type = dbTypes.get(h);
						if(type.getName().equals(action.getParameters().get(1).getType()))
						{
							dbAction.setParameter2(action.getParameters().get(1).getValue());
							dbAction.setParameter2Type(type);
							break;
						}
					}
				}
				if(action.getParameters().size()>2)
				{
					for(int h=0;h< dbTypes.size();h++)
					{
						Type type = dbTypes.get(h);
						if(type.getName().equals(action.getParameters().get(2).getType()))
						{
							dbAction.setParameter3(action.getParameters().get(2).getValue());
							dbAction.setParameter3Type(type);
							break;
						}
					}
				}
				if(action.getActionSetterObject()!=null)
				{
					dbAction.setObject2Classname(action.getActionSetterObject().getClassName());
					dbAction.setObject2Methodname(action.getActionSetterObject().getMethodName());
					for(int h=0;h< dbTypes.size();h++)
					{
						Type type = dbTypes.get(h);
						if(type.getName().equals(action.getActionSetterObject().getParameters().get(0).getType()))
						{
							dbAction.setObject2Parametertype(type);
							dbAction.setObject2Parameter(action.getActionSetterObject().getParameters().get(0).getValue());
							break;
						}
					}
					for(int h=0;h< dbTypes.size();h++)
					{
						Type type = dbTypes.get(h);
						if(type.getName().equals(action.getActionSetterObject().getParameters().get(1).getType()))
						{
							dbAction.setObject2Type(type);
							break;
						}
					}
				}
				// we only import the first getter object of an action as the web app currently only
				// supports one getter object
				if(action.getActionGetterObjects().size()>0)
				{
					dbAction.setObject1Classname(action.getActionGetterObjects().get(0).getClassName());
					dbAction.setObject1Methodname(action.getActionGetterObjects().get(0).getMethodName());
					for(int h=0;h< dbTypes.size();h++)
					{
						Type type = dbTypes.get(h);
						if(type.getName().equals(action.getActionGetterObjects().get(0).getParameters().get(0).getType()))
						{
							dbAction.setObject1Parametertype(type);
							dbAction.setObject1Parameter(action.getActionGetterObjects().get(0).getParameters().get(0).getValue());
							dbAction.setObject1Type(type);
							break;
						}
					}
				}
				dbAction.setLastUpdateUser(lastUpdateUser);
				dbAction.setConnection(getConnection());
				dbAction.insert(getConnection().getPreparedStatement(RuleGroupAction.INSERT_SQL),this,user);
			}
			
			ArrayList <com.datamelt.rules.core.RuleSubGroup> subgroups = group.getSubGroups();
			for (int f=0;f<subgroups.size();f++)
			{
				com.datamelt.rules.core.RuleSubGroup subgroup = subgroups.get(f);
				RuleSubgroup dbRuleSubGroup = new RuleSubgroup();
				dbRuleSubGroup.setName(subgroup.getId());
				dbRuleSubGroup.setDescription(subgroup.getDescription());
				dbRuleSubGroup.setIntergroupOperator(subgroup.getLogicalOperatorSubGroupAsString());
				dbRuleSubGroup.setRuleOperator(subgroup.getLogicalOperatorRulesAsString());
				dbRuleSubGroup.setRulegroupId(dbRuleGroup.getId());
				dbRuleSubGroup.setLastUpdateUser(lastUpdateUser);
				dbRuleSubGroup.setConnection(getConnection());
				dbRuleSubGroup.insert(getConnection().getPreparedStatement(RuleSubgroup.INSERT_SQL),this,user);
				
				ArrayList <Check>dbChecks = DbCollections.getAllChecks(getConnection());
				for(int m=0;m<subgroup.getRulesCollection().size();m++)
				{
					com.datamelt.rules.core.XmlRule rule = subgroup.getRulesCollection().get(m);
					Rule dbRule = new Rule();
					dbRule.setRuleSubgroupId(dbRuleSubGroup.getId());
					dbRule.setName(rule.getId());
					dbRule.setDescription(rule.getDescription());
					dbRule.setMessagePassed(rule.getMessage(com.datamelt.rules.core.XmlRule.PASSED).getText());
					dbRule.setMessageFailed(rule.getMessage(com.datamelt.rules.core.XmlRule.FAILED).getText());
					
					for(int h=0;h< dbChecks.size();h++)
					{
						Check check = dbChecks.get(h);
						String checkFullName =check.getPackageName() +"." + check.getName();
						if(checkFullName.equals(rule.getCheckToExecute()))
						{
							dbRule.setCheck(check);
							break;
						}
					}
					dbRule.setExpectedValue(rule.getExpectedValueRule());
					if(rule.getExpectedValueRule()!=null && !rule.getExpectedValueRule().equals(""))
					{
						for(int h=0;h< dbTypes.size();h++)
						{
							Type type = dbTypes.get(h);
							if(type.getName().equals(rule.getExpectedValueRuleType()))
							{
								dbRule.setExpectedValueType(type);
								break;
							}
						}
					}
					if(rule.getRuleObjects().size()>0)
					{
						dbRule.setObject1Classname(rule.getRuleObjects().get(0).getClassName());
						dbRule.setObject1Methodname(rule.getRuleObjects().get(0).getMethodName());
						for(int h=0;h< dbTypes.size();h++)
						{
							Type type = dbTypes.get(h);
							if(type.getName().equals(rule.getRuleObjects().get(0).getParameterType()))
							{
								dbRule.setObject1Parametertype(type);
								dbRule.setObject1Parameter(rule.getRuleObjects().get(0).getParameter());
								break;
							}
						}
						for(int h=0;h< dbTypes.size();h++)
						{
							Type type = dbTypes.get(h);
							if(type.getName().equals(rule.getRuleObjects().get(0).getMethodReturnType()))
							{
								dbRule.setObject1Type(type);
								break;
							}
						}
					}
					if(rule.getRuleObjects().size()>1)
					{
						dbRule.setObject2Classname(rule.getRuleObjects().get(1).getClassName());
						dbRule.setObject2Methodname(rule.getRuleObjects().get(1).getMethodName());
						for(int h=0;h< dbTypes.size();h++)
						{
							Type type = dbTypes.get(h);
							if(type.getName().equals(rule.getRuleObjects().get(1).getParameterType()))
							{
								dbRule.setObject2Parametertype(type);
								dbRule.setObject2Parameter(rule.getRuleObjects().get(1).getParameter());
								break;
							}
						}
						for(int h=0;h< dbTypes.size();h++)
						{
							Type type = dbTypes.get(h);
							if(type.getName().equals(rule.getRuleObjects().get(1).getMethodReturnType()))
							{
								dbRule.setObject2Type(type);
								break;
							}
						}
					}
					
					dbRule.setLastUpdateUser(lastUpdateUser);
					dbRule.setConnection(getConnection());
					dbRule.insert(getConnection().getPreparedStatement(Rule.INSERT_SQL),this,user);
				}
			}
		}
	}
	
	public Group getGroup()
    {
        return group;
    }
	
	public void setGroup(Group group)
	{
		this.group = group;
	}

	public int getPrivateProject()
	{
		return privateProject;
	}

	public void setPrivateProject(int privateProject)
	{
		this.privateProject = privateProject;
	}
	
}
