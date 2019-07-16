/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */ 
package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.datamelt.db.DatabaseRecord;
import com.datamelt.db.Loadable;


public class CheckMethod extends DatabaseRecord implements Loadable
{
	private long checkId;
	private String compare;
	private String compareTo;
	private String note;
	private String parameter1;
	private String parameter1Explanation;
	private String parameter2;
	private String parameter2Explanation;
	private String parameter3;
	private String parameter3Explanation;
	
	private static final String TABLENAME="check_method";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (check_id, compare, compare_to, note, parameter1, parameter1_explanation, parameter2, parameter2_explanation, parameter3, parameter3_explanation) values (?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set check_id=?, compare=?, compare_to=?, note=?, parameter1=?, parameter1_explanation=?, parameter2=?, parameter2_explanation=?, parameter3=?, parameter3_explanation=? where id =?";
    public static final String EXIST_SQL  = "select id from " + TABLENAME + "  where name =?";
    public static final String EXIST_METHOD_SQL  = "select id from " + TABLENAME + "  where check_id=? and compare=?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";

	public CheckMethod()
	{
		
	}
	
	public CheckMethod(long id)
	{
		this.setId(id);
	}
	
	
	public void load() throws Exception
	{
        ResultSet rs = selectRecordById(getConnection().getPreparedStatement(SELECT_SQL));
		if(rs.next())
		{
	        this.checkId = rs.getLong("check_id");
			this.compare = rs.getString("compare");
	        this.compareTo = rs.getString("compare_to");
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
		p.setLong(1,checkId);
		p.setString(2,compare);
		p.setString(3,compareTo);
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
		p.setLong(1,checkId);
		p.setString(2,compare);
		p.setString(3,compareTo);
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
	
	public boolean existMethod() throws Exception
    {
		String existSql = EXIST_METHOD_SQL;
		
        if(compareTo != null)
        {
        	existSql = existSql + " and compare_to = ?";
        }
        else
        {
        	existSql = existSql + " and compare_to is ?";
        }
        if(parameter1 != null)
        {
        	existSql = existSql + " and parameter1 = ?";
        }
        else
        {
        	existSql = existSql + " and parameter1 is ?";
        }
        if(parameter2 != null)
        {
        	existSql = existSql + " and parameter2 = ?";
        }
        else
        {
        	existSql = existSql + " and parameter2 is ?";
        }
        if(parameter3 != null)
        {
        	existSql = existSql + " and parameter3 = ?";
        }
        else
        {
        	existSql = existSql + " and parameter3 is ?";
        }
        
		ResultSet rs = selectExistMethod(getConnection().getPreparedStatement(existSql));
		boolean exists=false;
        if(rs.next())
		{
        	this.setId(rs.getLong("id"));
	        exists = true;
		}
        rs.close();
        return exists;
    }

	private ResultSet selectExistMethod(PreparedStatement p) throws Exception
	{
		p.setLong(1,checkId);
		p.setString(2,compare);
		p.setString(3, compareTo);
		p.setString(4, parameter1);
		p.setString(5, parameter2);
		p.setString(6, parameter3);
		return p.executeQuery();
	}
	
	public String getCompare()
	{
		return compare;
	}

	public void setCompare(String compare) 
	{
		this.compare = compare;
	}

	public String getCompareTo() 
	{
		return compareTo;
	}

	public void setCompareTo(String compareTo) 
	{
		this.compareTo = compareTo;
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

	public long getCheckId() 
	{
		return checkId;
	}

	public void setCheckId(long checkId) 
	{
		this.checkId = checkId;
	}


}
