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
<#include "Scaffolding.ftl">
<@scaffolding title="ActivityInfo">

  <div class="jumbotron masthead" style="margin-bottom:30px">
      <div class="container-fluid">
          <div class="row-fluid">
              <div class="span6">
                  <h1>Welcome to ActivityInfo</h1>
                  <p class="lead">Many humanitarian programs struggle with the challenge of collecting basic data about
                      their activities, as old methods of exchanging Excel and Word files by email prove time-consuming
                      and error-prone.</p>

                  <p class="lead">ActivityInfo is an online humanitarian project monitoring tool, which helps humanitarian
                      organizations to collect, manage, map and analyze indicators. ActivityInfo has been developed to
                      simplify reporting and allow for real time monitoring.</p>


                  <div>
                      <a href="/signUp" class="btn btn-large">Try it now</a> or
                      <a href="//about.activityinfo.org/?feature=database-builder" class="btn btn-large">Take the tour</a></div>
              </div>

              <div class="span6">
                  <img src="/img/front.png" width="548" height="433" style="position:absolute;top:-50px">
              </div>
          </div>
      </div>
  </div>


  <div class="container-fluid news-home">

      <div class="row-fluid">
          <div class="span12">
              <h2>News &amp; Announcements</h2>
          </div>
      </div>
      <div class="row-fluid">

          <div class="span3">

              <h3><a href="//about.activityinfo.org/2013/05/31/introducing-the-new-learning-center/">
                  Introducing the new Learning Center</a></h3>

              <p>We've just revamped the Learning Center section of the website, centralizing all of the resources
                  that will help you get the most out of ActivityInfo, including our very popular training videos.<br />
              </p>


          </div>

          <div class="span3">
              <h3><a href="//about.activityinfo.org/2013/03/05/activityinfo-org-reaches-99-99-availability/">
                  Activityinfo.org reaches 99.99% Availability</a></h3>

              <p>Over the past six months, BeDataDriven has invested in improving the availability of Activityinfo.org,
                  and we're pleased to see these efforts pay off with 99.99% availability over the past month.</p>
          </div>

          <div class="span3">

              <h3><a href="//about.activityinfo.org/2013/02/28/activityinfo-in-madagascar/">
                  ActivityInfo in Madagascar</a></h3>

              <p>ActivityInfo has been chosen as the basis of a common information system for emergency response in
                  Madagascar. It will bring together the National Disaster Office and the cluster system to support
                  needs assessment, coordination, and the monitoring &amp; evaluation of emergency responses.</p>
          </div>

          <div class="span3">
              <h3><a href="//about.activityinfo.org/build">Development Roundup</a></h3>

              <p>Read about the latest updates and fixes
                  released to ActivityInfo.org this week.</p>
          </div>

      </div>
  	</div>

	<@footer/>
	<@scripts>
	<script type="text/javascript">
	<#if showLogin>
		$('#loginModal').modal('show');
	<#else>
		if(location.hash) {
			$('#loginModal').modal('show');
			$('#emailInput').focus();
		}
	</#if>
	</script>
	</@scripts>
</@scaffolding>