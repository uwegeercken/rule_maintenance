
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

            interpreter.set("user", request.getSession().getAttribute("user"));
            interpreter.set("context",context);
            interpreter.set("request",request);
            interpreter.set("response",response);
            interpreter.set("language",Controller.getLanguage());
            
            interpreter.set("connection",getConnection());
            interpreter.set("scriptspath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.SCRIPTS_PATH) + "/" +  Controller.getLanguage());
            interpreter.set("templatespath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.TEMPLATES_PATH) + "/" +  Controller.getLanguage());
            interpreter.set("menupath",Controller.getProperty(Controller.CONTEXT_PATH) + Controller.getProperty(Controller.MENU_PATH) + "/" +  Controller.getLanguage());
            interpreter.set("contextpath",Controller.getProperty(Controller.CONTEXT_PATH));
            
            Message message = new Message();
            interpreter.set("infomessage",message); 
            
            interpreter.source(Controller.getProperty(Controller.CONTEXT_PATH)+ Controller.getProperty(Controller.SCRIPTS_PATH) + "/" +  Controller.getLanguage() + "/" + scriptName + Controller.SCRIPT_EXTENSION);

            if(checkAdminAccessOk(interpreter,user))
            {
                template = (String)interpreter.get("templatename");
                
                if(template==null)
                {
                    throw new Exception("template is undefined in: "+scriptName + Controller.SCRIPT_EXTENSION);
                }
            }
            else
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

