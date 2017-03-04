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

import java.util.ArrayList;

import com.datamelt.db.Action;
import com.datamelt.db.Check;
import com.datamelt.db.DbCollections;
import com.datamelt.db.MySqlConnection;
import com.datamelt.db.Project;
import com.datamelt.db.Rule;
import com.datamelt.db.RuleGroup;
import com.datamelt.db.RuleGroupAction;
import com.datamelt.db.RuleSubgroup;
import com.datamelt.db.Type;

public class ProjectMappingUtility 
{
	private static final String DEFAULT_VALID_FROM_DATE = "2014-01-01";
	private static final String DEFAULT_VALID_UNTIL_DATE = "2014-12-31";
	private static final String UNDEFINED = "[undefined]";
	private static final String PASSED = "passed";
	private static final String FAILED = "failed";
	
	public static RuleGroup mapRuleGroup(Project project, com.datamelt.rules.core.RuleGroup group)
	{
		RuleGroup dbRuleGroup = new RuleGroup();
		dbRuleGroup.setProjectId(project.getId());
		dbRuleGroup.setName(group.getId());
		dbRuleGroup.setDescription(group.getDescription());
		if(group.getValidFrom()==null)
		{
			dbRuleGroup.setValidFrom(DEFAULT_VALID_FROM_DATE);
		}
		else
		{
			dbRuleGroup.setValidFrom(group.getValidFrom());
		}
		if(group.getValidUntil()==null)
		{
			dbRuleGroup.setValidUntil(DEFAULT_VALID_UNTIL_DATE);
			
		}
		else
		{
			dbRuleGroup.setValidUntil(group.getValidUntil());
		}
		dbRuleGroup.setLastUpdateUser(project.getLastUpdateUser());
		
		return dbRuleGroup;

	}
	
	public static RuleSubgroup mapRuleSubgroup(RuleGroup dbRuleGroup, com.datamelt.rules.core.RuleSubGroup subgroup, MySqlConnection connection) throws Exception
	{
		RuleSubgroup dbRuleSubGroup = new RuleSubgroup();
		dbRuleSubGroup.setName(subgroup.getId());
		dbRuleSubGroup.setDescription(subgroup.getDescription());
		dbRuleSubGroup.setIntergroupOperator(subgroup.getLogicalOperatorSubGroupAsString());
		dbRuleSubGroup.setRuleOperator(subgroup.getLogicalOperatorRulesAsString());
		dbRuleSubGroup.setRulegroupId(dbRuleGroup.getId());
		dbRuleSubGroup.setLastUpdateUser(dbRuleGroup.getLastUpdateUser());
		dbRuleSubGroup.setConnection(connection);
		
		return dbRuleSubGroup;
	}
	
	public static Rule mapRules(RuleSubgroup dbRuleSubgroup, com.datamelt.rules.core.XmlRule rule , MySqlConnection connection) throws Exception
	{
		ArrayList <Type>dbTypes = DbCollections.getAllTypes(connection);
		ArrayList <Check>dbChecks = DbCollections.getAllChecks(connection); 
		
		Rule dbRule = new Rule();
		dbRule.setRuleSubgroupId(dbRuleSubgroup.getId());
		dbRule.setName(rule.getId());
		dbRule.setDescription(rule.getDescription());
		dbRule.setMessagePassed(rule.getMessage(com.datamelt.rules.core.XmlRule.PASSED).getText());
		dbRule.setMessageFailed(rule.getMessage(com.datamelt.rules.core.XmlRule.FAILED).getText());
		dbRule.setExpectedValue(rule.getExpectedValueRule());
		
		dbRule.setCheck(getDbCheck(dbChecks, rule));
		dbRule.setExpectedValueType(getDbRuleExpectedValueType(dbTypes, rule));
		mapRuleParameters(dbTypes, dbRule, rule, connection);
		
		dbRule.setLastUpdateUser(dbRuleSubgroup.getLastUpdateUser());
		dbRule.setConnection(connection);
		
		return dbRule;
	}
	
	public static RuleGroupAction mapAction(RuleGroup dbRuleGroup, com.datamelt.rules.core.XmlAction action, MySqlConnection connection) throws Exception
	{
		ArrayList <Type>dbTypes = DbCollections.getAllTypes(connection);
		
		RuleGroupAction dbAction = new RuleGroupAction();
		dbAction.setConnection(connection);
		dbAction.setRulegroupId(dbRuleGroup.getId());
		if(action.getId()==null || action.getId().trim().equals(""))
		{
			dbAction.setName(UNDEFINED);
		}
		else
		{
			dbAction.setName(action.getId());
		}
		if(action.getDescription()==null || action.getDescription().trim().equals(""))
		{
			dbAction.setDescription(UNDEFINED);
		}
		else
		{
			dbAction.setDescription(action.getDescription());
		}
		if(action.getExecuteIf()==0)
		{
			dbAction.setExecuteIf(PASSED);
		}
		else
		{
			dbAction.setExecuteIf(FAILED);
		}
		dbAction.setRulegroupId(dbRuleGroup.getId());
		dbAction.setLastUpdateUser(dbRuleGroup.getLastUpdateUser());
		dbAction.setAction(getDbAction(action, connection));
		mapActionParameters(dbTypes, dbAction, action, connection);
		mapActionSetterObjects(dbTypes, dbAction, action, connection);
		mapActionGetterObjects(dbTypes, dbAction, action, connection);
		
		return dbAction;
	}
	
	private static void mapActionParameters(ArrayList <Type>dbTypes, RuleGroupAction dbAction, com.datamelt.rules.core.XmlAction action, MySqlConnection connection) throws Exception
	{
		if(action.getParameters().size()>0)
		{
			Type type = getDbActionParameterType(dbTypes, action, 0);
			dbAction.setParameter1(action.getParameters().get(0).getValue());
			dbAction.setParameter1Type(type);
		}
		if(action.getParameters().size()>1)
		{
			Type type = getDbActionParameterType(dbTypes, action, 1);
			dbAction.setParameter2(action.getParameters().get(1).getValue());
			dbAction.setParameter2Type(type);
		}
		if(action.getParameters().size()>2)
		{
			Type type = getDbActionParameterType(dbTypes, action, 2);
			dbAction.setParameter3(action.getParameters().get(2).getValue());
			dbAction.setParameter3Type(type);
		}
	}
	
	private static void mapActionSetterObjects(ArrayList <Type>dbTypes, RuleGroupAction dbAction, com.datamelt.rules.core.XmlAction action, MySqlConnection connection) throws Exception
	{
		if(action.getActionSetterObject()!=null)
		{
			Type type = getDbActionSetterObjectType(dbTypes, action, 0);
			dbAction.setObject2Parametertype(type);
			dbAction.setObject2Parameter(action.getActionSetterObject().getParameters().get(0).getValue());
		}
	}
	
	private static void mapActionGetterObjects(ArrayList <Type>dbTypes, RuleGroupAction dbAction, com.datamelt.rules.core.XmlAction action, MySqlConnection connection) throws Exception
	{
		// we only import the first getter object of an action as the web app currently only
		// supports one getter object
		if(action.getActionGetterObjects().size()>0)
		{
			Type type = getDbActionGetterObjectType(dbTypes, action, 0);
			dbAction.setObject1Parametertype(type);
			dbAction.setObject1Parameter(action.getActionGetterObjects().get(0).getParameters().get(0).getValue());
			dbAction.setObject1Type(type);
		}
	}
	
	private static Action getDbAction(com.datamelt.rules.core.XmlAction action, MySqlConnection connection) throws Exception
	{
		ArrayList <Action>dbActions = DbCollections.getAllActions(connection);
		int found=-1;
		for(int g=0;g<dbActions.size();g++)
		{
			Action oneAction = dbActions.get(g);
			if(action.getClassName().equals(oneAction.getClassname())&& action.getMethodName().equals(oneAction.getMethodname()))
			{
				found = g;
				break;
			}
		}
		return dbActions.get(found);
	}
	
	private static Type getDbActionParameterType(ArrayList <Type>dbTypes, com.datamelt.rules.core.XmlAction action, int index) throws Exception
	{
		int found = -1;
		for(int h=0;h< dbTypes.size();h++)
		{
			Type type = dbTypes.get(h);
			if(type.getName().equals(action.getParameters().get(index).getType()))
			{
				found=h;
				break;
			}
		}
		return dbTypes.get(found);
	}
	
	private static Type getDbActionSetterObjectType(ArrayList <Type>dbTypes, com.datamelt.rules.core.XmlAction action, int index) throws Exception
	{
		int found = -1;
		for(int h=0;h< dbTypes.size();h++)
		{
			Type type = dbTypes.get(h);
			if(type.getName().equals(action.getActionSetterObject().getParameters().get(index).getType()))
			{
				found=h;
				break;
			}
		}
		return dbTypes.get(found);
	}
	
	private static Type getDbActionGetterObjectType(ArrayList <Type>dbTypes, com.datamelt.rules.core.XmlAction action, int index) throws Exception
	{
		int found = -1;
		for(int h=0;h< dbTypes.size();h++)
		{
			Type type = dbTypes.get(h);
			if(type.getName().equals(action.getActionGetterObjects().get(0).getParameters().get(0).getType()))
			{
				found=h;
				break;
			}
		}
		return dbTypes.get(found);
	}
	
	private static Type getDbRuleExpectedValueType(ArrayList <Type>dbTypes, com.datamelt.rules.core.XmlRule rule) throws Exception
	{
		int found = -1;
		for(int h=0;h< dbTypes.size();h++)
		{
			Type type = dbTypes.get(h);
			if(type.getName().equals(rule.getExpectedValueRuleType()))
			{
				found=h;
				break;
			}
		}
		return dbTypes.get(found);
	}
	
	private static Check getDbCheck(ArrayList <Check>dbChecks, com.datamelt.rules.core.XmlRule rule) throws Exception
	{
		int found = -1;
		for(int h=0;h< dbChecks.size();h++)
		{
			Check check = dbChecks.get(h);
			String checkFullName =check.getPackageName() + "." + check.getClassName();
			if(checkFullName.equals(rule.getCheckToExecute()))
			{
				found=h;
				break;
			}
		}
		return dbChecks.get(found);
	}
	
	private static Type getDbRuleParameterType(ArrayList <Type>dbTypes, com.datamelt.rules.core.XmlRule rule, int index) throws Exception
	{
		int found = -1;
		for(int h=0;h< dbTypes.size();h++)
		{
			Type type = dbTypes.get(h);
			if(type.getName().equals(rule.getRuleObjects().get(index).getParameterType()))
			{
				found=h;
				break;
			}
		}
		return dbTypes.get(found);
	}
	
	private static Type getDbRuleMethodReturnType(ArrayList <Type>dbTypes, com.datamelt.rules.core.XmlRule rule, int index) throws Exception
	{
		int found = -1;
		for(int h=0;h< dbTypes.size();h++)
		{
			Type type = dbTypes.get(h);
			if(type.getName().equals(rule.getRuleObjects().get(index).getMethodReturnType()))
			{
				found=h;
				break;
			}
		}
		return dbTypes.get(found);
	}
	
	private static void mapRuleParameters(ArrayList <Type>dbTypes, Rule dbRule, com.datamelt.rules.core.XmlRule rule, MySqlConnection connection) throws Exception
	{
		if(rule.getRuleObjects().size()>0)
		{
			Type type = getDbRuleParameterType(dbTypes, rule, 0);
			dbRule.setObject1Parametertype(type);
			dbRule.setObject1Parameter(rule.getRuleObjects().get(0).getParameter());
			
			Type type2 = getDbRuleMethodReturnType(dbTypes, rule, 0);
			dbRule.setObject1Type(type2);
			
		}
		if(rule.getRuleObjects().size()>1)
		{
			Type type = getDbRuleParameterType(dbTypes, rule, 1);
			dbRule.setObject2Parametertype(type);
			dbRule.setObject2Parameter(rule.getRuleObjects().get(1).getParameter());
			
			Type type2 = getDbRuleMethodReturnType(dbTypes, rule, 1);
			dbRule.setObject2Type(type2);
		}
	}
}
