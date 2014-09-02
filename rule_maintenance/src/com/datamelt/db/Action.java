package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class Action extends DatabaseRecord implements Loadable
{
	private String classname;
	private String methodname;
	private String methoddisplayname;
	
	private static final String TABLENAME="action";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " +(classname, methodname, methoddisplayname) values (?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " + set classname=?, methodname=?, methoddisplayname=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + "  where name =?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	
	public Action()
	{
		
	}
	
	public Action(long id)
	{
		this.setId(id);
	}
	
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.classname = rs.getString("classname");
	        this.methodname = rs.getString("methodname");
	        this.methoddisplayname = rs.getString("methoddisplayname");
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
	}
	
	private ResultSet selectRecordById(PreparedStatement p) throws Exception
	{
		p.setLong(1,getId());
		return p.executeQuery();
	}

	private ResultSet selectExistCheck(PreparedStatement p,String newName) throws Exception
	{
		p.setString(1,newName);
		return p.executeQuery();
	}
	
	public void update(PreparedStatement p) throws Exception
	{
		p.setString(1,classname);
		p.setString(2,methodname);
		p.setString(3,methoddisplayname);
		
		p.setLong(4,getId());

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
		p.setString(1,classname);
		p.setString(2,methodname);
		p.setString(3,methoddisplayname);
		
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
        ResultSet rs = selectExistCheck(getConnection().getPreparedStatement(EXIST_SQL),newName);
		boolean exists=false;
        if(rs.next())
		{
	        exists = true;
		}
        rs.close();
        return exists;
    }

	public String getClassname()
	{
		return classname;
	}

	public void setClassname(String classname) 
	{
		this.classname = classname;
	}

	public String getMethodname() 
	{
		return methodname;
	}

	public void setMethodname(String methodname) 
	{
		this.methodname = methodname;
	}

	public String getMethoddisplayname() 
	{
		return methoddisplayname;
	}

	public void setMethoddisplayname(String methoddisplayname) 
	{
		this.methoddisplayname = methoddisplayname;
	}
}
