package com.datamelt.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email
{
    private static final String SMTP_TRANSPORT_PROTOCOL			= "smtp";
    private static final String SMTP_AUTHENTICATION				= "true";
    private static final String SMTP_STARTTLS_ENABLE			= "true";
    //private static final String SMTP_STARTTLS_ENABLE			= "false";
    private static final String SMTP_MESSAGE_MIME_TYPE			= "text/html; charset=utf-8";
    
    private String smtpHost = "localhost";
    private String smtpPort = "587";
    //private String smtpPort = "25";
    private String smtpUser;
    private String smtpUserPassword;
    private String smtpAddressFrom;
    
    private String subject = "Registration for the Business Rules Maintenance Tool";
    private String subjectResetPassword = "Business Rules Maintenance Tool - Password reset";
    private String body;
    private String recipient;
    private String recipientUserid;
    private String recipientHash;
    private String url;
    
    private boolean debugMode = false;
    
    public Email(String smtpHost,String smtpPort, String smtpAddressFrom, String smtpUser, String smtpUserPassword)
    {
    	this.smtpHost = smtpHost;
    	this.smtpPort = smtpPort;
    	this.smtpAddressFrom = smtpAddressFrom;
    	this.smtpUser = smtpUser;
    	this.smtpUserPassword = smtpUserPassword;
    }
    
    public String getHostname()
    {
    	 InetAddress ip;
         String hostname="";
         try
         {
             ip = InetAddress.getLocalHost();
             hostname = ip.getHostName();
         }
         catch (UnknownHostException e) 
         {
             e.printStackTrace();
         }
         return hostname;
    }
    
    public Email(String smtpHost, String smtpAddressFrom, String smtpUser, String smtpUserPassword)
    {
    	this.smtpHost = smtpHost;
    	this.smtpAddressFrom = smtpAddressFrom;
    	this.smtpUser = smtpUser;
    	this.smtpUserPassword = smtpUserPassword;
    }
    
    public static void main(String[] args) throws Exception
    {
    	Email email = new Email("smtp.web.de","uwe.geercken@web.de","uwe.geercken","2fast4U2");
    	//email.setDebugMode(true);
    	email.setRecipient("uwe.geercken@web.de");
    	email.setRecipientUserid("uwegeercken");
    	
    	email.send();
    	
    }
    
    private String getMessageBody() throws Exception
    {
    	StringBuffer buffer = new StringBuffer();
    	
    	buffer.append("<p>You have registered for a login to the Business Rules Maintenance Tool. To be able to use the tool your account needs to be activated.</p>");
    	buffer.append("<p>Please click on the link below to confirm your login and activate the account as user: " + recipientUserid + "</p>");
    	buffer.append("<p>Click to <a href=\"" + url + "?action=bsh&scriptname=selectconfirmregistration&code=" + recipientHash +"\">confirm your login</a></p>");
    	buffer.append("<p>Your IT Support for the Business Rules Maintenance Tool.</p>");
    	
    	return buffer.toString();
    }
    
    private String getMessageBodyPasswordReset() throws Exception
    {
    	StringBuffer buffer = new StringBuffer();
    	
    	buffer.append("<p>You have requested a password reset for the Business Rules Maintenance Tool.</p>");
    	buffer.append("<p>Please click on the link below to confirm the reset of your password as user: " + recipientUserid + "</p>");
    	buffer.append("<p>Click to <a href=\"" + url + "?action=bsh&scriptname=selectconfirmpasswordreset&code=" + recipientHash +"\">reset your password</a></p>");
    	buffer.append("<p>Your IT Support for the Business Rules Maintenance Tool.</p>");
    	
    	return buffer.toString();
    }
    
	public void send() throws Exception
	{
		generateRecipientHash();
		
		Properties properties = new Properties();
		
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", smtpPort);
		properties.put("mail.transport.protocol", SMTP_TRANSPORT_PROTOCOL);
		if(smtpUser!=null && !smtpUser.equals("") && smtpUserPassword !=null && !smtpUserPassword.equals(""))
		{
			properties.put("mail.smtp.auth", SMTP_AUTHENTICATION);
		}
		else
		{
			properties.put("mail.smtp.auth", false);
		}
		properties.put("mail.smtp.starttls.enable", SMTP_STARTTLS_ENABLE);
        
        Session mailSession = Session.getDefaultInstance(properties, null);
        
        if(debugMode)
        {
        	mailSession.setDebug(true);
        }
		
        MimeMessage message = new MimeMessage(mailSession);
        
        message.setFrom(new InternetAddress(smtpAddressFrom));
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setSentDate(new Date());

        String htmlText = getMessageBody();
        message.setContent(htmlText, SMTP_MESSAGE_MIME_TYPE);
        
        Transport transport = mailSession.getTransport();
        if(smtpUser!=null && !smtpUser.equals("") && smtpUserPassword !=null && !smtpUserPassword.equals(""))
		{
        	transport.connect(smtpUser,smtpUserPassword);
		}
        else
        {
        	transport.connect();
        }
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();			
	}
	
	public void sendResetPassword()
	{
		try 
		{
			generateRecipientHash();
			
			Properties properties = new Properties();
			
			properties.put("mail.smtp.host", smtpHost);
			properties.put("mail.smtp.port", smtpPort);
			properties.put("mail.transport.protocol", SMTP_TRANSPORT_PROTOCOL);
			if(smtpUser!=null && !smtpUser.equals("") && smtpUserPassword !=null && !smtpUserPassword.equals(""))
			{
				properties.put("mail.smtp.auth", SMTP_AUTHENTICATION);
			}
			properties.put("mail.smtp.starttls.enable", SMTP_STARTTLS_ENABLE);
	        
	        Session mailSession = Session.getDefaultInstance(properties, null);
	        
	        if(debugMode)
	        {
	        	mailSession.setDebug(true);
	        }
			
	        MimeMessage message = new MimeMessage(mailSession);
	        
	        message.setFrom(new InternetAddress(smtpAddressFrom));
	        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
	        message.setSubject(subjectResetPassword);
	        message.setSentDate(new Date());

	        String htmlText = getMessageBodyPasswordReset();
	        message.setContent(htmlText, SMTP_MESSAGE_MIME_TYPE);
	        
	        Transport transport = mailSession.getTransport();
	        if(smtpUser!=null && !smtpUser.equals("") && smtpUserPassword !=null && !smtpUserPassword.equals(""))
			{
	        	transport.connect(smtpUser,smtpUserPassword);
			}
	        else
	        {
	        	transport.connect();
	        }
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();			
	        
	    }
		catch (Exception ex) 
		{
			ex.printStackTrace();
	    }
	}

	public String getSmtpHost()
	{
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost)
	{
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort()
	{
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort)
	{
		this.smtpPort = smtpPort;
	}

	public String getSmtpUser()
	{
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser)
	{
		this.smtpUser = smtpUser;
	}

	public String getSmtpUserPassword()
	{
		return smtpUserPassword;
	}

	public void setSmtpUserPassword(String smtpUserPassword)
	{
		this.smtpUserPassword = smtpUserPassword;
	}

	public String getSmtpAddressFrom()
	{
		return smtpAddressFrom;
	}

	public void setSmtpAddressFrom(String smtpAddressFrom)
	{
		this.smtpAddressFrom = smtpAddressFrom;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getRecipient()
	{
		return recipient;
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public boolean isDebugMode()
	{
		return debugMode;
	}

	public void setDebugMode(boolean debugMode)
	{
		this.debugMode = debugMode;
	}

	public String getRecipientUserid()
	{
		return recipientUserid;
	}

	public void setRecipientUserid(String recipientUserid)
	{
		this.recipientUserid = recipientUserid;
	}

	public String getRecipientHash()
	{
		return recipientHash;
	}
	
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	private void generateRecipientHash() throws Exception
	{
		String password = PasswordUtility.generateRandomPassword();
    	String hash = PasswordUtility.generateHash(password);
		recipientHash = hash;
	}
}


