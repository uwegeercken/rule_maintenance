package com.datamelt.util;
/**
 * maps Pentaho PDI types to Java types defined in the database
 * 
 *
 */

public class TypeMapper 
{
	public static int getJavaType(int pentahoType)
	{
		int javaType=0;
		switch (pentahoType)
		{
	        case 1:	javaType=4; // Pentaho TYPE_NUMBER
	                break;
	        case 2: javaType=1; // Pentaho TYPE_STRING
	                break;
	        case 3: javaType=8; // Pentaho TYPE_DATE
            		break;
	        case 4: javaType=5; // Pentaho TYPE_BOOLEAN
    				break;
	        case 5: javaType=6; // Pentaho TYPE_INTEGER
					break;
	        case 6: javaType=7; // Pentaho TYPE_BIGNUMBER
					break;
	        case 7: javaType=3; // Pentaho TYPE_BOOLEAN
					break;
		}        
        return javaType;
	}
}
