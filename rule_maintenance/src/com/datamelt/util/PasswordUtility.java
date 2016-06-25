package com.datamelt.util;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtility 
{
	private static final Random RANDOM = new SecureRandom();
	public static final int PASSWORD_LENGTH = 10;

	/**
	* Generate a random String suitable for use as a temporary password.
	*
	*/
	public static String generateRandomPassword()
	{
	    // Pick from some letters that won't be easily mistaken for each
	    // other. So, for example, omit o O and 0, 1 l and L.

	    String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+";
	    String pw = "";

	    for (int i=0; i<PASSWORD_LENGTH; i++)
	    {
	        int index = (int)(RANDOM.nextDouble()*letters.length());
	        pw += letters.substring(index, index+1);
	    }
	    return pw;
	}
}
