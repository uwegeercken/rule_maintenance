/*
 *
 * all code by uwe geercken - 2014
 */
package com.datamelt.plugin;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

import bsh.Interpreter;

import com.datamelt.db.MySqlConnection;
import com.datamelt.web.ConstantsWeb;
import com.datamelt.web.Controller;

public class PluginLoader
{
    private ArrayList<BeanshellPlugin> plugins = new ArrayList<BeanshellPlugin>();
    
    public void loadPlugins(String path) throws Exception
    {
        String base = Controller.getProperty(Controller.CONTEXT_PATH);
        
        File dir = new File(base +  path);
        File files[] = dir.listFiles();
        if(files!=null)
        {
	        for(int i=0;i<files.length;i++) 
	        {
	            if(files[i].getName().endsWith(ConstantsWeb.SCRIPTS_EXTENSION))
	            {
	                BeanshellPlugin plugin = new BeanshellPlugin(files[i].getName());
	                addPlugin(plugin);
	            }
	            
	        }
        }
        
    }
    
    public void processPlugins(Context context, Interpreter interpreter,MySqlConnection connection,HttpServletRequest request,HttpServletResponse response)
    {
        if(plugins!=null)
        {
	        for(int i=0;i<plugins.size();i++)
	        {
	            BeanshellPlugin p = (BeanshellPlugin)plugins.get(i);
	            p.process(context,interpreter, connection, request, response);
	        }
        }
    }
    
    public void processPlugin(String name, Context context, Interpreter interpreter,MySqlConnection connection,HttpServletRequest request,HttpServletResponse response)
    {
        if(plugins!=null)
        {
	        for(int i=0;i<plugins.size();i++)
	        {
	            BeanshellPlugin p = (BeanshellPlugin)plugins.get(i);
	            if(p.getPluginName().equals(name))
	            {
	                p.process(context,interpreter, connection, request, response);
	            }
	        }
        }
    }
    
    public ArrayList <BeanshellPlugin>getPlugins()
    {
        return plugins;
    }
    
    public void setPlugins(ArrayList <BeanshellPlugin>plugins)
    {
        this.plugins = plugins;
    }
    
    private void addPlugin(BeanshellPlugin plugin) throws Exception
    {
        plugins.add(plugin);
    }
    
}
