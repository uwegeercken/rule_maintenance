package com.datamelt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.datamelt.db.DbCollections;
import com.datamelt.db.MySqlConnection;
import com.datamelt.db.Project;
import com.datamelt.db.Rule;
import com.datamelt.db.RuleGroup;
import com.datamelt.db.RuleGroupAction;
import com.datamelt.db.RuleSubgroup;
import com.datamelt.db.User;
import com.datamelt.db.Field;

public class ProjectCopyUtility 
{
	public static void copy(Project originalProject, Project newProject, User user, MySqlConnection connection) throws Exception
	{
		ArrayList <RuleGroup>ruleGroups = DbCollections.getAllRuleGroups(connection,originalProject.getId());
		newProject.setRulegroups(ruleGroups);
		
		originalProject.loadFields();
		
		for(int i=0;i<originalProject.getFields().size();i++)
		{
			Field field = originalProject.getFields().get(i);
			field.setProjectId(newProject.getId());
			field.setConnection(connection);
			field.insert(connection.getPreparedStatement(Field.INSERT_SQL));
		}
		
		HashMap <Long,Long>ruleGroupIdMap = new HashMap<Long,Long>();
		
		for(int i=0;i<ruleGroups.size();i++)
		{
			RuleGroup dbRuleGroup = ruleGroups.get(i);
			
			long originalRuleGroupId = dbRuleGroup.getId();
			
			dbRuleGroup.setConnection(connection);
			dbRuleGroup.setProjectId(newProject.getId());
			dbRuleGroup.loadRuleGroupActions();
			
			dbRuleGroup.insert(connection.getPreparedStatement(RuleGroup.INSERT_SQL),newProject,user);
			
			// capture old and new id of the rulegroup
			// the dependencies are updated based on this
			ruleGroupIdMap.put(originalRuleGroupId, dbRuleGroup.getId());
			
			ArrayList <RuleGroupAction>actions = dbRuleGroup.getActions();
			for (int k=0;k<actions.size();k++)
			{
				RuleGroupAction action = actions.get(k);
				action.setConnection(connection);
				action.setRulegroupId(dbRuleGroup.getId());
				
				action.insert(connection.getPreparedStatement(RuleGroupAction.INSERT_SQL),newProject,user);
			}
			
			ArrayList <RuleSubgroup>subGroups = DbCollections.getAllRuleSubgroups(connection,originalRuleGroupId);
			dbRuleGroup.setRuleSubgroups(subGroups);
			
			for (int f=0;f<subGroups.size();f++)
			{
				RuleSubgroup dbRuleSubgroup = subGroups.get(f);
				dbRuleSubgroup.setConnection(connection);
				dbRuleSubgroup.setRulegroupId(dbRuleGroup.getId());
				
				dbRuleSubgroup.insert(connection.getPreparedStatement(RuleSubgroup.INSERT_SQL),newProject,user);
				
				for(int m=0;m<dbRuleSubgroup.getRules().size();m++)
				{
					Rule dbRule = dbRuleSubgroup.getRules().get(m);
					dbRule.setConnection(connection);
					dbRule.setRuleSubgroupId(dbRuleSubgroup.getId());
					
					dbRule.insert(connection.getPreparedStatement(Rule.INSERT_SQL),newProject,user);
				}
			}
		}
		
		// all groups processed. we need to update the group dependencies
		for(int i=0;i<ruleGroups.size();i++)
		{
			RuleGroup dbRuleGroup = ruleGroups.get(i);
			
			// get id of the group that this group depends on
			long dependantGroupId = dbRuleGroup.getDependentRuleGroupId();
			if(dependantGroupId>0)
			{
				// get the id from the group that was inserted previously
				long newDependantGroupId = ruleGroupIdMap.get(dependantGroupId);
				dbRuleGroup.setDependentRuleGroupId(newDependantGroupId);
				dbRuleGroup.update(connection.getPreparedStatement(RuleGroup.UPDATE_SQL), newProject, user);
			}
		}
		
		
	}
}
