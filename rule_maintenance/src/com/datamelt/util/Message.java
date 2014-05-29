package com.datamelt.util;

public class Message
{
    private String text;
    private String additionalInfo;
    private String type = INFO; //default
    
    public static final String INFO  = "info";
    public static final String ERROR = "error";
    public static final String WARNING = "warning";
    
    private Object affectedObject;
    
    public Message ()
    {
    }
    
    public Message (String type, String text)
    {
        this.text = text;
        this.type = type;
    }
    
    public Message (String type, String text, String additionalInfo)
    {
        this.text = text;
        this.type = type;
        this.additionalInfo = additionalInfo;
    }

    public Object getAffectedObject()
    {
    	return affectedObject;
    }
    
    public void setAffectedObject(Object object)
    {
    	this.affectedObject = object;
    }
    
    public String getText()
    {
        return text;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getAdditionalInfo()
    {
        return additionalInfo;
    }
    
    public void setAdditionalInfo(String additionalInfo)
    {
        this.additionalInfo = additionalInfo;
    }
}
