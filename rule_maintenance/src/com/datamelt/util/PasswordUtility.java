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

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtility 
{
	private static final Random RANDOM = new SecureRandom();
	public static final int PASSWORD_LENGTH = 12;

	/**
	* Generate a random String suitable for use as a temporary password.
	*
	*/
	public static String generateRandomPassword()
	{
	    // Pick from some letters that won't be easily mistaken for each
	    // other. So, for example, omit o O and 0, 1 l and L.

	    String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+-_#=!";
	    String pw = "";

	    for (int i=0; i<PASSWORD_LENGTH; i++)
	    {
	        int index = (int)(RANDOM.nextDouble()*letters.length());
	        pw += letters.substring(index, index+1);
	    }
	    return pw;
	}
	
	// generate a hash that is sent to the user
	public static String generateHash(String message) throws Exception
	{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(message));
		return String.format("%032x", new BigInteger(1, md5.digest()));
	}
	
}
