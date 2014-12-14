
package com.datamelt.web;  

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;

import bsh.Interpreter;

import com.datamelt.db.MySqlConnection;
import com.datamelt.plugin.BeanshellPlugin;
import com.datamelt.plugin.PluginLoader;
import com.datamelt.util.Ldap;
import com.datamelt.web.action.Action;

public class Controller extends org.apache.velocity.tools.view.servlet.VelocityLayoutServlet 
{
    
    private static final String ACTIONS_WEBINF_ATTRIBUTE           = "actions";
    private static final String DB_USER					           = "db_user";
    private static final String DB_USERPASSWORD					   = "db_userpassword";
	private static final String DEFAULT_ACTION_NAME                = "action";
	private static final String BSH_ACTION_NAME		               = "BeanshellAction";
	private static final String ACTIONS_PACKAGE_NAME               = "com.datamelt.web.action.";
	private static final String LANGUAGE_WEBINF_ATTRIBUTE          = "language";
	private static final String DEFAULT_LANGUAGE 				   = "eng";
	private static final String DIRECTORY_MESSAGES                 = "message";
    private static final String DB_HOSTNAME					       = "db_hostname";
    private static final String DB_PORT					       	   = "db_port";
    
    private static final String LDAP_HOSTNAME					   = "ldap_hostname";
    private static final String LDAP_DOMAIN						   = "ldap_domain";
    private static final String LDAP_PORT						   = "ldap_port";
    
    private static final long serialVersionUID=300000;

	public static final String PLUGIN_PATH_WEBINF_ATTRIBUTE		  = "pluginpath";
	
	// if set to <true> then each template will automatically get all plugins
	// otherwise the required template needs to be explicitly named in the script
	public static final String AUTO_ADD_PLUGINS_WEBINF_ATTRIBUTE  = "autoaddplugins";
	
	public static final String CONTEXT_PATH				          = "contextpath";
	public static final String SCRIPT_EXTENSION					  = ".bsh";
	public static final String SCRIPTS_PATH		                  = "scriptspath";
	public static final String TEMPLATES_PATH	                  = "templatespath";
	public static final String MENU_PATH    	                  = "menupath";
	public static final String DB_NAME                       	  = "db_name";
	private static final String MESSAGE_UNDEFINED                 = "[error: message undefined]";
	private static final String MESSAGES                          = "messages";
	
	private static String language                                = "eng";
	
	private static Properties actions = new Properties();
    private static Properties properties = new Properties();
    private static Properties messages = new Properties();
    private static String dbUser;
    private static String dbUserPassword;
    private static String dbHostname;
    private static int dbPort;
    private static String dbName;
    private boolean dbConnectionOk;
    private static Ldap ldap;
    
    private static Interpreter interpreter=null;
    private static PluginLoader pluginLoader= null;
    
	public void init(ServletConfig config) throws ServletException
    {
		super.init(config);
		
		String realPath = getServletContext().getRealPath("/");
        properties.put(CONTEXT_PATH, realPath);
		
		readConfigFile(realPath);
		dbConnectionOk = databaseConnectionOk();
		
		interpreter = new Interpreter();
		
		String startupLanguage = config.getInitParameter(LANGUAGE_WEBINF_ATTRIBUTE);
	    if(startupLanguage!=null)
	    {
	        language = startupLanguage;
	    }
	    String actionsFile = config.getInitParameter(ACTIONS_WEBINF_ATTRIBUTE);

	    if ( actionsFile != null )
		{
			realPath = getServletContext().getRealPath(actionsFile);

			if ( realPath != null )
			{
				actionsFile = realPath;
			}
		}
        try
        {
            actions.load(new FileInputStream(actionsFile));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
	    String scriptsPath = config.getInitParameter(SCRIPTS_PATH);
	    if (scriptsPath!=null)
	    {
	        properties.put(SCRIPTS_PATH, scriptsPath); 
	    }
	    
	    String templatesPath = config.getInitParameter(TEMPLATES_PATH);
	    if (templatesPath!=null)
	    {
	        properties.put(TEMPLATES_PATH, templatesPath); 
	    }
	    
	    String menuPath = config.getInitParameter(MENU_PATH);
	    if (menuPath!=null)
	    {
	        properties.put(MENU_PATH, menuPath); 
	    }
	    
	    String pluginPath = config.getInitParameter(PLUGIN_PATH_WEBINF_ATTRIBUTE);
	    if (pluginPath!=null)
	    {
	        properties.put(PLUGIN_PATH_WEBINF_ATTRIBUTE, pluginPath); 
	    }
	    
	    String autoAddPlugins = config.getInitParameter(AUTO_ADD_PLUGINS_WEBINF_ATTRIBUTE);
	    if (autoAddPlugins!=null)
	    {
	        properties.put(AUTO_ADD_PLUGINS_WEBINF_ATTRIBUTE, autoAddPlugins); 
	    }
	    
	    loadMessages();
	    
	    try
	    {
	        pluginLoader = new PluginLoader();
	        pluginLoader.loadPlugins(getProperty(PLUGIN_PATH_WEBINF_ATTRIBUTE));
	    }
	    catch(Exception ex)
	    {
	        ex.printStackTrace();
	    }
    }	
	
	public void readConfigFile(String realPath)
	{
		try
		{
			FileInputStream fis = new FileInputStream(realPath + "/" + ConstantsWeb.CONFIG_FILE);
			Properties p = new Properties();
			p.load(fis);
			fis.close();
			dbHostname = p.getProperty(DB_HOSTNAME);
			dbPort = Integer.parseInt(p.getProperty(DB_PORT));
			dbName = p.getProperty(DB_NAME);
			dbUser=p.getProperty(DB_USER);
			dbUserPassword=p.getProperty(DB_USERPASSWORD);
			
			ldap = new Ldap();
	        ldap.setHost(p.getProperty(LDAP_HOSTNAME));
	        ldap.setDomain(p.getProperty(LDAP_DOMAIN));
	        if(p.getProperty(LDAP_PORT)!=null && !p.getProperty(LDAP_PORT).trim().equals(""))
	        {
	        	ldap.setPort(Integer.parseInt(p.getProperty(LDAP_PORT)));
	        }
	        else
	        {
	        	ldap.setPort(ConstantsWeb.LDAP_DEFAULT_PORT);
	        }
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean databaseConnectionOk()
	{
		try
		{
			MySqlConnection con = getConnection(dbHostname,dbPort,dbName,dbUser,dbUserPassword);
			con.close();
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
        
	}
	
	public Template handleRequest(HttpServletRequest request,HttpServletResponse response, org.apache.velocity.context.Context context ) 
    {
	    Template template= null; 
	    String actionTemplate = null;
	    String scriptName= request.getParameter("scriptname");
	    String className=null;
	    String command = request.getParameter(DEFAULT_ACTION_NAME); 
		if (command!=null)
		{
		    className= actions.getProperty(command);
		}
		Action action = null;
		if (className != null && className.trim().length() > 0)
		{
		    try
			{
			    Class<?> c = Class.forName(ACTIONS_PACKAGE_NAME + className);
		        action = (Action)c.newInstance();
		        MySqlConnection con=null;
		        
		        if(!dbConnectionOk && !scriptName.equals(ConstantsWeb.CONFIG_SCRIPT))
		        {
		        	scriptName = ConstantsWeb.SELECTCONFIG_SCRIPT;
		        }
		        else if(dbConnectionOk && !scriptName.equals(ConstantsWeb.CONFIG_SCRIPT))
		        {
		        	con = getConnection(dbHostname,dbPort,dbName,dbUser,dbUserPassword);
			        if(getProperty(AUTO_ADD_PLUGINS_WEBINF_ATTRIBUTE).equals("true"))
			        {
			            // executed if all plugins should be loaded:
			            pluginLoader.processPlugins(context,interpreter,con, request, response);
			        }
			        action.setConnection(con);
		        }
			    actionTemplate = action.execute(request, response, context, interpreter, scriptName, pluginLoader.getPlugins());
			    if(scriptName.equals(ConstantsWeb.CONFIG_SCRIPT))
		        {
		        	readConfigFile((String)properties.get(CONTEXT_PATH));
		        	dbConnectionOk = databaseConnectionOk();
		        }
			    if(con!=null)
			    {
			    	con.close();
			    }
   
		    	template  = getTemplate(language + "/" + actionTemplate);
			}
		    
			catch (ClassNotFoundException cnfe)
	        {
	            context.put("error_cause",cnfe);
			    template = getTemplate(language + "/" + ConstantsWeb.PAGE_ERROR);
	        }
	        catch (InstantiationException ie)
	        {
	            context.put("error_cause",ie);
	            template = getTemplate(language + "/" + ConstantsWeb.PAGE_ERROR);
	        }
	        catch (IllegalAccessException ia)
	        {
	            context.put("error_cause",ia);
	            template = getTemplate(language + "/" + ConstantsWeb.PAGE_ERROR);
	        }
		    catch (Exception ex)
		    {
		    	context.put("error_cause",ex);
	            template = getTemplate(language + "/" + ConstantsWeb.PAGE_ERROR);
		    }
		}
		else
		{
		    context.put("error_cause",new Exception("Unknown action command in query string"));
		    template = getTemplate(language + "/" + ConstantsWeb.PAGE_ERROR);
		}
		return template;
    }
	
	public static MySqlConnection getConnection(String hostname, int port, String databasename,String dbUser, String dbPassword) throws Exception
	{
	    return new MySqlConnection(hostname,port,databasename,dbUser,dbUserPassword);
	}
	
	public static String getMessage(String message)
	{
	    String msg = messages.getProperty(message); 
	    if (message!=null)
	    {
	        return msg;
	    }
	    else
	    {
	        return MESSAGE_UNDEFINED;
	    }
	}
	
	private void loadMessages()
	{
	    String messagesFile = getServletConfig().getInitParameter(MESSAGES);
	    FileInputStream fis=null;
	    if(language==null)
	    {
	    	language=DEFAULT_LANGUAGE;
	    }
	    if ( messagesFile != null )
		{
			String realPath = getServletContext().getRealPath(DIRECTORY_MESSAGES + "/" + language + "/" + messagesFile);
			if ( realPath != null )
			{
				messagesFile = realPath;
			}
		}
        try
        {
            messages.clear();
            fis = new FileInputStream(messagesFile);
            messages.load(fis);
        }
        catch (Exception ex)
        {
            System.out.println("messages file: " + messagesFile + " could not be found or could nor be read");
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch(Exception ex)
            {
            }
        }
	    
	}
	
	public static String getProperty(String key)
	{
	    return properties.getProperty(key);
	}
	
	public static String getLanguage()
	{
	    return language;
	}
	
	public static ArrayList <BeanshellPlugin> getPlugins()
	{
	    return pluginLoader.getPlugins();
	}
	
	public static Ldap getLdap()
	{
		return ldap;
	}
}