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
<@scaffolding title="${title}">

    <@content>
        <h5>&#171; <a href="/admin/reports">Administrative Reports</a></h5>
      
        <h3>${title}</h3>

        <#if hint??>
        <em>${hint}</em>
        </#if>
                
        <table id="report" class="display">
            <thead>
                <tr>
                    <#list headers as header>
                    <th>${header}</th>
                    </#list>
                </tr>
            </thead>
            <tbody>
                <#list rows as row>
                <tr>
                    <#list row as field> 
                    <td>${field!'unknown'}</td>
                    </#list>
                </tr>
                </#list>
            </tbody>
        </table>
            
    </@content>

    <@footer/>
    
    <@scripts>
        <script type="text/javascript" src="/js/datatables-1.9.4/jquery.dataTables.min.js"></script>
        <script type="text/javascript">
            $(document).ready( function () {
                $('#report').dataTable({
                    "bPaginate": false,
                    "bLengthChange": false,
                    "bFilter": true,
                    "bSort": false,
                    "bInfo": false,
                    "bAutoWidth": false 
                });
            } );
        </script>
    </@scripts>
</@scaffolding>
