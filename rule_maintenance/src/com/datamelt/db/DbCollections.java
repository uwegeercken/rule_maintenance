/*
 *
 * all code by uwe geercken - 2014
 */
package com.datamelt.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import com.datamelt.db.Project;

public class DbCollections
{
    
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
            list.add(project);
        }
        rs.close();
        return list;
    }
    
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
    
    
    public static ArrayList<RuleGroup> getAllRuleGroups(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select id from rulegroup where project_id=" + projectId +
        	" order by id";
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
    
    public static long getAllRuleGroupsCount(MySqlConnection connection, long projectId) throws Exception
    {
        String sql="select count(1) as counter from rulegroup where project_id=" + projectId;
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
    
    public static ArrayList<RuleGroup> getAllValidRuleGroups(MySqlConnection connection, long projectId, String selectedDate) throws Exception
    {
        
    	String sql="select id from rulegroup where project_id=" + projectId + " and valid_from <='" + selectedDate + "' and valid_until>='" + selectedDate + "'" +
        	" order by id";
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
    
    public static ArrayList<RuleSubgroup> getAllRuleSubgroups(MySqlConnection connection, long rulegroupId) throws Exception
    {
        String sql="select id from rulesubgroup where rulegroup_id=" + rulegroupId +
        	" order by id";
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
    
    public static ArrayList<RuleGroupAction> getAllRuleGroupActions(MySqlConnection connection, long rulegroupId) throws Exception
    {
        String sql="select id from rulegroupaction where rulegroup_id=" + rulegroupId +
        	" order by id";
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
    
    public static ArrayList <Check> getAllChecks(MySqlConnection connection) throws Exception
    {
        String sql="select id from `check`" + 
        	" order by name";
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
    
    public static ArrayList <Type> getAllTypes(MySqlConnection connection) throws Exception
    {
        String sql="select id from types" + 
        	" order by id";
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
    
    public static ArrayList <Object> getUserHistory(MySqlConnection connection, User user) throws Exception
    {
        String sql="select max(id) as id from history where user_id=" + user.getId() + " group by type,type_id order by max(last_update) desc limit 20";
        ResultSet rs = connection.getResultSet(sql);
        ArrayList <Object> list = new ArrayList<Object>();
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
