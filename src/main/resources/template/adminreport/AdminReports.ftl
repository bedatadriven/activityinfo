<#--
 #%L
 ActivityInfo Server
 %%
 Copyright (C) 2009 - 2013 UNICEF
 %%
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the 
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public 
 License along with this program.  If not, see
 <http://www.gnu.org/licenses/gpl-3.0.html>.
 #L%
-->
<#include "../page/Scaffolding.ftl">
<@scaffolding title="Administrative Reports">

    <@content>
        <h3>Select an administrative report</h3>
        <ul>
            <li>Users: <a href="/admin/reports/users/mostActive">Most active</a> | <a href="/admin/reports/users/recentlyActive">Recently active</a></li>
            <li>Activities: <a href="/admin/reports/activities/mostActive">Most active</a> | <a href="/admin/reports/activities/recentlyActive">Recently active</a></li>
            <li>Databases: <a href="/admin/reports/databases/mostActive">Most active</a> | <a href="/admin/reports/databases/recentlyActive">Recently active</a></li>
        </ul>
    </@content>

    <@footer/>
    
    <@scripts>
    </@scripts>
</@scaffolding>
