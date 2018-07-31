rule_maintenance
================

Web application to manage rules for the Business Rule Engine - Jare.

On the highest level there are projects. A project contains one to many rule groups. A rule group again consists of one to many subgroups. Rules in a subgroup are connected with an "and" or "or" condition. Subgroups are connected to each other also using an "and" or "or" condition. This way the logic of a rule group can be defined in a very flexible way. So the rule group ultimately defines a set of logic and all rule groups in a project are excuted one after the other to check the data for validity and conformity.

Rule groups also have a validity date, defining from which date until which other date the group is valid. This allows to define time frames in which rules are to be used or not.

Reference fields may be assigned to a project. As a consequence the user does not need to type in field names and types - when writing rules or actions - but may select the fields from a dropdown.

The application allows to use LDAP for authentication. Projects can be private or read-only. Roles can be setup that define access to projects and the other components.

The latest version has enhanced user support: filtering on pages, storing filters and a calendar widget. New is also a project copy function and a search function for rules and actions.

Make sure you have a MySQL or Mariadb server running. You will need a user and user password which allows to create the database, tables and some initial data.


    Copyright (C) 2008-2018  Uwe Geercken

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.

Uwe Geercken - uwe.geercken@web.de
last update: 2018-07-31

