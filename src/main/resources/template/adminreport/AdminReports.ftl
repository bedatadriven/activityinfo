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
        <div class="row">
            <div class="span12">
                <h3>Administrative Reports</h3>
            </div>
        </div>
        <div class="row">
            <div class="span12">
                <ul>
                    <li><a href="/admin/reports/recentUpdates">Recent updates</a></li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="span4">
                <h5>Users</h5>
                <ul>
                    <li><a href="/admin/reports/users/mostActive">Most active</a></li>
                    <li><a href="/admin/reports/users/recentlyActive">Recently active</a></li>
                    <li><a href="/admin/reports/users/mostActiveThisMonth">Most active this month</a></li>
                    <li><a href="/admin/reports/users/mostActiveThisQuarter">Most active this quarter</a></li>
                    <li><a href="/admin/reports/users/mostActiveThisYear">Most active this year</a></li>
                    <li><a href="/admin/reports/users/mostActiveSince">Most active in the last 30 days</a></li>
                    <li><a href="/admin/reports/users/mostActiveSince?days=90">Most active in the last 90 days</a></li>
                    <li><a href="/admin/reports/users/mostActiveSince?days=365">Most active in the last 365 days</a></li>
                </ul>
                <ul>
                    <li><a href="/admin/reports/users/leastActive">Least active</a></li>
                    <li><a href="/admin/reports/users/leastRecentlyActive">Least recently active</a></li>
                    <li><a href="/admin/reports/users/leastActiveThisMonth">Least active this month</a></li>
                    <li><a href="/admin/reports/users/leastActiveThisQuarter">Least active this quarter</a></li>
                    <li><a href="/admin/reports/users/leastActiveThisYear">Least active this year</a></li>
                    <li><a href="/admin/reports/users/leastActiveSince">Least active in the last 30 days</a></li>
                    <li><a href="/admin/reports/users/leastActiveSince?days=90">Least active in the last 90 days</a></li>
                    <li><a href="/admin/reports/users/leastActiveSince?days=365">Least active in the last 365 days</a></li>
                </ul>
                <ul>
                    <li><a href="/admin/reports/users/withoutHistory">Without history</a></li>
                </ul>
            </div>
            <div class="span4">
                <h5>Activities</h5>
                <ul>
                    <li><a href="/admin/reports/activities/mostActive">Most active</a></li>
                    <li><a href="/admin/reports/activities/recentlyActive">Recently active</a></li> 
                    <li><a href="/admin/reports/activities/mostActiveThisMonth">Most active this month</a></li>
                    <li><a href="/admin/reports/activities/mostActiveThisQuarter">Most active this quarter</a></li>
                    <li><a href="/admin/reports/activities/mostActiveThisYear">Most active this year</a></li>
                    <li><a href="/admin/reports/activities/mostActiveSince">Most active in the last 30 days</a></li>
                    <li><a href="/admin/reports/activities/mostActiveSince?days=90">Most active in the last 90 days</a></li>
                    <li><a href="/admin/reports/activities/mostActiveSince?days=365">Most active in the last 365 days</a></li>
                </ul>
                <ul>
                    <li><a href="/admin/reports/activities/leastActive">Least active</a></li>
                    <li><a href="/admin/reports/activities/leastRecentlyActive">Least recently active</a></li>
                    <li><a href="/admin/reports/activities/leastActiveThisMonth">Least active this month</a></li>
                    <li><a href="/admin/reports/activities/leastActiveThisQuarter">Least active this quarter</a></li>
                    <li><a href="/admin/reports/activities/leastActiveThisYear">Least active this year</a></li>
                    <li><a href="/admin/reports/activities/leastActiveSince">Least active in the last 30 days</a></li>
                    <li><a href="/admin/reports/activities/leastActiveSince?days=90">Least active in the last 90 days</a></li>
                    <li><a href="/admin/reports/activities/leastActiveSince?days=365">Least active in the last 365 days</a></li>
                </ul>
                <ul>
                    <li><a href="/admin/reports/activities/withoutHistory">Without history</a></li>
                </ul>
                
            </div>
            <div class="span4">
                <h5>Databases</h5>
                <ul>
                    <li><a href="/admin/reports/databases/mostActive">Most active</a></li> 
                    <li><a href="/admin/reports/databases/recentlyActive">Recently active</a></li>
                    <li><a href="/admin/reports/databases/mostActiveThisMonth">Most active this month</a></li>
                    <li><a href="/admin/reports/databases/mostActiveThisQuarter">Most active this quarter</a></li>
                    <li><a href="/admin/reports/databases/mostActiveThisYear">Most active this year</a></li>
                    <li><a href="/admin/reports/databases/mostActiveSince">Most active in the last 30 days</a></li>
                    <li><a href="/admin/reports/databases/mostActiveSince?days=90">Most active in the last 90 days</a></li>
                    <li><a href="/admin/reports/databases/mostActiveSince?days=365">Most active in the last 365 days</a></li>
                </ul>
                <ul>
                    <li><a href="/admin/reports/databases/leastActive">Least active</a></li> 
                    <li><a href="/admin/reports/databases/leastRecentlyActive">Least recently active</a></li>
                    <li><a href="/admin/reports/databases/leastActiveThisMonth">Least active this month</a></li>
                    <li><a href="/admin/reports/databases/leastActiveThisQuarter">Least active this quarter</a></li>
                    <li><a href="/admin/reports/databases/leastActiveThisYear">Least active this year</a></li>
                    <li><a href="/admin/reports/databases/leastActiveSince">Least active in the last 30 days</a></li>
                    <li><a href="/admin/reports/databases/leastActiveSince?days=90">Least active in the last 90 days</a></li>
                    <li><a href="/admin/reports/databases/leastActiveSince?days=365">Least active in the last 365 days</a></li>
                </ul>
                <ul>
                    <li><a href="/admin/reports/databases/withoutHistory">Without history</a></li>
                </ul>
            </div>
        </div>
    </@content>

    <@footer/>
    
    <@scripts>
    </@scripts>
</@scaffolding>
