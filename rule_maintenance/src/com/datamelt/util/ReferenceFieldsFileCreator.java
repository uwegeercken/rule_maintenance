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

import java.io.File;
import java.io.FileWriter;

import com.datamelt.db.Project;

/**
* ReferenceFieldsFileCreator. Program to generate a zip file for the JaRe - Java Ruleengine - from the database of the Business Rules Maintenance Tool.
* An XML File is generated for each rulegroup and written to the temporary folder. Then the files are put/compressed into a single zip file.
* If no project name is specified, then zip files for all projects will be created.
* Only Rule Groups which are valid for the given date are considered for the output.
*
*
* all code by uwe geercken - 2014-2018
*/

public class ReferenceFieldsFileCreator 
{
	private static final String FILENAME = "reference_fields.xml";
	
	private Project project;
	private String temporaryPath;
	private String templatePath;
	private String templateName;
	
	public ReferenceFieldsFileCreator(Project project, String temporaryPath, String templatePath, String templateName) throws Exception
	{
		this.project = project;
		this.temporaryPath = temporaryPath;
		this.templatePath = templatePath;
		this.templateName = templateName;
	}
	
	public String writeFile() throws Exception
	{
		String tempPath = FileUtility.addTrailingSlash(temporaryPath) + project.getName();
		File folder = new File(tempPath);
		folder.mkdirs();
		
		project.loadFields();
		
		String fileName=FileUtility.addTrailingSlash(tempPath) + FILENAME;
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		fw.write(project.mergeWithReferenceFieldTemplate(templatePath, templateName));
		fw.close();
		return fileName;
	}
	
	public void deleteFile(String fileName)
	{
		File file = new File(fileName);
		file.delete();
	}
}
