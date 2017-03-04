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

