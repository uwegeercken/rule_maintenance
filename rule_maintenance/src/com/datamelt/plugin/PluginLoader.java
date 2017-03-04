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
