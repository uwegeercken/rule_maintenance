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
    
    private final String LDAP_INITIAL_CONTEXT_FACTORY 	= "com.sun.jndi.ldap.LdapCtxFactory";
    private final String SECURITY_TYPE					= "simple";
    
    private String getUrlDc()
    {
    	return "ldap://" + host + "." + domain + ":" + port +"/";
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
		catch (Exception e)
		{
		    //e.printStackTrace();
		    // do nothing for the moment
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

