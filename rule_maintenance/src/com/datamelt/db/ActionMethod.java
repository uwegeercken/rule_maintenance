package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class ActionMethod extends DatabaseRecord implements Loadable
{
	private long actionId;
	private String returnType;
	private String valueType;
	private String note;
	private String parameter1;
	private String parameter1Explanation;
	private String parameter2;
	private String parameter2Explanation;
	private String parameter3;
	private String parameter3Explanation;
	
	private static final String TABLENAME="action_method";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " +(action_id, return_type, value, note, parameter1, parameter1_explanation, parameter2, parameter2_explanation, parameter3, parameter3_explanation) values (?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " + set check_id=?, return_type=?, value=?, note=?, parameter1=?, parameter1_explanation=?, parameter2=?, parameter2_explanation=?, parameter3=?, parameter3_explanation=? where id =?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	
	public ActionMethod()
	{
		
	}
	
	public ActionMethod(long id)
	{
		this.setId(id);
	}
	
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.actionId = rs.getLong("action_id");
			this.returnType = rs.getString("return_type");
	        this.valueType = rs.getString("value_type");
	        this.note = rs.getString("note");
	        this.parameter1 = rs.getString("parameter1");
	        this.parameter1Explanation = rs.getString("parameter1_explanation");
	        this.parameter2 = rs.getString("parameter2");
	        this.parameter2Explanation = rs.getString("parameter2_explanation");
	        this.parameter3 = rs.getString("parameter3");
	        this.parameter3Explanation = rs.getString("parameter3_explanation");
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
	}
	
	private ResultSet selectRecordById(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}

	public void update(PreparedStatement p) throws Exception
	{
		p.setLong(1,actionId);
		p.setString(2,returnType);
		p.setString(3,valueType);
		p.setString(4,note);
		p.setString(5,parameter1);
		p.setString(6,parameter1Explanation);
		p.setString(7,parameter2);
		p.setString(8,parameter2Explanation);
		p.setString(9,parameter3);
		p.setString(10,parameter3Explanation);
		
		p.setLong(11,getId());

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
		p.setLong(1,actionId);
		p.setString(2,returnType);
		p.setString(3,valueType);
		p.setString(4,note);
		p.setString(5,parameter1);
		p.setString(6,parameter1Explanation);
		p.setString(7,parameter2);
		p.setString(8,parameter2Explanation);
		p.setString(9,parameter3);
		p.setString(10,parameter3Explanation);
		
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
	
	public long getActionId() 
	{
		return actionId;
	}

	public void setActionId(long actionId) 
	{
		this.actionId = actionId;
	}

	public String getReturnType() 
	{
		return returnType;
	}

	public void setReturnType(String returnType) 
	{
		this.returnType = returnType;
	}

	public String getValueType() 
	{
		return valueType;
	}

	public void setValueType(String valueType) 
	{
		this.valueType = valueType;
	}

	public String getNote() 
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getParameter1()
	{
		return parameter1;
	}

	public void setParameter1(String parameter1) 
	{
		this.parameter1 = parameter1;
	}

	public String getParameter1Explanation() 
	{
		return parameter1Explanation;
	}

	public void setParameter1Explanation(String parameter1Explanation) 
	{
		this.parameter1Explanation = parameter1Explanation;
	}
	
	public String getParameter2()
	{
		return parameter2;
	}

	public void setParameter2(String parameter2) 
	{
		this.parameter2 = parameter2;
	}

	public String getParameter2Explanation() 
	{
		return parameter2Explanation;
	}

	public void setParameter2Explanation(String parameter2Explanation) 
	{
		this.parameter2Explanation = parameter2Explanation;
	}
	
	public String getParameter3()
	{
		return parameter3;
	}

	public void setParameter3(String parameter3) 
	{
		this.parameter3 = parameter3;
	}

	public String getParameter3Explanation() 
	{
		return parameter3Explanation;
	}

	public void setParameter3Explanation(String parameter3Explanation) 
	{
		this.parameter3Explanation = parameter3Explanation;
	}


}
