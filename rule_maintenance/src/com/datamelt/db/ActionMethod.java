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


public class ActionMethod extends DatabaseRecord implements Loadable
{
	private long actionId;
	private String returnType;
	private String methodTypes;
	private String note;
	private String optionalType1;
	private String optionalType1Explanation;
	private String optionalType2;
	private String optionalType2Explanation;
	private String optionalType3;
	private String optionalType3Explanation;
	
	private static final String TABLENAME="action_method";
	private static final String SELECT_SQL="select * from " + TABLENAME + " where id=?";
	
	public static final String INSERT_SQL = "insert into " + TABLENAME + " (action_id, return_type, method_types, note, optional_type1, optional_type1_explanation, optional_type2, optional_type2_explanation, optional_type3, optional_type3_explanation) values (?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_SQL = "update " + TABLENAME + " set action_id=?, return_type=?, method_types=?, note=?, optional_type1=?, optional_type1_explanation=?, optional_type2=?, optional_type2_explanation=?, optional_type3=?, optional_type3_explanation=? where id =?";
    public static final String DELETE_SQL = "delete from " + TABLENAME + " where id=?";
    public static final String EXIST_METHOD_SQL  = "select id from " + TABLENAME + "  where return_type=? and method_types=? and action_id=?";

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
	        this.methodTypes = rs.getString("method_types");
	        this.note = rs.getString("note");
	        this.optionalType1 = rs.getString("optional_type1");
	        this.optionalType1Explanation = rs.getString("optional_type1_explanation");
	        this.optionalType2 = rs.getString("optional_type2");
	        this.optionalType2Explanation = rs.getString("optional_type2_explanation");
	        this.optionalType3 = rs.getString("optional_type3");
	        this.optionalType3Explanation = rs.getString("optional_type3_explanation");
	        
	        setLastUpdate(rs.getString("last_update"));
	        
		}
        rs.close();
	}
	
	private ResultSet selectExistMethod(PreparedStatement p) throws Exception
	{
		p.setString(1,returnType);
		p.setString(2,methodTypes);
		p.setLong(3, actionId);
		return p.executeQuery();
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
		p.setString(3,methodTypes);
		p.setString(4,note);
		p.setString(5,optionalType1);
		p.setString(6,optionalType1Explanation);
		p.setString(7,optionalType2);
		p.setString(8,optionalType2Explanation);
		p.setString(9,optionalType3);
		p.setString(10,optionalType3Explanation);
		
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
		p.setString(3,methodTypes);
		p.setString(4,note);
		p.setString(5,optionalType1);
		p.setString(6,optionalType1Explanation);
		p.setString(7,optionalType2);
		p.setString(8,optionalType2Explanation);
		p.setString(9,optionalType3);
		p.setString(10,optionalType3Explanation);
		
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
        ResultSet rs = selectExistMethod(getConnection().getPreparedStatement(EXIST_METHOD_SQL));
		boolean exists=false;
        if(rs.next())
		{
        	this.setId(rs.getLong("id"));
	        exists = true;
		}
        rs.close();
        return exists;
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

	public String getMethodTypes() 
	{
		return methodTypes;
	}

	public void setMethodTypes(String methodTypes) 
	{
		this.methodTypes = methodTypes;
	}

	public String getNote() 
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getOptionalType1()
	{
		return optionalType1;
	}

	public void setOptionalType1(String optionalType1) 
	{
		this.optionalType1 = optionalType1;
	}

	public String getOptionalType1Explanation() 
	{
		return optionalType1Explanation;
	}

	public void setOptionalType1Explanation(String optionalType1Explanation) 
	{
		this.optionalType1Explanation = optionalType1Explanation;
	}
	
	public String getOptionalType2()
	{
		return optionalType2;
	}

	public void setOptionalType2(String optionalType2) 
	{
		this.optionalType2 = optionalType2;
	}

	public String getOptionalType2Explanation() 
	{
		return optionalType2Explanation;
	}

	public void setOptionalType2Explanation(String optionalType2Explanation) 
	{
		this.optionalType2Explanation = optionalType2Explanation;
	}
	
	public String getOptionalType3()
	{
		return optionalType3;
	}

	public void setOptionalType3(String optionalType3) 
	{
		this.optionalType3 = optionalType3;
	}

	public String getOptionalType3Explanation() 
	{
		return optionalType3Explanation;
	}

	public void setOptionalType3Explanation(String optionalType3Explanation) 
	{
		this.optionalType3Explanation = optionalType3Explanation;
	}


}
