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

/**
 * Contains the sql code to create the database tables and fields required
 * by this web application.
 * 
 * When the web application is started the first time or when the database configuration
 * was changed, this class is used.
 * 
 * @author uwe geercken
 *
 */
public class CreateDatabase
{
	public static final String TABLE_ACTION = "`action`";
	public static final String TABLE_ACTION_METHOD = "`action_method`";
	public static final String TABLE_CHECK = "`check`";
	public static final String TABLE_CHECK_METHOD = "`check_method`";
	public static final String TABLE_REFERENCE_FIELDS = "`reference_fields`";
	public static final String TABLE_ACTIVITY_LOG = "`activity_log`";
	public static final String TABLE_GROUPS = "`groups`";
	public static final String TABLE_HISTORY = "`history`";
	public static final String TABLE_PROJECT = "`project`";
	public static final String TABLE_RULE = "`rule`";
	public static final String TABLE_RULEGROUP = "`rulegroup`";
	public static final String TABLE_RULEGROUPACTION = "`rulegroupaction`";
	public static final String TABLE_RULESUBGROUP = "`rulesubgroup`";
	public static final String TABLE_TYPES = "`types`";
	public static final String TABLE_USER = "`user`";
	public static final String TABLE_GROUPUSER= "`groupuser`";
	public static final String TABLE_RULEGROUP_TESTDATA = "`rulegroup_testdata`";
	public static final String TABLE_JARE_VERSION = "`jare_version`";
	
	public static final String CREATE_TABLE_JARE_VERSION = "CREATE TABLE IF NOT EXISTS " + TABLE_JARE_VERSION + " ("
    		+ " `id` int(10) NOT NULL AUTO_INCREMENT,"
    		+ " `version` varchar(255) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	
	public static final String CREATE_TABLE_ACTION_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTION + " ("
    		+ " `id` int(10) NOT NULL AUTO_INCREMENT,"
    		+ " `description` varchar(255) DEFAULT NULL,"
    		+ " `classname` varchar(255) NOT NULL,"
    		+ " `methodname` varchar(255) DEFAULT NULL,"
    		+ " `methoddisplayname` varchar(255) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	
	public static final String CREATE_TABLE_ACTION_METHOD_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTION_METHOD + " ("
    		+ " `id` int(10) NOT NULL AUTO_INCREMENT,"
    		+ " `action_id` int(10) NOT NULL,"
    		+ " `return_type` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `method_types` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `note` varchar(255) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `optional_type1` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `optional_type1_explanation` varchar(255) DEFAULT NULL,"
    		+ " `optional_type2` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `optional_type2_explanation` varchar(255) DEFAULT NULL,"
    		+ " `optional_type3` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `optional_type3_explanation` varchar(255) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_actionid` (`action_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";

    public static final String CREATE_TABLE_ACTIVITY_LOG_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITY_LOG + " ("
    		+ " `id` int(19) NOT NULL AUTO_INCREMENT,"
    		+ " `activity_date` datetime DEFAULT NULL,"
    		+ " `user_id` int(10) unsigned NOT NULL DEFAULT '0',"
    		+ " `message` varchar(255) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_last_update` (`last_update`)"
    		+ ") ENGINE=MyISAM CHARSET=utf8";
    
    public static final String CREATE_TABLE_CHECK_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_CHECK + " ("
    		+ " `id` int(10) NOT NULL AUTO_INCREMENT,"
    		+ " `name` varchar(80) NOT NULL,"
    		+ " `description` varchar(255) DEFAULT NULL,"
    		+ " `name_descriptive` varchar(255) DEFAULT NULL,"
    		+ " `package` varchar(80) DEFAULT NULL,"
    		+ " `class` varchar(80) DEFAULT NULL,"
    		+ " `check_single_field` tinyint(1) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " UNIQUE KEY (`class`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	
    public static final String CREATE_TABLE_CHECK_METHOD_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_CHECK_METHOD + " ("
    		+ " `id` int(10) NOT NULL AUTO_INCREMENT,"
    		+ " `check_id` int(10) NOT NULL,"
    		+ " `compare` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `compare_to` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `note` varchar(255) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `parameter1` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `parameter1_explanation` varchar(255) DEFAULT NULL,"
    		+ " `parameter2` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `parameter2_explanation` varchar(255) DEFAULT NULL,"
    		+ " `parameter3` varchar(80) CHARACTER SET latin1 DEFAULT NULL,"
    		+ " `parameter3_explanation` varchar(255) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_checkid` (`check_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_REFERENCE_FIELDS_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_REFERENCE_FIELDS + " ("
    		+ " `id` int(11) NOT NULL AUTO_INCREMENT,"
    		+ " `project_id` int(11) NOT NULL,"
    		+ " `name` varchar(80) DEFAULT NULL,"
    		+ " `name_descriptive` varchar(80) NOT NULL,"
    		+ " `description` varchar(120) DEFAULT NULL,"
    		+ " `java_type_id` int(1) NOT NULL,"
    		+ " `last_update_user_id` int(10) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " UNIQUE KEY `idx_name` (`project_id`,`name`),"
    		+ " KEY `idx_project_id` (`project_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_GROUPS_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUPS + " ("
    		+ " `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		+ " `name` varchar(45) NOT NULL DEFAULT '',"
    		+ " `description` varchar(255) NOT NULL DEFAULT '',"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_name` (`name`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_HISTORY_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " ("
    		+ " `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		+ " `type` varchar(20) NOT NULL,"
    		+ " `type_id` int(10) unsigned NOT NULL,"
    		+ " `user_id` int(10) DEFAULT NULL,"
    		+ " `parent_1` int(10) DEFAULT NULL,"
    		+ " `parent_2` int(10) DEFAULT NULL,"
    		+ " `parent_3` int(10) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_user_id` (`user_id`),"
    		+ " KEY `idx_type_typeid_userid` (`type`,`type_id`,`user_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_PROJECT_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_PROJECT + " ("
    		+ " `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		+ " `name` varchar(80) NOT NULL,"
    		+ " `description` varchar(255) DEFAULT NULL,"
    		+ " `export_filename` varchar(255) DEFAULT NULL,"
    		+ " `is_private` tinyint(1) DEFAULT '0',"
    		+ " `object_classname` varchar(80) DEFAULT NULL,"
    		+ " `object_method_getter` varchar(80) DEFAULT NULL,"
    		+ " `object_method_setter` varchar(80) DEFAULT NULL,"
    		+ " `database_hostname` varchar(80) DEFAULT NULL,"
    		+ " `database_name` varchar(80) DEFAULT NULL,"
    		+ " `database_tablename` varchar(80) DEFAULT NULL,"
    		+ " `database_userid` varchar(80) DEFAULT NULL,"
    		+ " `database_user_password` varchar(80) DEFAULT NULL,"
    		+ " `last_update_user_id` int(10) DEFAULT NULL,"
    		+ " `owner_user_id` int(10) DEFAULT NULL,"
    		+ " `group_id` int(10) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_RULE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_RULE + " ("
    		+ " `id` int(10) NOT NULL AUTO_INCREMENT,"
    		+ " `rulesubgroup_id` int(10) NOT NULL DEFAULT '0',"
    		+ " `check_id` int(10) DEFAULT NULL,"
    		+ " `last_update_user_id` int(10) DEFAULT NULL,"
    		+ " `name` varchar(80) NOT NULL,"
    		+ " `description` varchar(255) DEFAULT NULL,"
    		+ " `object1_parametertype_id` int(10) DEFAULT NULL,"
    		+ " `object1_parameter` varchar(255) DEFAULT NULL,"
    		+ " `object1_type_id` int(10) DEFAULT NULL,"
    		+ " `object2_parametertype_id` int(10) DEFAULT NULL,"
    		+ " `object2_parameter` varchar(255) DEFAULT NULL,"
    		+ " `object2_type_id` int(10) DEFAULT NULL,"
    		+ " `expectedvalue` mediumtext DEFAULT NULL,"
    		+ " `expectedvalue_type_id` int(10) DEFAULT NULL,"
    		+ " `additional_parameter` varchar(255) DEFAULT NULL,"
    		+ " `additional_parameter_type_id` int(10) DEFAULT NULL,"
    		+ " `message_passed` varchar(255) NOT NULL,"
    		+ " `message_failed` varchar(255) NOT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_rulesubgroup_id` (`rulesubgroup_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_RULEGROUP_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_RULEGROUP + " ("
    		+ " `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		+ " `project_id` int(10) unsigned NOT NULL DEFAULT '0',"
    		+ " `last_update_user_id` int(10) DEFAULT NULL,"
    		+ " `name` varchar(80) NOT NULL,"
    		+ " `description` varchar(255) NOT NULL,"
    		+ " `valid_from` date NOT NULL,"
    		+ " `valid_until` date NOT NULL,"
    		+ " `dependent_rulegroup_id` int(10) unsigned DEFAULT NULL,"
    		+ " `dependent_rulegroup_execute_if` varchar(6) DEFAULT NULL,"
    		+ " `disabled` tinyint(2) default 0," 
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_project_id` (`project_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_RULEGROUPACTION_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_RULEGROUPACTION + " ("
    		+ " `id` int(10) NOT NULL AUTO_INCREMENT,"
    		+ " `rulegroup_id` int(10) NOT NULL DEFAULT '0',"
    		+ " `last_update_user_id` int(10) DEFAULT NULL,"
    		+ " `action_id` int(10) NOT NULL,"
    		+ " `name` varchar(80) NOT NULL,"
    		+ " `description` varchar(255) DEFAULT NULL,"
    		+ " `object1_parametertype_id` int(10) DEFAULT NULL,"
    		+ " `object1_parameter` varchar(255) DEFAULT NULL,"
    		+ " `object1_type_id` int(10) DEFAULT NULL,"
    		+ " `object2_parametertype_id` int(10) DEFAULT NULL,"
    		+ " `object2_parameter` varchar(255) DEFAULT NULL,"
    		+ " `object2_type_id` int(10) DEFAULT NULL,"
    		+ " `object3_parametertype_id` int(10) DEFAULT NULL,"
    		+ " `object3_parameter` varchar(255) DEFAULT NULL,"
    		+ " `object3_type_id` int(10) DEFAULT NULL,"
    		+ " `parameter1` varchar(255) DEFAULT NULL,"
    		+ " `parameter1_type_id` int(10) DEFAULT NULL,"
    		+ " `parameter2` varchar(255) DEFAULT NULL,"
    		+ " `parameter2_type_id` int(10) DEFAULT NULL,"
    		+ " `parameter3` varchar(255) DEFAULT NULL,"
    		+ " `parameter3_type_id` int(10) DEFAULT NULL,"
    		+ " `execute_if` varchar(20) DEFAULT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_rulegroup_id` (`rulegroup_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";

    public static final String CREATE_TABLE_RULESUBGROUP_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_RULESUBGROUP + " ("
    		+ " `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		+ " `rulegroup_id` int(10) unsigned NOT NULL DEFAULT '0',"
    		+ " `last_update_user_id` int(10) DEFAULT NULL,"
    		+ " `name` varchar(80) NOT NULL,"
    		+ " `description` varchar(255) DEFAULT NULL,"
    		+ " `intergroupoperator` enum('and','or') NOT NULL,"
    		+ " `ruleoperator` enum('and','or') NOT NULL,"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " KEY `idx_rulegroup_id` (`rulegroup_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	
    public static final String CREATE_TABLE_TYPES_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_TYPES + " ("
			+ " `id` int(19) NOT NULL AUTO_INCREMENT,"
			+ " `name` varchar(20) DEFAULT NULL,"
			+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
			+ " PRIMARY KEY (`id`)"
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_USER_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ("
    		+ " `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		+ " `userid` varchar(25) NOT NULL,"
    		+ " `name` varchar(80) NOT NULL DEFAULT '',"
    		+ " `password` varchar(255) DEFAULT NULL,"
    		+ " `email` varchar(80) DEFAULT NULL,"
    		+ " `generated_code` VARCHAR(40) NULL DEFAULT NULL,"
    		+ " `lastlogin` datetime DEFAULT NULL,"
    		+ " `deactivated` tinyint(1) unsigned DEFAULT '0',"
    		+ " `deactivated_date` date DEFAULT '0000-00-00',"
    		+ " `password_update_date` date DEFAULT '0000-00-00',"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " UNIQUE KEY `idx_userid` (`userid`),"
    		+ " KEY `idx_deactivated` (`deactivated`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";

    public static final String CREATE_TABLE_GROUPUSER_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUPUSER + " ("
    		+ " `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		+ " `user_id` int(10) unsigned NOT NULL DEFAULT '0',"
    		+ " `groups_id` int(10) unsigned NOT NULL DEFAULT '0',"
    		+ " `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
    		+ " PRIMARY KEY (`id`),"
    		+ " UNIQUE KEY `idx_groupuser` (`user_id`,`groups_id`),"
    		+ " KEY `idx_user` (`user_id`),"
    		+ " KEY `idx_group` (`groups_id`)"
    		+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_TABLE_RULEGROUP_TESTDATA = "CREATE TABLE IF NOT EXISTS " + TABLE_RULEGROUP_TESTDATA + " ("
    		  +" `id` int(10) unsigned NOT NULL AUTO_INCREMENT,"
    		  +" `rulegroup_id` int(10) unsigned NOT NULL,"
    		  +" `user_id` int(10) unsigned NOT NULL,"
    		  +" `value` mediumtext DEFAULT NULL,"
    		  +" `last_update` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),"
    		  +" PRIMARY KEY (`id`),"
    		  +" KEY `idx_rulegroup_user_id` (`rulegroup_id`,`user_id`)"
    		  +") ENGINE=MyISAM DEFAULT CHARSET=utf8";
    
    public static final String CREATE_GROUPS = "INSERT INTO " + TABLE_GROUPS + " VALUES (1,'Admin','Admin Group',now())";
    
    public static final String CREATE_USERS = "INSERT INTO " + TABLE_USER + " VALUES "
    		+ "(1,'admin','admin user','*4ACFE3202A5FF5CF467898FC58AAB1D615029441',NULL,NULL,'2000-01-01 00:00:00',0,NULL,'2000-01-01',now())";
    
    public static final String CREATE_GROUPUSERS = "INSERT INTO " + TABLE_GROUPUSER + " VALUES (1,1,1,now())";
    
    public static final String CREATE_TYPES =  "INSERT INTO " + TABLE_TYPES + " VALUES (1,'string',now()),"
    			+ "(2,'integer',now()),"
    			+ "(3,'float',now()),"
    			+ "(4,'double',now()),"
    			+ "(5,'boolean',now()),"
    			+ "(6,'long',now()),"
    			+ "(7,'bigdecimal',now()),"
    			+ "(8,'date',now())";
    }
