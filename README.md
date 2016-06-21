rule_maintenance
================

Web application to manage rules for the Business Rule Engine - Jare.

On the highest level there are projects. A project contains one to many rule groups. A rule group again consists of one to many subgroups. Rules in a subgroup are connected with an "and" or "or" condition. Subgroups are connected to each other also using an "and" or "or" condition. This way the logic of a rule group can be defined in a very flexible way. So the rule group ultimately defines a set of logic and all rule groups in a project are excuted one after the other to check the data for validity and conformity.

Rule groups also have a validity date, defining from which date until which other date the group is valid. This allows to define time frames in which rules are to be used or not.

Reference fields may be assigned to a project. As a consequence the user does not need to type in field names and types - when writing rules or actions - but may select the fields from a dropdown.

The application allows to use LDAP for authentication. Projects can be private or read-only. Roles can be setup that define access to projects and the other components.

The latest version has enhanced user support: filtering on pages, storing filters and a calendar widget. New is also a project copy function and a search function for rules and actions.

Note 1: make sure that you have imported the database schema before you run the web application.

   Copyright (C) 2008-2016  Uwe Geercken
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses

Uwe Geercken - uwe.geercken@web.de
last update: 2016-06-21
