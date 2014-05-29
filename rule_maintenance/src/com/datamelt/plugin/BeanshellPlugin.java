
package com.datamelt.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

import com.datamelt.db.MySqlConnection;
import com.datamelt.web.Controller;

import bsh.*;

public class BeanshellPlugin 
{
    public String pluginName;
    
    public BeanshellPlugin(String name)
    {
        pluginName = name;
    }
    
    public void process(Context context,Interpreter interpreter, MySqlConnection connection,HttpServletRequest request,HttpServletResponse response)
	{
        try
        {
            interpreter.set("context",context);
            interpreter.set("connection",connection);
            interpreter.set("request",request);
            interpreter.set("response",response);
            
            String path = Controller.getProperty(Controller.CONTEXT_PATH)+ "plugins/";
            interpreter.source(path + pluginName);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
	}
    
    public String getPluginName()
    {
        return pluginName ;
        
    }
}

