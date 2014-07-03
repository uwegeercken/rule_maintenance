rule_maintenance
================

Web application to manage rules for the Business Rule Engine - Jare.

On the highest level there are projects. A project contains one to many rule groups. A rule group again consists of one to many subgroups. Rules in a subgroup are connected with an "and" or "or" condition. Subgroups are connected to each other also using an "and" or "or" condition. This way the logic of a rule group can be defined in a very flexible way. So the rule group ultimately defines a set of logic and all rule groups in a project are excuted one after the other to check the data for validity and conformity.

Rule groups also have a validity date, defining from which date until which other date the group is valid. This allows to define time frames in which rules are to be used or not.


Note 1: make sure that you have imported the database schema.
Note 2: adjust the web.xml file - containing database connection information - according to your local settings.

