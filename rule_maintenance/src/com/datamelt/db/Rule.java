package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class Rule extends DatabaseRecord implements Loadable
{
	private String name;
	private String description;
	private long rulesubgroupId;
	private Type object1Parametertype = new Type();
	private String object1Parameter;
	private Type object1Type = new Type();
	private Type object2Parametertype = new Type();
	private String object2Parameter;
	private Type object2Type = new Type();
	private String expectedValue;
	private Type expectedValueType = new Type();
	private String additionalParameter;
	private Type additionalParameterType = new Type();
	private String messagePassed;
	private String messageFailed;
	private Check check = new Check();
	private User lastUpdateUser = new User();

	
	public static final String TABLENAME="rule";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	private static final String SELECT_BY_NAME_SQL="select * from " + TABLENAME + " where name=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (rulesubgroup_id, name, description, check_id, object1_parametertype_id,object1_parameter,object1_type_id, object2_parametertype_id,object2_parameter,object2_type_id,expectedvalue,expectedvalue_type_id,additional_parameter,additional_parameter_type_id,message_passed,message_failed, last_update_user_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set rulesubgroup_id=?, name=?, description=?, check_id=?, object1_parametertype_id=?,object1_parameter=?,object1_type_id=?, object2_parametertype_id=?,object2_parameter=?,object2_type_id=?,expectedvalue=?,expectedvalue_type_id=?,additional_parameter=?,additional_parameter_type_id=?, message_passed=?,message_failed=?, last_update_user_id=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + "  where name =? and rulesubgroup_id=?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	
	public Rule()
	{
		
	}
	
	public Rule(long id)
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
	        this.rulesubgroupId = rs.getLong("rulesubgroup_id");

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

	        this.expectedValue = rs.getString("expectedvalue");

	        this.expectedValueType.setId(rs.getLong("expectedvalue_type_id"));
	        this.expectedValueType.setConnection(getConnection());
	        this.expectedValueType.load();

	        this.additionalParameter = rs.getString("additional_parameter");
	        
	        this.additionalParameterType.setId(rs.getLong("additional_parameter_type_id"));
	        this.additionalParameterType.setConnection(getConnection());
	        this.additionalParameterType.load();
	        
	        this.messageFailed = rs.getString("message_failed");
	        this.messagePassed = rs.getString("message_passed");

	        this.check.setId(rs.getLong("check_id"));
	        this.check.setConnection(getConnection());
	        this.check.load();
	        
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
	        this.rulesubgroupId = rs.getLong("rulesubgroup_id");

	        this.object1Parametertype.setId(rs.getLong("object1_parametertype"));
	        this.object1Parametertype.setConnection(getConnection());
	        this.object1Parametertype.load();
	        
	        this.object1Parameter = rs.getString("object1_parameter");
	        
	        this.object1Type.setId(rs.getLong("object1_type"));
	        this.object1Type.setConnection(getConnection());
	        this.object1Type.load();

	        this.object2Parametertype.setId(rs.getLong("object2_parametertype"));
	        this.object2Parametertype.setConnection(getConnection());
	        this.object2Parametertype.load();
	        
	        this.object2Parameter = rs.getString("object2_parameter");

	        this.object2Type.setId(rs.getLong("object2_type"));
	        this.object2Type.setConnection(getConnection());
	        this.object2Type.load();

	        this.expectedValue = rs.getString("expectedvalue");

	        this.expectedValueType.setId(rs.getLong("expectedvalue_type"));
	        this.expectedValueType.setConnection(getConnection());
	        this.expectedValueType.load();

	        this.additionalParameter = rs.getString("additional_parameter");
	        
	        this.additionalParameterType.setId(rs.getLong("additional_parameter_type_id"));
	        this.additionalParameterType.setConnection(getConnection());
	        this.additionalParameterType.load();
	        
	        this.messageFailed = rs.getString("message_failed");
	        this.messagePassed = rs.getString("message_passed");

	        
	        this.check.setId(rs.getLong("check_id"));
	        this.check.setConnection(getConnection());
	        this.check.load();
	        
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
	
	private ResultSet selectExistRule(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		p.setLong(2,rulesubgroupId);
		return p.executeQuery();
	}
	
	public void update(PreparedStatement p, Project project, User user) throws Exception
	{
		p.setLong(1, rulesubgroupId);
		p.setString(2,name);
		p.setString(3,description);
		p.setLong(4, check.getId());
		p.setLong(5, object1Parametertype.getId());
		p.setString(6,object1Parameter);
		p.setLong(7, object1Type.getId());
		p.setLong(8, object2Parametertype.getId());
		p.setString(9,object2Parameter);
		p.setLong(10, object2Type.getId());
		p.setString(11,expectedValue);
		p.setLong(12, expectedValueType.getId());
		p.setString(13,additionalParameter);
		p.setLong(14, additionalParameterType.getId());
		p.setString(15, messagePassed);
		p.setString(16, messageFailed);
		p.setLong(17, lastUpdateUser.getId());
		
		
		p.setLong(18,getId());

		try
		{
			if(user.canUpdateProject(project))
			{
				p.executeUpdate();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowed to update the rule");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void insert(PreparedStatement p, Project project, User user) throws Exception
    {
		p.setLong(1, rulesubgroupId);
		p.setString(2,name);
		p.setString(3,description);
		p.setLong(4, check.getId());
		p.setLong(5, object1Parametertype.getId());
		p.setString(6,object1Parameter);
		p.setLong(7, object1Type.getId());
		p.setLong(8, object2Parametertype.getId());
		p.setString(9,object2Parameter);
		p.setLong(10, object2Type.getId());
		p.setString(11,expectedValue);
		p.setLong(12, expectedValueType.getId());
		p.setString(13,additionalParameter);
		p.setLong(14, additionalParameterType.getId());
		p.setString(15, messagePassed);
		p.setString(16, messageFailed);
		p.setLong(17, lastUpdateUser.getId());
		
		try
		{
			if(user.canUpdateProject(project))
			{
				p.execute();
			}
			else
			{
				throw new Exception("user " + user.getUserid() + " is not allowd to insert the rule");	
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
        
        setId(getConnection().getLastInsertId());
    }
	
	public void delete(PreparedStatement p,Project project, User user) throws Exception
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
				throw new Exception("user " + user.getUserid() + " is not allowed to delete the rule");	
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean exist(String newName) throws Exception
    {
        ResultSet rs = selectExistRule(getConnection().getPreparedStatement(EXIST_SQL),newName);
		boolean exists=false;
        if(rs.next())
		{
	        exists = true;
		}
        rs.close();
        return exists;
    }
	
	public String getRuleLogic()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("field [" + object1Parameter + "] ");
		buffer.append(check.getNameDescriptive() + " ");
		if(check.getCheckSingleField()==0)
		{
			if(object2Parameter!=null && !object2Parameter.trim().equals(""))
			{
				buffer.append("field [" + object2Parameter + "] ");
			}
			else
			{
				buffer.append("value [" + expectedValue + "] ");
			}
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

	public long getRuleSubgroupId()
	{
		return rulesubgroupId;
	}

	public void setRuleSubgroupId(long rulesubgroupId)
	{
		this.rulesubgroupId = rulesubgroupId;
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

	public String getExpectedValue() 
	{
		return expectedValue;
	}

	public void setExpectedValue(String expectedValue) 
	{
		this.expectedValue = expectedValue;
	}

	public Type getExpectedValueType() 
	{
		return expectedValueType;
	}

	public void setExpectedValueType(Type expectedValueType) 
	{
		this.expectedValueType = expectedValueType;
	}

	public String getMessagePassed() 
	{
		return messagePassed;
	}

	public void setMessagePassed(String messagePassed) 
	{
		this.messagePassed = messagePassed;
	}

	public String getMessageFailed() 
	{
		return messageFailed;
	}

	public void setMessageFailed(String messageFailed) 
	{
		this.messageFailed = messageFailed;
	}

	public Check getCheck()
	{
		return check;
	}

	public void setCheck(Check check) 
	{
		this.check = check;
	}

	public long getRulesubgroupId()
	{
		return rulesubgroupId;
	}

	public void setRulesubgroupId(long rulesubgroupId) 
	{
		this.rulesubgroupId = rulesubgroupId;
	}

	public User getLastUpdateUser()
	{
		return lastUpdateUser;
	}

	public void setLastUpdateUser(User user) 
	{
		this.lastUpdateUser = user;
	}

	public String getAdditionalParameter()
	{
		return additionalParameter;
	}

	public void setAdditionalParameter(String additionalParameter)
	{
		this.additionalParameter = additionalParameter;
	}

	public Type getAdditionalParameterType() 
	{
		return additionalParameterType;
	}

	public void setAdditionalParameterType(Type additionalParameterType) 
	{
		this.additionalParameterType = additionalParameterType;
	}


}
