package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class RuleGroupAction extends DatabaseRecord implements Loadable
{
	private String name;
	private String description;
	private long rulegroupId;
	private Type object1Parametertype = new Type();
	private String object1Parameter;
	private Type object1Type = new Type();
	private Type object2Parametertype = new Type();
	private String object2Parameter;
	private Type object2Type = new Type();
	private String parameter1;
	private Type parameter1Type = new Type();
	private String parameter2;
	private Type parameter2Type = new Type();
	private String parameter3;
	private Type parameter3Type = new Type();
	private String executeIf;
	
	private Action action = new Action();
	private User lastUpdateUser = new User();
	
	public static final String TABLENAME="rulegroupaction";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	private static final String SELECT_BY_NAME_SQL="select * from " + TABLENAME + " where name=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (rulegroup_id, action_id, name, description, object1_parametertype_id,object1_parameter,object1_type_id, object2_parametertype_id,object2_parameter,object2_type_id,parameter1,parameter1_type_id,parameter2,parameter2_type_id,parameter3,parameter3_type_id,execute_if, last_update_user_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set rulegroup_id=?, action_id=?, name=?, description=?, object1_parametertype_id=?,object1_parameter=?,object1_type_id=?, object2_parametertype_id=?,object2_parameter=?,object2_type_id=?,parameter1=?,parameter1_type_id=?,parameter2=?,parameter2_type_id=?,parameter3=?,parameter3_type_id=?, execute_if=?, last_update_user_id=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + "  where name =? and rulegroup_id=?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	
	public RuleGroupAction()
	{
		
	}
	
	public RuleGroupAction(long id)
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
	        
	        this.action.setId(rs.getLong("action_id"));
	        this.action.setConnection(getConnection());
	        this.action.load();
	        
	        this.object1Parametertype.setId(rs.getLong("object1_parametertype_id"));
	        this.object1Parametertype.setConnection(getConnection());
	        this.object1Parametertype.load();
	        
	        this.object1Parameter = rs.getString("object1_parameter");
	        
	        this.object1Type.setId(rs.getLong("object1_type_id"));
	        this.object1Type.setConnection(getConnection());
	        this.object1Type.load();

	        this.object2Parametertype.setId(rs.getLong("object2_parametertype_id"));
	        this.object2Parametertype.setConnection(getConnection());
	        this.object2Parametertype.load();
	        
	        this.object2Parameter = rs.getString("object2_parameter");

	        this.object2Type.setId(rs.getLong("object2_type_id"));
	        this.object2Type.setConnection(getConnection());
	        this.object2Type.load();

	        this.parameter1 = rs.getString("parameter1");
	        this.parameter1Type.setId(rs.getLong("parameter1_type_id"));
	        this.parameter1Type.setConnection(getConnection());
	        this.parameter1Type.load();
	        
	        this.parameter2 = rs.getString("parameter2");
	        this.parameter2Type.setId(rs.getLong("parameter2_type_id"));
	        this.parameter2Type.setConnection(getConnection());
	        this.parameter2Type.load();
	        
	        this.parameter3 = rs.getString("parameter3");
	        this.parameter3Type.setId(rs.getLong("parameter3_type_id"));
	        this.parameter3Type.setConnection(getConnection());
	        this.parameter3Type.load();
	        
	        this.executeIf = rs.getString("execute_if");
	        	        
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
	        this.rulegroupId = rs.getLong("rulegroup_id");

	        this.action.setId(rs.getLong("action_id"));
	        this.action.setConnection(getConnection());
	        this.action.load();
	        
	        this.object1Parametertype.setId(rs.getLong("object1_parametertype_id"));
	        this.object1Parametertype.setConnection(getConnection());
	        this.object1Parametertype.load();
	        
	        this.object1Parameter = rs.getString("object1_parameter");
	        
	        this.object1Type.setId(rs.getLong("object1_type_id"));
	        this.object1Type.setConnection(getConnection());
	        this.object1Type.load();

	        this.object2Parametertype.setId(rs.getLong("object2_parametertype_id"));
	        this.object2Parametertype.setConnection(getConnection());
	        this.object2Parametertype.load();
	        
	        this.object2Parameter = rs.getString("object2_parameter");

	        this.object2Type.setId(rs.getLong("object2_type_id"));
	        this.object2Type.setConnection(getConnection());
	        this.object2Type.load();

	        this.parameter1 = rs.getString("parameter1");
	        this.parameter1Type.setId(rs.getLong("parameter1_type_id"));
	        this.parameter1Type.setConnection(getConnection());
	        this.parameter1Type.load();
	        
	        this.parameter2 = rs.getString("parameter2");
	        this.parameter2Type.setId(rs.getLong("parameter2_type_id"));
	        this.parameter2Type.setConnection(getConnection());
	        this.parameter2Type.load();
	        
	        this.parameter3 = rs.getString("parameter3");
	        this.parameter3Type.setId(rs.getLong("parameter3_type_id"));
	        this.parameter3Type.setConnection(getConnection());
	        this.parameter3Type.load();
	        
	        this.executeIf = rs.getString("execute_if");
	        
	        this.lastUpdateUser.setId(rs.getLong("last_update_user_id"));
	        this.lastUpdateUser.setConnection(getConnection());
	        this.lastUpdateUser.load();
	        
	        setLastUpdate(rs.getString("last_update"));
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
	
	private ResultSet selectExistRuleGroupAction(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		p.setLong(2,rulegroupId);
		return p.executeQuery();
	}
	
	public void update(PreparedStatement p, Project project, User user) throws Exception
	{
		p.setLong(1, rulegroupId);
		p.setLong(2, action.getId());
		p.setString(3,name);
		p.setString(4,description);
		p.setLong(5, object1Parametertype.getId());
		p.setString(6,object1Parameter);
		p.setLong(7, object1Type.getId());
		p.setLong(8, object2Parametertype.getId());
		p.setString(9,object2Parameter);
		p.setLong(10, object2Type.getId());
		p.setString(11,parameter1);
		p.setLong(12, parameter1Type.getId());
		p.setString(13,parameter2);
		p.setLong(14, parameter2Type.getId());
		p.setString(15,parameter3);
		p.setLong(16, parameter3Type.getId());
		p.setString(17,executeIf);
		p.setLong(18, lastUpdateUser.getId());
		
		p.setLong(19,getId());

		try
		{
			if(user.canUpdateProject(project))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to update the action");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement p, Project project, User user) throws Exception
    {
		p.setLong(1, rulegroupId);
		p.setLong(2, action.getId());
		p.setString(3,name);
		p.setString(4,description);
		p.setLong(5, object1Parametertype.getId());
		p.setString(6,object1Parameter);
		p.setLong(7, object1Type.getId());
		p.setLong(8, object2Parametertype.getId());
		p.setString(9,object2Parameter);
		p.setLong(10, object2Type.getId());
		p.setString(11,parameter1);
		p.setLong(12, parameter1Type.getId());
		p.setString(13,parameter2);
		p.setLong(14, parameter2Type.getId());
		p.setString(15,parameter3);
		p.setLong(16, parameter3Type.getId());
		p.setString(17,executeIf);
		p.setLong(18, lastUpdateUser.getId());
		
		try
		{
			if(user.canUpdateProject(project))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to insert the action");	
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
				throw new Exception("user " + user.getUserid() + " is not allowed to delete the action");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean exist(String newName) throws Exception
    {
        ResultSet rs = selectExistRuleGroupAction(getConnection().getPreparedStatement(EXIST_SQL),newName);
		boolean exists=false;
        if(rs.next())
		{
	        exists = true;
		}
        rs.close();
        return exists;
    }
	
	public String getActionLogic()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("update field [" + object2Parameter + "] from: ");
		buffer.append(action.getMethoddisplayname() + " ");
		if(object1Parameter!=null && !object1Parameter.trim().equals(""))
		{
			buffer.append("field [" + object1Parameter + "] ");
		}
		if(parameter1!=null && !parameter1.equals(""))
		{
			buffer.append("[" + parameter1 + "] ");
		}
		if(parameter2!=null && !parameter2.equals(""))
		{
			buffer.append(" [" + parameter2 + "] ");
		}
		if(parameter3!=null && !parameter3.equals(""))
		{
			buffer.append(" [" + parameter3 + "] ");
		}


		return buffer.toString();
		
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

	public long getRuleGroupId()
	{
		return rulegroupId;
	}

	public void setRuleGroupId(long rulegroupId)
	{
		this.rulegroupId = rulegroupId;
	}

	public Type getObject1Parametertype() 
	{
		return object1Parametertype;
	}

	public void setObject1Parametertype(Type object1Parametertype) 
	{
		this.object1Parametertype = object1Parametertype;
	}

	public String getObject1Parameter() 
	{
		return object1Parameter;
	}

	public void setObject1Parameter(String object1Parameter) 
	{
		this.object1Parameter = object1Parameter;
	}

	public Type getObject1Type() 
	{
		return object1Type;
	}

	public void setObject1Type(Type object1Type) 
	{
		this.object1Type = object1Type;
	}

	public Type getObject2Parametertype() 
	{
		return object2Parametertype;
	}

	public void setObject2Parametertype(Type object2Parametertype) 
	{
		this.object2Parametertype = object2Parametertype;
	}

	public String getObject2Parameter() 
	{
		return object2Parameter;
	}

	public void setObject2Parameter(String object2Parameter) 
	{
		this.object2Parameter = object2Parameter;
	}

	public Type getObject2Type() 
	{
		return object2Type;
	}

	public void setObject2Type(Type object2Type) 
	{
		this.object2Type = object2Type;
	}

	public String getParameter1()
	{
		return parameter1;
	}

	public void setParameter1(String parameter1) 
	{
		this.parameter1 = parameter1;
	}

	public Type getParameter1Type() 
	{
		return parameter1Type;
	}

	public void setParameter1Type(Type parameter1Type) 
	{
		this.parameter1Type = parameter1Type;
	}

	public String getParameter2() 
	{
		return parameter2;
	}

	public void setParameter2(String parameter2) 
	{
		this.parameter2 = parameter2;
	}

	public Type getParameter2Type() 
	{
		return parameter2Type;
	}

	public void setParameter2Type(Type parameter2Type) 
	{
		this.parameter2Type = parameter2Type;
	}

	public String getParameter3() 
	{
		return parameter3;
	}

	public void setParameter3(String parameter3) 
	{
		this.parameter3 = parameter3;
	}

	public Type getParameter3Type() 
	{
		return parameter3Type;
	}

	public void setParameter3Type(Type parameter3Type) 
	{
		this.parameter3Type = parameter3Type;
	}

	public long getRulegroupId() 
	{
		return rulegroupId;
	}

	public void setRulegroupId(long rulegroupId) 
	{
		this.rulegroupId = rulegroupId;
	}
	
	public Action getAction() 
	{
		return action;
	}

	public void setAction(Action action) 
	{
		this.action = action;
	}

	public String getExecuteIf() 
	{
		return executeIf;
	}

	public void setExecuteIf(String executeIf) 
	{
		this.executeIf = executeIf;
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
