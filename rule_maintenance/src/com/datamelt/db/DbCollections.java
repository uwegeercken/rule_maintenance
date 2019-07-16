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
package com.datamelt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.datamelt.db.Project;
import com.datamelt.util.DateUtility;

public class DbCollections
{
	/**
	 * get a list of all projects 
	 */
    public static ArrayList<Project> getAllProjects(MySqlConnection connection) throws Exception
    {
        String sql="select id from project" +
        	" order by name";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Project>list = new ArrayList<Project>();
        while(rs.next())
        {
        	Project project = new Project();
        	project.setConnection(connection);
        	project.setId(rs.getLong("id"));
        	project.load();
        	project.loadRuleGroupsCount();
        	project.loadRulesCount();
        	project.loadFieldsCount();
            list.add(project);
        }
        rs.close();
        return list;
    }
    
	/**
	 * get a list of all projects for a given user 
	 */
    public static ArrayList<Project> getAllProjects(MySqlConnection connection, User user) throws Exception
    {
        String sql="select id as id from project" +
        		" order by name";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Project>list = new ArrayList<Project>();
        while(rs.next())
        {
        	Project project = new Project();
        	project.setConnection(connection);
        	project.setId(rs.getLong("id"));
        	project.load();
        	project.loadRuleGroupsCount();
        	project.loadRulesCount();
        	project.loadFieldsCount();
        	if(project.getPrivateProject()==1) 
        	{
        		if(user.canUpdateProject(project)|| user.isInGroup(User.ADMINISTRATOR)|| user.getId()==project.getOwnerUser().getId())
        		{
        			list.add(project);
        		}
        	}
        	else
        	{
        		list.add(project);
        	}
        }
        rs.close();
        return list;
    }
    
	/**
	 * get a list of all rulegroups for a given project 
	 */
    public static ArrayList<RuleGroup> getAllRuleGroups(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select id from rulegroup where project_id=" + projectId +
        	" and (disabled=0 or disabled is null)" +
        	" order by id";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleGroup>list = new ArrayList<RuleGroup>();
        while(rs.next())
        {
        	RuleGroup rulegroup = new RuleGroup();
        	rulegroup.setConnection(connection);
        	rulegroup.setId(rs.getLong("id"));
        	rulegroup.load();
        	rulegroup.loadDependentRuleGroup();
        	rulegroup.loadNumberOfRuleGroupsDependingOnThisRuleGroup();
            list.add(rulegroup);
        }
        rs.close();
        return list;
    }
    
	/**
	 * get a list of all disabled rulegroups for a given project 
	 */
    public static ArrayList<RuleGroup> getAllDisabledRuleGroups(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select id from rulegroup where project_id=" + projectId +
        	" and disabled=1" +
        	" order by id";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleGroup>list = new ArrayList<RuleGroup>();
        while(rs.next())
        {
        	RuleGroup rulegroup = new RuleGroup();
        	rulegroup.setConnection(connection);
        	rulegroup.setId(rs.getLong("id"));
        	rulegroup.load();
        	rulegroup.loadDependentRuleGroup();
        	rulegroup.loadNumberOfRuleGroupsDependingOnThisRuleGroup();
            list.add(rulegroup);
        }
        rs.close();
        return list;
    }

    /**
	 * get a list of all testdata for a given rulegroup 
	 */
    public static ArrayList<RuleGroupTestData> getAllRuleGroupTestData(MySqlConnection connection, long ruleGroupId, long userId) throws Exception
    {
        String sql="select id from rulegroup_testdata where rulegroup_id=" + ruleGroupId +
        		" and user_id= " + userId + " order by id desc";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleGroupTestData>list = new ArrayList<RuleGroupTestData>();
        while(rs.next())
        {
        	RuleGroupTestData testData = new RuleGroupTestData();
        	testData.setConnection(connection);
        	testData.setId(rs.getLong("id"));
        	testData.load();
            list.add(testData);
        }
        rs.close();
        return list;
    }
    
    /**
	 * get a list of only the latest testdata for a given rulegroup 
	 */
    public static ArrayList<RuleGroupTestData> getLatestRuleGroupTestData(MySqlConnection connection, long ruleGroupId, long userId) throws Exception
    {
        String sql="select id from rulegroup_testdata where rulegroup_id=" + ruleGroupId +
        	" and user_id= " + userId + " order by id desc limit 1";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleGroupTestData>list = new ArrayList<RuleGroupTestData>();
        while(rs.next())
        {
        	RuleGroupTestData testData = new RuleGroupTestData();
        	testData.setConnection(connection);
        	testData.setId(rs.getLong("id"));
        	testData.load();
            list.add(testData);
        }
        rs.close();
        return list;
    }
    
    /**
	 * get a list of all testdata for a given rulegroup 
	 */
    public static long getCountRuleGroupTestData(MySqlConnection connection, long ruleGroupId, long userId) throws Exception
    {
        String sql="select count(id) as counter from rulegroup_testdata where rulegroup_id=" + ruleGroupId +
        	" and user_id= " + userId;
        ResultSet rs = connection.getResultSet(sql);
        long counter = 0;
        if(rs.next())
        {
        	counter = rs.getLong("counter");
        }
        rs.close();
        return counter;
    }
    
    /**
     *  get a list of possible dependent rulegroups for the given rulegroup
     */
    public static ArrayList<RuleGroup> getAllPossibleDependentRuleGroups(MySqlConnection connection, long projectId, long rulegroupId) throws Exception
    {
    	ArrayList <RuleGroup>list = new ArrayList<RuleGroup>();
    	
    	// check how many rulegroups depend on the given rulegroup
    	long counterGroupsDependingOnThisGroup = getCountGroupsDependingOnThisGroup(connection,projectId,rulegroupId);

    	// if other groups depend on this rulegroup it is not allowed to make this rulegroup dependent on other rulegroups
    	// to avoid circular references
        if(counterGroupsDependingOnThisGroup==0)
        {
	    	String sql="select id from rulegroup where (dependent_rulegroup_id is null or dependent_rulegroup_id=0) and id!=" + rulegroupId + " and project_id=" + projectId +
	        	" and (disabled=0 or disabled is null)" +
	    		" order by id";
	        ResultSet rs = connection.getResultSet(sql);
	        
	        while(rs.next())
	        {
	        	RuleGroup rulegroup = new RuleGroup();
	        	rulegroup.setConnection(connection);
	        	rulegroup.setId(rs.getLong("id"));
	        	rulegroup.load();
	        	rulegroup.loadDependentRuleGroup();
	            list.add(rulegroup);
	        }
	        rs.close();
        }
        return list;
    }
    
    /**
     * get a list of rulegroups that have a dependency to the specified group
     */
    public static ArrayList<RuleGroup> getAllDependentRuleGroups(MySqlConnection connection, long projectId, long rulegroupId) throws Exception
    {
    	ArrayList <RuleGroup>list = new ArrayList<RuleGroup>();
    	
    	String sql="select id from rulegroup where dependent_rulegroup_id =" + rulegroupId 
    			+ " and project_id=" + projectId
    			+ " order by id";
        ResultSet rs = connection.getResultSet(sql);
        
        while(rs.next())
        {
        	RuleGroup rulegroup = new RuleGroup();
        	rulegroup.setConnection(connection);
        	rulegroup.setId(rs.getLong("id"));
        	rulegroup.load();
            list.add(rulegroup);
        }
        rs.close();
        return list;
    }
    
    /**
     *  get a list of all rulegroups that other groups in the project depend on
     */
    public static ArrayList<RuleGroup> getAllDependentRuleGroups(MySqlConnection connection, long projectId) throws Exception
    {
    	ArrayList <RuleGroup>list = new ArrayList<RuleGroup>();
    	
    	String sql="select distinct dependent_rulegroup_id as id from rulegroup"
    			+ " where dependent_rulegroup_id is not null"
    			+ " and dependent_rulegroup_id>0"
    			+ " and (disabled=0 or disabled is null)"
    			+ " and project_id=" + projectId
    			+ " order by id";
        ResultSet rs = connection.getResultSet(sql);
        
        while(rs.next())
        {
        	RuleGroup rulegroup = new RuleGroup();
        	rulegroup.setConnection(connection);
        	rulegroup.setId(rs.getLong("id"));
        	rulegroup.load();
            list.add(rulegroup);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a count of how many rulegroups have a dependancy on the given rulegroup 
     */
    public static long getCountGroupsDependingOnThisGroup(MySqlConnection connection, long projectId, long rulegroupId) throws Exception
    {
    	// check how many rulegroups depend on the given rulegroup
    	String sql="select count(1) as counter from rulegroup"
    			+ " where dependent_rulegroup_id is not null"
    			+ " and (disabled=0 or disabled is null)"
    			+ " and dependent_rulegroup_id=" + rulegroupId + " and project_id=" + projectId;
        ResultSet rs = connection.getResultSet(sql);
    	long counterGroupsDependingOnThisGroup = 0;
        if(rs.next())
    	{
        	counterGroupsDependingOnThisGroup = rs.getLong("counter");
    	}
        rs.close();
        return counterGroupsDependingOnThisGroup;
    }
    
    /**
     * get all rulegroups which have a valid from or valid until setting that is not within the boundaries of the
     * provided rulegroup valid from and valid until 
     */
    public static ArrayList<RuleGroup> getRuleGroupsDependingWithInvalidFromUntil(MySqlConnection connection, long projectId, RuleGroup dependentRulegroup) throws Exception
    {
    	// check how many rulegroups depend on the given rulegroup and that have an
    	// invalid from/until validity (outside the validity of the rulegroup)
    	String sql="select id from rulegroup"
    			+ " where dependent_rulegroup_id is not null"
    			+ " and (disabled=0 or disabled is null)"
    			+ "and dependent_rulegroup_id=" + dependentRulegroup.getId() + " and project_id=" + projectId;
        ResultSet rs = connection.getResultSet(sql);
        ArrayList<RuleGroup> invalidRulegroups = new ArrayList<RuleGroup>();
        while(rs.next())
    	{
        	RuleGroup rulegroup = new RuleGroup();
        	rulegroup.setConnection(connection);
        	rulegroup.setId(rs.getLong("id"));
        	rulegroup.load();
        	
        	rulegroup.setDependentRuleGroup(dependentRulegroup);
        	
        	if(!DateUtility.isWithinValidFromUntil(rulegroup,dependentRulegroup))
        	{
        		invalidRulegroups.add(rulegroup);
        	}
    	}
        rs.close();
        return invalidRulegroups;
    }

    /**
     * if one or multiple rulegroups depend on another rulegroup, then get the minimum and maximun valid from
     * and valid until dates of those rulegroups 
     */
    public static String getRuleGroupsDependingMinMaxValidDates(ArrayList<RuleGroup> rulegroups) throws Exception
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    	Calendar minimumDate=null;
		Calendar maximumDate=null;
		
    	for(int i=0;i<rulegroups.size();i++)
    	{
    		RuleGroup ruleGroup = rulegroups.get(i);
    		
    		Calendar ruleGroupDateFrom = Calendar.getInstance();
    		ruleGroupDateFrom.setTime(sdf.parse(ruleGroup.getValidFrom()));
    		ruleGroupDateFrom.set(Calendar.HOUR_OF_DAY, 0);
    		ruleGroupDateFrom.set(Calendar.MINUTE, 0);
    		ruleGroupDateFrom.set(Calendar.SECOND, 0);
    		ruleGroupDateFrom.set(Calendar.MILLISECOND, 0);
    		
    		Calendar ruleGroupDateUntil = Calendar.getInstance();
    		ruleGroupDateUntil.setTime(sdf.parse(ruleGroup.getValidUntil()));
    		ruleGroupDateUntil.set(Calendar.HOUR_OF_DAY, 0);
    		ruleGroupDateUntil.set(Calendar.MINUTE, 0);
    		ruleGroupDateUntil.set(Calendar.SECOND, 0);
    		ruleGroupDateUntil.set(Calendar.MILLISECOND, 0);
    		
    		if (minimumDate==null || ruleGroupDateFrom.before(minimumDate))
    		{
    			minimumDate = ruleGroupDateFrom;
    		}
    		if (maximumDate==null || ruleGroupDateUntil.after(maximumDate))
    		{
    			maximumDate = ruleGroupDateUntil;
    		}
    	}
    	
    	return sdf.format(minimumDate.getTime()) + "/" + sdf.format(maximumDate.getTime());
    }
    
    public static ArrayList<RuleGroup> getAllRuleGroupsSubgroupsActions(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select id from rulegroup where project_id=" + projectId +
        	" and (disabled=0 or disabled is null)" +
        	" order by id";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleGroup>list = new ArrayList<RuleGroup>();
        while(rs.next())
        {
        	RuleGroup rulegroup = new RuleGroup();
        	rulegroup.setConnection(connection);
        	rulegroup.setId(rs.getLong("id"));
        	rulegroup.load();
        	rulegroup.loadDependentRuleGroup();
        	rulegroup.loadRuleSubgroups();
        	rulegroup.loadRuleGroupActions();
        	rulegroup.loadDependentRuleGroup();
	        rulegroup.loadDependentRuleGroupsList();
            list.add(rulegroup);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a count of all rulegroups for a given project 
     */
    public static long getAllRuleGroupsCount(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select count(1) as counter from rulegroup"
        		+ " where project_id=" + projectId
        		+ " and (disabled=0 or disabled is null)";
        ResultSet rs = connection.getResultSet(sql);
        if(rs.next())
        {
        	return rs.getLong("counter");
        }
        else
        {
        	return 0;
        }
    }
    
    /**
     * get a count of all reference fields for a given project 
     */
    public static long getAllReferenceFieldsCount(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select count(1) as counter from reference_fields"
        		+ " where project_id=" + projectId;
        ResultSet rs = connection.getResultSet(sql);
        if(rs.next())
        {
        	return rs.getLong("counter");
        }
        else
        {
        	return 0;
        }
    }
    
    /**
     * get a rulegroup by providing it's name
     */
    public RuleGroup getRuleGroupByName(MySqlConnection connection, long projectId, String groupName) throws Exception
    {
    	ArrayList <RuleGroup>list = getAllRuleGroups(connection, projectId);
    	int found=-1;
    	for(int i=0;i<list.size();i++)
    	{
    		RuleGroup group = list.get(i);
    		if(group.getName().equals(groupName))
    		{
    			found=i;
    			break;
    		}
    	}
    	if(found>-1)
    	{
    		return list.get(found);
    	}
    	else
    	{
    		return null;
    	}
    }
    
    /**
     * get all valid rulegroups for a given project that are valid on the specified date or that are valid in the future
     */
    public static ArrayList<RuleGroup> getAllValidRuleGroups(MySqlConnection connection, long projectId, String selectedDate) throws Exception
    {
        // select all rulegroups that are valid at the moment or that are
    	// valid in the future
    	String sql= "select id from rulegroup where project_id=" + projectId 
    			+   " and ((valid_from <= '" + selectedDate + "' and valid_until >= '" + selectedDate + "') or valid_from > '" + selectedDate + "')"
    			+   " and (disabled=0 or disabled is null)"
    			+   " order by id";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleGroup>list = new ArrayList<RuleGroup>();
        while(rs.next())
        {
        	RuleGroup rulegroup = new RuleGroup();
        	rulegroup.setConnection(connection);
        	rulegroup.setId(rs.getLong("id"));
        	rulegroup.load();
            list.add(rulegroup);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all subgroups for a given rulegroup 
     */
    public static ArrayList<RuleSubgroup> getAllRuleSubgroups(MySqlConnection connection, long rulegroupId) throws Exception
    {
        String sql="select id from rulesubgroup"
        		+ " where rulegroup_id=" + rulegroupId
        		+ " order by id";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleSubgroup>list = new ArrayList<RuleSubgroup>();
        while(rs.next())
        {
        	RuleSubgroup ruleSubgroup = new RuleSubgroup();
        	ruleSubgroup.setConnection(connection);
        	ruleSubgroup.setId(rs.getLong("id"));
        	ruleSubgroup.load();
            list.add(ruleSubgroup);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all actions for a given rulegroup 
     */
    public static ArrayList<RuleGroupAction> getAllRuleGroupActions(MySqlConnection connection, long rulegroupId) throws Exception
    {
        String sql="select id from rulegroupaction"
        		+ " where rulegroup_id=" + rulegroupId 
        	    + " order by id";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <RuleGroupAction>list = new ArrayList<RuleGroupAction>();
        while(rs.next())
        {
        	RuleGroupAction rulegroupAction = new RuleGroupAction();
        	rulegroupAction.setConnection(connection);
        	rulegroupAction.setId(rs.getLong("id"));
        	rulegroupAction.load();
            list.add(rulegroupAction);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all reference fields for a given project
     */
    public static ArrayList<Field> getAllFields(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select id from reference_fields"
        		+ " where project_id=" + projectId 
        	    + " order by name_descriptive";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Field>list = new ArrayList<Field>();
        while(rs.next())
        {
        	Field field = new Field();
        	field.setConnection(connection);
        	field.setId(rs.getLong("id"));
        	field.load();
            list.add(field);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all databases
     */
    public static boolean getCheckDatabaseExists(MySqlConnection connection, String databaseName) throws Exception
    {
    	ResultSet resultSet = connection.getMetaData().getCatalogs();
    	boolean exists = false;
    	while(resultSet.next())
        {
    		String name = resultSet.getString(1);
    		if(name.trim().toLowerCase().equals(databaseName.trim().toLowerCase()))
    		{
    			exists = true;
    		}
        }
    	resultSet.close();
        return exists;
    }
    
    /**
     * get a list of all users that are assigned to a given group 
     */
    public static ArrayList<User> getAllGroupUsers(MySqlConnection connection, String groupName) throws Exception
    {
        String sql="select user_id from groups, groupuser" + 
        	" where groupuser.groups_id=groups.id "+
        	" and name = '" + groupName + "'";	
        ResultSet rs = connection.getResultSet(sql);
        ArrayList<User> list = new ArrayList<User>();
        while(rs.next())
        {
            User user = new User();
            user.setConnection(connection);
            user.setId(rs.getLong("user_id"));
            user.load();
            list.add(user);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all users that are assigned to a given group 
     */
    public static ArrayList<User> getAllGroupUsers(MySqlConnection connection, long groupId) throws Exception
    {
        String sql="select user_id from groups, groupuser" + 
        	" where groupuser.groups_id=groups.id "+
        	" and groupuser.id = " + groupId ;	
        ResultSet rs = connection.getResultSet(sql);
        ArrayList<User> list = new ArrayList<User>();
        while(rs.next())
        {
            User user = new User();
            user.setConnection(connection);
            user.setId(rs.getLong("user_id"));
            user.load();
            list.add(user);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all groups that the given user is assigned to 
     */
    public static ArrayList<Group> getAllGroups(MySqlConnection connection, User user) throws Exception
    {
        String sql="select id from groups order by name";	
        ResultSet rs = connection.getResultSet(sql);
        ArrayList<Group> list = new ArrayList<Group>();
        while(rs.next())
        {
        	Group group = new Group();
        	group.setConnection(connection);
        	group.setId(rs.getLong("id"));
        	group.load();
        	if(group.getId()==1 && user.isInGroup(User.ADMINISTRATOR))
        	{
        		list.add(group);
        	}
        	else if(group.getId()>1)
        	{
        		list.add(group);
        	}
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of rules according to a specified search term and date (if specified) 
     */
    public static ArrayList<Rule> getSearchRules(MySqlConnection connection, User user, String searchTerm, String searchDate) throws Exception
    {
        String sql="select rule.id as ruleid, rulegroup.id as rulegroupid, rulegroup.project_id as projectid"
        		+ " from rule, rulesubgroup, rulegroup"
        		+ " where rule.rulesubgroup_id = rulesubgroup.id and rulesubgroup.rulegroup_id = rulegroup.id"
        		+ " and (disabled=0 or disabled is null)"
        		+ " and (rule.name like " + "'%" + searchTerm + "%'"
        		+ " or rule.description like "+ "'%" + searchTerm + "%')";
        
        if(searchDate!=null && !searchDate.trim().equals(""))
        {
        	sql = sql + " and rule.last_update >= '" + searchDate + "'";
        }
        
        sql = sql + " order by rule.name";
        
        ResultSet rs = connection.getResultSet(sql);
        ArrayList<Rule> list = new ArrayList<Rule>();
        while(rs.next())
        {
        	Project project = new Project();
        	project.setConnection(connection);
        	project.setId(rs.getLong("projectid"));
        	project.load();
        	
        	if(project.getPrivateProject()==1 && user.isInProjectGroup(project.getGroup()) || project.getPrivateProject()==0)
        	{
        	
	        	Rule rule = new Rule();
	        	rule.setConnection(connection);
	        	rule.setId(rs.getLong("ruleid"));
	        	rule.load();
	        	rule.setProject(project);
        	
	        	rule.setRuleGroupId(rs.getLong("rulegroupid"));
        	
	        	list.add(rule);
        	}
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of actions according to a specified search term 
     */
    public static ArrayList<RuleGroupAction> getSearchActions(MySqlConnection connection, User user, String searchTerm, String searchDate) throws Exception
    {
        String sql="select rulegroupaction.id as rulegroupactionid, rulegroup.project_id as projectid"
        		+ " from rulegroupaction, rulegroup"
        		+ " where rulegroupaction.rulegroup_id = rulegroup.id"
        		+ " and (rulegroup.disabled=0 or rulegroup.disabled is null)"
        		+ " and (rulegroupaction.name like " + "'%" + searchTerm + "%'"
        		+ " or rulegroupaction.description like "+ "'%" + searchTerm + "%')";
        		
        if(searchDate!=null && !searchDate.trim().equals(""))
        {
        	sql = sql + " and rulegroupaction.last_update >= '" + searchDate + "'";
        }
        
        sql = sql + " order by rulegroupaction.name";		

        ResultSet rs = connection.getResultSet(sql);
        ArrayList<RuleGroupAction> list = new ArrayList<RuleGroupAction>();
        while(rs.next())
        {
        	Project project = new Project();
        	project.setConnection(connection);
        	project.setId(rs.getLong("projectid"));
        	project.load();
        	
        	if(project.getPrivateProject()==1 && user.isInProjectGroup(project.getGroup()) || project.getPrivateProject()==0)
        	{
	        	RuleGroupAction action = new RuleGroupAction();
	        	action.setConnection(connection);
	        	action.setId(rs.getLong("rulegroupactionid"));
	        	action.load();
	        	action.setProject(project);
	        	
	       		list.add(action);
        	}
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of fields (contained in a rule) according to a specified search term and date (if specified) 
     */
    public static ArrayList<Rule> getSearchFieldsRules(MySqlConnection connection, User user, String searchTerm, String searchDate) throws Exception
    {
        String sql="select rule.id as ruleid, rulegroup.id as rulegroupid, rulegroup.project_id as projectid"
        		+ " from rule, rulesubgroup, rulegroup"
        		+ " where rule.rulesubgroup_id = rulesubgroup.id and rulesubgroup.rulegroup_id = rulegroup.id"
        		+ " and (disabled=0 or disabled is null)"
        		+ " and (rule.object1_parameter like " + "'%" + searchTerm + "%'"
        		+ " or rule.object2_parameter like "+ "'%" + searchTerm + "%')";
        
        if(searchDate!=null && !searchDate.trim().equals(""))
        {
        	sql = sql + " and rule.last_update >= '" + searchDate + "'";
        }
        
        sql = sql + " order by rule.name";
        
        ResultSet rs = connection.getResultSet(sql);
        ArrayList<Rule> list = new ArrayList<Rule>();
        while(rs.next())
        {
        	Project project = new Project();
        	project.setConnection(connection);
        	project.setId(rs.getLong("projectid"));
        	project.load();
        	
        	if(project.getPrivateProject()==1 && user.isInProjectGroup(project.getGroup()) || project.getPrivateProject()==0)
        	{
        	
	        	Rule rule = new Rule();
	        	rule.setConnection(connection);
	        	rule.setId(rs.getLong("ruleid"));
	        	rule.load();
	        	rule.setProject(project);
        	
	        	rule.setRuleGroupId(rs.getLong("rulegroupid"));
        	
	        	list.add(rule);
        	}
        }
        rs.close();
        return list;
    }

    /**
     * get a list of fields (contained in an action) according to a specified search term and date (if specified) 
     */
    public static ArrayList<RuleGroupAction> getSearchFieldsActions(MySqlConnection connection, User user, String searchTerm, String searchDate) throws Exception
    {
        String sql="select rulegroupaction.id as rulegroupactionid, rulegroup.project_id as projectid"
        		+ " from rulegroupaction, rulegroup"
        		+ " where rulegroupaction.rulegroup_id = rulegroup.id"
        		+ " and (rulegroup.disabled=0 or rulegroup.disabled is null)"
        		+ " and (rulegroupaction.object1_parameter like " + "'%" + searchTerm + "%'"
        		+ " or rulegroupaction.object2_parameter like "+ "'%" + searchTerm + "%'"
        		+ " or rulegroupaction.object3_parameter like "+ "'%" + searchTerm + "%')";
        		
        if(searchDate!=null && !searchDate.trim().equals(""))
        {
        	sql = sql + " and rulegroupaction.last_update >= '" + searchDate + "'";
        }
        
        sql = sql + " order by rulegroupaction.name";		

        ResultSet rs = connection.getResultSet(sql);
        ArrayList<RuleGroupAction> list = new ArrayList<RuleGroupAction>();
        while(rs.next())
        {
        	Project project = new Project();
        	project.setConnection(connection);
        	project.setId(rs.getLong("projectid"));
        	project.load();
        	
        	if(project.getPrivateProject()==1 && user.isInProjectGroup(project.getGroup()) || project.getPrivateProject()==0)
        	{
	        	RuleGroupAction action = new RuleGroupAction();
	        	action.setConnection(connection);
	        	action.setId(rs.getLong("rulegroupactionid"));
	        	action.load();
	        	action.setProject(project);
	        	
	       		list.add(action);
        	}
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all users 
     */
    public static ArrayList <User> getAllUsers(MySqlConnection connection) throws Exception
    {
        String sql="select id from user where deactivated=0" + 
        	" order by name";	
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <User> list = new ArrayList<User>();
        while(rs.next())
        {
            User user = new User();
            user.setConnection(connection);
            user.setId(rs.getLong("id"));
            user.load();
            list.add(user);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all deactivated users 
     */
    public static ArrayList <User> getAllDeactivatedUsers(MySqlConnection connection) throws Exception
    {
        String sql="select id from user where deactivated=1" + 
        	" order by name";	
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <User> list = new ArrayList<User>();
        while(rs.next())
        {
            User user = new User();
            user.setConnection(connection);
            user.setId(rs.getLong("id"));
            user.load();
            list.add(user);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of all checks available 
     */
    public static ArrayList <Check> getAllChecks(MySqlConnection connection) throws Exception
    {
        String sql="select id from `check`" + 
        	" order by name_descriptive";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Check> list = new ArrayList<Check>();
        while(rs.next())
        {
        	Check check = new Check();
        	check.setConnection(connection);
        	check.setId(rs.getLong("id"));
        	check.load();
            list.add(check);
        }
        rs.close();
        return list;
    }
    
    /**
     * retrieve a list of methods available for a given check 
     */
    public static ArrayList <CheckMethod> getAllCheckMethods(MySqlConnection connection, long checkId) throws Exception
    {
    	String sql="select id from check_method where check_id=" + checkId ;
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <CheckMethod> list = new ArrayList<CheckMethod>();
        while(rs.next())
        {
        	CheckMethod method = new CheckMethod();
        	method.setConnection(connection);
        	method.setId(rs.getLong("id"));
        	method.load();
            list.add(method);
        }
        rs.close();
        return list;
    }
    
    /**
     * determine if a method exists for a given check 
     */
    public static boolean getCheckMethodExists(MySqlConnection connection, long checkId, String compareType, String compareToType) throws Exception
    {
    	String compareTypeSql;
        if(compareType==null)
        {
        	compareTypeSql = "compare is null";
        }
        else
        {
        	compareTypeSql = "compare = '" + convertType(compareType) + "'";
        }
        String compareToTypeSql;
        if(compareToType==null)
        {
        	compareToTypeSql = "compare_to is null";
        }
        else
        {
        	compareToTypeSql = "compare_to = '" + convertType(compareToType) + "'";
        }
        String sql="select count(1) as counter from check_method where check_id=" + checkId +
        		" and " + compareTypeSql + " and " + compareToTypeSql;
        		
        ResultSet rs = connection.getResultSet(sql);
        long numberOfMethods=0;
        if(rs.next())
        {
        	numberOfMethods = rs.getLong("counter");
        }
        rs.close();
        return numberOfMethods>=1;
    }
    
    private static String convertType(String type)
    {
    	// the database contains int and not integer - so we translate it here
    	if(type.equals("integer"))
    	{
    		return "int";
    	}
    	else
    	{
    		return type;
    	}
    }
    
    /**
     * determine if a specified method of an action exists 
     */
    public static boolean getActionMethodExists(MySqlConnection connection, long actionId, String methodTypes, String object2Type) throws Exception
    {
    	// array of types the method received
    	String[] methodType;
    	int numberOfTypes = 0;
    	if(!methodTypes.equals(""))
    	{
    		// split into types
    		methodType = methodTypes.toLowerCase().split(",");
    		numberOfTypes = methodType.length;
    	}
    	
    	// get all methods for the selected action
    	String sql = "select * from action_method where action_id=" + actionId;
    	
    	ResultSet rs = connection.getResultSet(sql);
    	int found = 0;

    	// loop over all methods from the database
    	while(rs.next())
    	{
    		String actionMethodTypes = rs.getString("method_types");
    		String[] actionMethodType = actionMethodTypes.split(",");
    		
    		String actionParameter1Type =  rs.getString("optional_type1");
    		String actionParameter2Type =  rs.getString("optional_type2");
    		String actionParameter3Type =  rs.getString("optional_type3");

    		String actionReturnType =  rs.getString("return_type");
    		
    		// buffer to contain the types from the database
    		// the buffer is filled until the number of parameters the user has entered is specified
    		StringBuffer buffer = new StringBuffer();
    		int typesCount = 0;
    		// if [empty], we ignore the method_types field from the database
    		if(!actionMethodTypes.equals("[empty]"))
    		{
    			for(int i=0;i<actionMethodType.length;i++)
    			{
    				typesCount++;
    				if(buffer.length()>0)
    				{
    					buffer.append(", ");
    				}
    				buffer.append(actionMethodType[i].trim());
    			}
    		}
    		if(typesCount<numberOfTypes)
    		{
    			if(actionParameter1Type!=null)
    			{
    				typesCount++;
    				if(buffer.length()>0)
    				{
    					buffer.append(", ");
    				}
    				buffer.append(actionParameter1Type);
    			}
    		}
    		if(typesCount<numberOfTypes)
    		{
    			if(actionParameter2Type!=null)
    			{
    				typesCount++;
    				if(buffer.length()>0)
    				{
    					buffer.append(", ");
    				}
    				buffer.append(actionParameter2Type);
    			}
    		}
    		if(typesCount<numberOfTypes)
    		{
    			if(actionParameter3Type!=null)
    			{
    				typesCount++;
    				if(buffer.length()>0)
    				{
    					buffer.append(", ");
    				}
    				buffer.append(actionParameter3Type);
    			}
    		}
    		// check if a method from the db equals to the method specified by the user
    		// and if the return type is the same
    		if(methodTypes.equals(buffer.toString().toLowerCase()) && actionReturnType.toLowerCase().equals(object2Type.toLowerCase()))
    		{
    			found++;
    			break;
    		}
    	}
    	
        return found>0;
    }
    
    /**
     * get a list of methods that exist for a given action 
     */
    public static ArrayList <ActionMethod> getAllActionMethods(MySqlConnection connection, long actionId) throws Exception
    {
        String sql="select id from action_method where action_id=" + actionId ;
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <ActionMethod> list = new ArrayList<ActionMethod>();
        while(rs.next())
        {
        	ActionMethod method = new ActionMethod();
        	method.setConnection(connection);
        	method.setId(rs.getLong("id"));
        	method.load();
            list.add(method);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of actions that exist 
     */
    public static ArrayList <Action> getAllActions(MySqlConnection connection) throws Exception
    {
        String sql="select id from action" + 
        	" order by methoddisplayname";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Action> list = new ArrayList<Action>();
        while(rs.next())
        {
        	Action action = new Action();
        	action.setConnection(connection);
        	action.setId(rs.getLong("id"));
        	action.load();
            list.add(action);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of types that exist 
     */
    public static ArrayList <Type> getAllTypes(MySqlConnection connection) throws Exception
    {
        String sql="select id from types order by id";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Type> list = new ArrayList<Type>();
        while(rs.next())
        {
        	Type type = new Type();
        	type.setConnection(connection);
        	type.setId(rs.getLong("id"));
        	type.load();
            list.add(type);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a list of activities that have occurred 
     */
    public static ArrayList <Activity> getActivities(MySqlConnection connection) throws Exception
    {
        String sql="select id from activity_log order by last_update desc limit 40";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Activity> list = new ArrayList<Activity>();
        while(rs.next())
        {
        	Activity activity = new Activity();
        	activity.setConnection(connection);
        	activity.setId(rs.getLong("id"));
        	activity.load();
            list.add(activity);
        }
        rs.close();
        return list;
    }
    
    /**
     * get a count of all rules over all projects
     */
    public static long getRulesCount(MySqlConnection connection) throws Exception
    {
        String sql="select count(1) as numberofrules from rule";
        ResultSet rs = connection.getResultSet(sql);
        long numberOfRules=0;
        if(rs.next())
        {
        	numberOfRules= rs.getLong("numberofrules");
        }
        rs.close();
        return numberOfRules;
    }
    
    /**
     * get a count of all rules for a given project
     */
    public static long getRulesCount(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="SELECT count(rule.id) as numberOfRules"
        		+ " FROM rule, rulesubgroup, rulegroup"
        		+ " where rule.rulesubgroup_id = rulesubgroup.id"
        		+ " and rulesubgroup.rulegroup_id = rulegroup.id"
        		+ " and rulegroup.project_id = " + projectId;
        		
        ResultSet rs = connection.getResultSet(sql);
        long numberOfRules=0;
        if(rs.next())
        {
        	numberOfRules= rs.getLong("numberofrules");
        }
        rs.close();
        return numberOfRules;
    }
    
    /**
     * get a count of all actions over all projects and rulegroups
     */
    public static long getActionsCount(MySqlConnection connection) throws Exception
    {
        String sql="select count(1) as numberofactions"
        		+ " from rulegroupaction,rulegroup"
        		+ " where rulegroupaction.rulegroup_id = rulegroup.id"
        		+ " and (rulegroup.disabled=0 or rulegroup.disabled is null)";
        ResultSet rs = connection.getResultSet(sql);
        long numberOfActions=0;
        if(rs.next())
        {
        	numberOfActions= rs.getLong("numberofactions");
        }
        rs.close();
        return numberOfActions;
    }
    
    /**
     * get a count of all projects
     */
    public static long getProjectsCount(MySqlConnection connection) throws Exception
    {
        String sql="select count(1) as numberofprojects from project";
        ResultSet rs = connection.getResultSet(sql);
        long numberOfProjects=0;
        if(rs.next())
        {
        	numberOfProjects= rs.getLong("numberofprojects");
        }
        rs.close();
        return numberOfProjects;
    }

    /**
     * get a count of all users
     */
    public static long getUsersCount(MySqlConnection connection) throws Exception
    {
        String sql="select count(1) as numberofusers from user where deactivated=0";
        ResultSet rs = connection.getResultSet(sql);
        long numberOfRules=0;
        if(rs.next())
        {
        	numberOfRules= rs.getLong("numberofusers");
        }
        rs.close();
        return numberOfRules;
    }

    /**
     * delete the history of a given user and a given type
     */
    public static void deleteUserHistory(MySqlConnection connection, String type, long typeId, User user) throws Exception
    {
    	String deleteSql = "delete from history where type=? and type_id=? and user_id=?";
    	PreparedStatement psHistory = connection.getPreparedStatement(deleteSql);
    	
    	psHistory.setString(1,type);
    	psHistory.setLong(2, typeId);
    	psHistory.setLong(3, user.getId());
    	psHistory.executeUpdate();    			
    }
    
    /**
     * delete the complete history of a given user
     */
    public static void deleteUserHistory(MySqlConnection connection, User user) throws Exception
    {
    	String deleteSql = "delete from history where user_id=?";
    	PreparedStatement psHistory = connection.getPreparedStatement(deleteSql);
    	
    	psHistory.setLong(1, user.getId());
    	psHistory.executeUpdate();    			
    }
    
    /**
     * delete the complete testdata of a given user
     */
    public static void deleteUserRuleGroupTestData(MySqlConnection connection, User user) throws Exception
    {
    	String deleteSql = "delete from rulegroup_testdata where user_id=?";
    	PreparedStatement psTestData = connection.getPreparedStatement(deleteSql);
    	
    	psTestData.setLong(1, user.getId());
    	psTestData.executeUpdate();    			
    }
    
    /**
     * delete the reference fields for a given project
     */
    public static void deleteReferenceFields(MySqlConnection connection, long projectId) throws Exception
    {
    	String deleteSql = "delete from reference_fields where project_id=?";
    	PreparedStatement psFields = connection.getPreparedStatement(deleteSql);
    	
    	psFields.setLong(1, projectId);
    	psFields.executeUpdate();    			
    }
    
    /**
     * get a list of history entries for a given user
     */
    public static ArrayList <History> getUserHistory(MySqlConnection connection, User user) throws Exception
    {
        String sql="select max(id) as id from history where user_id=" + user.getId() + " group by type,type_id order by max(last_update) desc limit 20";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <History> list = new ArrayList<History>();
        while(rs.next())
        {
        	History history = new History();
        	history.setConnection(connection);
        	history.setId(rs.getLong("id"));
        	history.load();
        	
        	if(history.getType().equals("project"))
        	{
        		Project project = new Project();
        		project.setConnection(connection);
        		project.setId(history.getTypeId());
        		project.load();
        		
        		history.setProject(project);
        		history.setHistoryEntry(project);
        	}
        	else if(history.getType().equals("rulegroup"))
        	{
        		RuleGroup rulegroup = new RuleGroup();
        		rulegroup.setConnection(connection);
        		rulegroup.setId(history.getTypeId());
        		rulegroup.load();
        		
        		Project project = new Project();
        		project.setConnection(connection);
        		project.setId(history.getParent1());
        		project.load();
        		
        		history.setProject(project);
        		history.setHistoryEntry(rulegroup);
        		
        	}
        	else if(history.getType().equals("rulesubgroup"))
        	{
        		RuleSubgroup rulesubgroup = new RuleSubgroup();
        		rulesubgroup.setConnection(connection);
        		rulesubgroup.setId(history.getTypeId());
        		rulesubgroup.load();
        		
        		Project project = new Project();
        		project.setConnection(connection);
        		project.setId(history.getParent2());
        		project.load();
        		
        		history.setProject(project);
        		history.setHistoryEntry(rulesubgroup);
        	}
        	else if(history.getType().equals("rule"))
        	{
        		Rule rule = new Rule();
        		rule.setConnection(connection);
        		rule.setId(history.getTypeId());
        		rule.load();
        		
        		Project project = new Project();
        		project.setConnection(connection);
        		project.setId(history.getParent3());
        		project.load();
        		
        		history.setProject(project);
        		history.setHistoryEntry(rule);
        	}
        	else if(history.getType().equals("action"))
        	{
        		RuleGroupAction action = new RuleGroupAction();
        		action.setConnection(connection);
        		action.setId(history.getTypeId());
        		action.load();
        		
        		Project project = new Project();
        		project.setConnection(connection);
        		project.setId(history.getParent2());
        		project.load();
        		
        		history.setProject(project);
        		history.setHistoryEntry(action);
        	}
        
        	if(history.getProject().getPrivateProject()==1) 
        	{
        		if(user.canUpdateProject(history.getProject())|| user.isInGroup(User.ADMINISTRATOR)|| user.getId()==history.getProject().getOwnerUser().getId())
        		{
        			list.add(history);
        		}
        	}
        	else
        	{
        		list.add(history);
        	}
        }
        rs.close();
        return list;
    }
}
