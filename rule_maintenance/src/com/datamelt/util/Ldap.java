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

import java.util.Hashtable;

import javax.naming.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class Ldap
{
	private String host;
	private String domain;
    private int port;
    
    private static final String LDAP_INITIAL_CONTEXT_FACTORY 	= "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String SECURITY_TYPE					= "simple";
    
    private String getUrlDc()
    {
    	return "ldap://" + host + ":" + port +"/";
    }
    
    public boolean authenticate(String userid, String password)
    {
        Hashtable <String,String>env = new Hashtable<String,String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY,LDAP_INITIAL_CONTEXT_FACTORY);
		env.put(Context.SECURITY_AUTHENTICATION, SECURITY_TYPE);
		env.put(Context.SECURITY_PRINCIPAL, userid + "@" + domain);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.PROVIDER_URL, getUrlDc());
		
		boolean loginOk = false;
		try
		{
		    LdapContext ctx = new InitialLdapContext(env,null);
		    ctx.close();
		    loginOk=true;
		}
		catch (AuthenticationNotSupportedException exception) 
        {
            System.out.println("Ldap: The authentication is not supported by the server");
        }
        catch (AuthenticationException exception)
        {
            System.out.println("Ldap: Incorrect password or username");
        }

        catch (NamingException exception)
        {
            System.out.println("Ldap: Error when trying to create the context: " + env.toString());
        }
		return loginOk;
    }
    
	public String getHost()
	{
		return host;
	}

	public void setHost(String host) 
	{
		this.host = host;
	}

	public String getDomain() 
	{
		return domain;
	}

	public void setDomain(String domain) 
	{
		this.domain = domain;
	}

	public int getPort() 
	{
		return port;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}
    
}

