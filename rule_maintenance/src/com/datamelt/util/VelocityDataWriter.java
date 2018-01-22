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
package com.datamelt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;


 /** 
 * Utility class to write database objects to a specific format.<br>
 * The format will be specified in external velocity templates.
 * @author uwegeercken
 *
 */
public class VelocityDataWriter
{
    public static final String XML_FILE_EXTENSION   = ".xml";
    public static final String HTML_FILE_EXTENSION  = ".html";
	private static final String DEFAULT_OBJECT_NAME = "dbObject";
	
	private static final String RESOURCE_PATH = "file.resource.loader.path";
	
	private String templatePath;
    private String templateName;
    private PrintStream out;
    
    private Template t;
    private Hashtable<String, Object> objects= new Hashtable<String, Object>();
    
    public VelocityDataWriter(String templatePath, String templateName) throws Exception
    {
        this.templatePath = templatePath;
        this.templateName = templateName;
		
        // Alternatively the properties may be read from the properties file
        //InputStream in = null;
        //in = getServletContext().getResourceAsStream("/WEB-INF/velocity.properties");
        //properties.load(in);
        //engine.init(properties);
        
        Properties properties = new Properties();
        properties.setProperty(RESOURCE_PATH, templatePath);
        
        properties.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
        properties.setProperty("runtime.log.logsystem.log4j.logger","velocity");
        
        VelocityEngine ve = new VelocityEngine();
        ve.init(properties);
        t = ve.getTemplate(templateName);
    }
    
    public void reInit(String templateName) throws Exception
    {
        objects.clear();
        this.templateName = templateName;
		Properties properties = new Properties();
        properties.setProperty(RESOURCE_PATH, templatePath);
        
        properties.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
        properties.setProperty("runtime.log.logsystem.log4j.logger","velocity");
        
        VelocityEngine ve = new VelocityEngine();
        ve.init(properties);
        t = ve.getTemplate(templateName);
    }
    
    public void clearObjects()
    {
        objects.clear();
    }
    
    public void setOutputStream(PrintStream out)
    {
        this.out = out;
    }

    public void addObject(String objectName, Object object)
    {
    	objects.put(objectName,object);
    }
    
	public void addObject(Object object)
	{
		objects.put(DEFAULT_OBJECT_NAME,object);
	}
    
	public String merge() throws Exception
	{
		VelocityContext context = new VelocityContext();
	
		if (objects.size()== 0)
		{
			throw new Exception("velocitydatawriter: no objects to process");
		}
		for (Enumeration<String> e = objects.keys(); e.hasMoreElements() ;)
		{
			String objectKey = (String)e.nextElement();
			context.put(objectKey, objects.get(objectKey));
		}
		StringWriter writer = new StringWriter();
		t.merge(context,writer);
		return writer.toString();
	}    
    
    public void write(String path, String fileName) throws Exception
    {

		String result = merge();        
        String checkedPath = adjustPath(path);
        File f = new File(checkedPath);
        f.mkdirs();
        OutputStream o = new FileOutputStream(new File(checkedPath + fileName));
        out = new PrintStream(o);
        out.println(result);
        out.close();  
    }

	private String adjustPath(String path)
	{
		if (path.endsWith("\\") || path.endsWith("/"))
		{
			return path;
		}
		else
		{
			return path + "/";
		}
	}

	/**
     * the path to the velocity templates
	 * @return path to the templates
	 */
	public String getTemplatePath()
	{
		return templatePath;
	}

	/**
	 * @param string
	 */
	public void setTemplatePath(String path)
	{
		templatePath = path;
	}

	/**
	 * @return
	 */
	public String getTemplateName()
	{
		return templateName;
	}

	/**
	 * @param string
	 */
	public void setTemplateName(String string)
	{
		templateName = string;
	}

}
