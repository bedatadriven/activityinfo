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

<!DOCTYPE html>
<html>
<head>
  <title>${title}</title>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  
  <!-- CSS Files -->
  <link href="/css/style.css" rel="stylesheet" media="screen">
  <link href="/js/datatables-1.9.4/jquery.dataTables.css" rel="stylesheet" media="screen">
  
  <script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-11567120-1']);
  _gaq.push(['_setDomainName', 'activityinfo.org']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
  </script>
</head>
<body>
	<div id="wrap">

<header role="banner">

  <div id="inner-header" class="clearfix">
    
    <div class="navbar navbar-static-top">
      <div class="navbar-inner">
        <div class="container nav-container">
          <nav role="navigation">
            <a class="brand" href="http://about.activityinfo.org" style=""><img src="/img/logo-shadow.png"><span>${label.activityInfo}</span></a>
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
            </a>
            <div class="pull-right">
             <ul class="upper nav">
              <li  class="language <#if .lang="en">active</#if>"><a href="">en</a></li>
              <li  class="language <#if .lang="fr">active</#if>"><a href="">fr</a></li>
              <li class="normal-header">
                <a href="/login" class="btn">${label.login}</a>
              </li>
              <li>
                <a href="/signUp" class="btn">${label.signUpButton}</a>
              </li>
             </ul>
            </div>
            <div class="nav-collapse pull-right normal-header">
              <ul id="menu-activity-info" class="nav">
                <li><a href="//about.activityinfo.org/">Home</a></li>
                <li><a href="//about.activityinfo.org/feature/flexible/">Features</a></li>
                <li><a href="//about.activityinfo.org/case-studies/">Who uses AI?</a></li>
                <li><a href="//about.activityinfo.org/learn/">Learning Center</a></li>
              </ul>
            </div>
          </nav>
        </div>
      </div>
	</div><!-- /.navbar -->
  
  </div> <!-- end #inner-header -->
	
	  ${body}
	  
	  <div id="push"></div>
	</div>  
	  
  <div id="footer">
    <footer role="contentinfo" class="container">
      <div id="inner-footer" class="row">
        <div class="span3">
          <div id="text-4" class="widget widget_text">
            <h4>Developed by</h4>
            <div class="textwidget">
              <p><a href="http://www.unicef.org" target="_blank">
                <img src="/img/unicef-logo.png"></a></p>
              <p><a href="http://www.bedatadriven.com" target="_blank">
                <img src="/img/bdd.png"></a></p>
            </div>
          </div>
        </div>
        <div class="span3">
          <div id="text-5" class="widget widget_text">
            <h4>Partners</h4>
            <div class="textwidget">
              <p><a href="http://www.unocha.org" target="_blank">
                <img src="//about.activityinfo.org/wp-content/uploads/2011/10/OCHA-Logo.png"></a>
              </p>
            </div>
          </div>
        </div>
        <div class="span3">
          &nbsp;
        </div>
        <div class="span3">
        </div>
      </div>
    </div>
    </footer> <!-- end footer -->
  </div>  
</body>
</html>
