
package com.datamelt.web.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

import com.datamelt.db.MySqlConnection;
import com.datamelt.plugin.BeanshellPlugin;

import bsh.Interpreter;

public abstract class Action  
{
    private MySqlConnection connection;
    public static final String DEFAULT_VELOCITY_OBJECT = "dbObject";
    
    public abstract String execute(HttpServletRequest request, HttpServletResponse response, Context context, Interpreter interpreter,String scriptName, ArrayList<BeanshellPlugin> plugins );
    
	public String adjustInvalidCharacters(String value)
	{
	    String adjustedString="";
	    if(value!=null)
		{
	        adjustedString = value.replaceAll("'", "\'\'");
	        adjustedString = adjustedString.replaceAll("\"", "\"\"");
		}
	    return adjustedString;
	}
	
	public void setConnection(MySqlConnection con)
	{
	    this.connection = con;
	}
	
	protected MySqlConnection getConnection()
	{
	    return connection;
	}

}

