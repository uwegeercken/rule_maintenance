/*
 * 
 *
 * all code by uwe geercken - 2014
 */
package com.datamelt.plugin;


public abstract class Plugin
{
    
    private String name;
    
    public Plugin(String name)
    {
        this.name=name;
    }
    
    public Plugin()
    {
        
    }
    
    public String getName()
    {
        return this.name;
    }
}
