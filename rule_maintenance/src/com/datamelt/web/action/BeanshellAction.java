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
package com.datamelt.web.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

import com.datamelt.db.User;
import com.datamelt.plugin.BeanshellPlugin;
import com.datamelt.plugin.PluginLoader;
import com.datamelt.util.Message;
import com.datamelt.web.ConstantsWeb;
import com.datamelt.web.Controller;

import bsh.*;

public class BeanshellAction extends Action  
{
    public String execute(HttpServletRequest request, HttpServletResponse response, Context context,Interpreter interpreter, String scriptName, ArrayList<BeanshellPlugin> plugins)
	{
        String template= null;
        
        try
        {
            User user = (User)request.getSession().getAttribute("user");

            interpreter.unset("templatename");
            interpreter.unset("adminaction");
            interpreter.unset("writeaction");
            interpreter.unset("plugins");
            interpreter.unset("buffer");

            interpreter.set("hostconnectionok", getHostConnectionOk());
            interpreter.set("databaseconnectionok", getDatabaseConnectionOk());
            context.put("hostconnectionok", getHostConnectionOk());
            context.put("databaseconnectionok", getDatabaseConnectionOk());
            
            interpreter.set("user", request.getSession().getAttribute("user"));
            interpreter.set("context",context);
            interpreter.set("request",request);
            interpreter.set("response",response);
            interpreter.set("language",Controller.getLanguage());
           	interpreter.set("ldap",Controller.getLdap());
           	context.put("ldap",Controller.getLdap());
           	
            interpreter.set("connection",getConnection());
            interpreter.set("scriptspath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.SCRIPTS_PATH) + "/" +  Controller.getLanguage());
            interpreter.set("templatespath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.TEMPLATES_PATH) + "/" +  Controller.getLanguage());
            interpreter.set("uploadspath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.UPLOADS_PATH));
            interpreter.set("imagespath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.IMAGES_PATH));
            interpreter.set("menupath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.MENU_PATH) + "/" +  Controller.getLanguage());
            interpreter.set("contextpath",Controller.getProperty(Controller.CONTEXT_PATH));
            interpreter.set("configfile",ConstantsWeb.CONFIG_FILE);
            interpreter.set("databasefile",ConstantsWeb.DATABASE_FILE);
            
            Message message = new Message();
            interpreter.set("infomessage",message); 
            
            interpreter.source(Controller.getProperty(Controller.CONTEXT_PATH)+ "/"+ Controller.getProperty(Controller.SCRIPTS_PATH) + "/" +  Controller.getLanguage() + "/" + scriptName + Controller.SCRIPT_EXTENSION);
            
            if(request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid() && !scriptName.equals(ConstantsWeb.CONFIG_SCRIPT))
            {
            	template = "login.vm";
            }
            else if(checkAdminAccessOk(interpreter,user))
            {
                template = (String)interpreter.get("templatename");
                
                if(template==null)
                {
                    throw new Exception("template is undefined in: "+scriptName + Controller.SCRIPT_EXTENSION);
                }
            }
            else if(!checkAdminAccessOk(interpreter,user))
            {
                throw new Exception("error: the requested action may only be executed by a person who is in the Admin group.");
            }
            
            // add plugins as specified in script if not already added globally (controller)
            if(!Controller.getProperty(Controller.AUTO_ADD_PLUGINS_WEBINF_ATTRIBUTE).equals("true"))
            {
                addPlugins(request, response, context, interpreter, plugins);
            }
            if(message.getText()!=null)
            {
                context.put("infomessage",message);
            }
        }
        catch(Exception ex)
        {
            context.put("error_cause",ex);
            return ConstantsWeb.PAGE_ERROR;
        }
        
        return template;
	}
    
    private boolean checkAdminAccessOk(Interpreter interpreter, User user) throws Exception
    {
        String adminAction = (String)interpreter.get("adminaction");
        boolean isAdminAction = adminAction!=null && adminAction.equals("true");
        if(isAdminAction)
        {
            if(user!=null && user.isInGroup(User.ADMINISTRATOR))
            {
                return true;
            }
            else
            {
                return false;
            }
            
        }
        else
        {
            return true;
        }
    }
    
    private void addPlugins(HttpServletRequest request, HttpServletResponse response, Context context,Interpreter interpreter, ArrayList<BeanshellPlugin> plugins)throws Exception
    {
        String toBeUsedPlugins = (String)interpreter.get("plugins");
        if(toBeUsedPlugins!=null)
        {
            String pluginNames[]= toBeUsedPlugins.split(",");
	        PluginLoader loader = new PluginLoader();
	        loader.setPlugins(Controller.getPlugins());
	        for(int i=0;i<pluginNames.length;i++)
	        {
	            String pluginName = pluginNames[i].trim();
	            if(!pluginName.endsWith(Controller.SCRIPT_EXTENSION))
	            {
	                pluginName = pluginName + Controller.SCRIPT_EXTENSION;
	            }
	            loader.processPlugin(pluginName,context,interpreter, getConnection(),request,response);
	        }
        }
    }
}

