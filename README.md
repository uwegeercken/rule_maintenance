rule_maintenance
================

Web application to manage rules for the Business Rule Engine - Jare.

On the highest level there are projects. A project contains one to many rule groups. A rule group again consists of one to many subgroups. Rules in a subgroup are connected with an "and" or "or" condition. Subgroups are connected to each other also using an "and" or "or" condition. This way the logic of a rule group can be defined in a very flexible way. So the rule group ultimately defines a set of logic and all rule groups in a project are excuted one after the other to check the data for validity and conformity.

Rule groups also have a validity date, defining from which date until which other date the group is valid. This allows to define time frames in which rules are to be used or not.


Note 1: make sure that you have imported the database schema.
Note 2: adjust the web.xml file - containing database connection information - according to your local settings.

   Copyright (C) 2008-2014  Uwe Geercken
    
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
last update: 2014-08-06
