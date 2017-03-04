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
