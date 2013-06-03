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
<#macro scaffolding title leaflet=false>
<!DOCTYPE html>

<html>
<head>
  <title>${title}</title>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  
  <!-- CSS Files -->
  <link href="/css/bootstrap.css" rel="stylesheet" media="screen">
  <link href="/css/bootstrap-responsive.css" rel="stylesheet" media="screen">
  <link href="/css/style.css" rel="stylesheet" media="screen">
  
  <#if leaflet>
  <script src="/js/leaflet-0.5.min.js"></script>
  <link rel="stylesheet" href="/css/leaflet-0.5.css" />
  <!--[if lte IE 8]>
      <link rel="stylesheet" href="/css/leaflet-0.5.ie.css" />
  <![endif]-->
	 
  </#if>
  
</head>
<body>
    <header role="banner">
    
      <div id="inner-header" class="clearfix">
        
        <div class="navbar navbar-static-top">
          <div class="navbar-inner">
            <div class="container-fluid nav-container">
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
                    <a data-toggle="modal" href="#loginModal" class="btn">${label.login}</a>
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
    
    </header> <!-- end header -->

	<#nested>


	  
</body>
</html>
</#macro>

<#macro content>
    <div class="container-fluid">
		<div id="content" class="clearfix row-fluid">

		<#nested>
		
	  
    	</div> <!-- end #content -->
	</div>
</#macro>

<#macro scripts>
  <script src="/js/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="/js/bootstrap-modal-2.3.2-min.js"></script>
	
  <script type="text/javascript">
	 
  		$('.language').click(function() {
  			var language = $(this).text();
  			var now = new Date();
   			now.setMonth( now.getMonth() + 1 );
   			document.cookie="locale" + "=" + language +";expires=" + now.toUTCString();
	 	});
	 	
	 	// login popup
		 	
		var enableForm = function(enabled) {
			$('#loginButton').prop('disabled', !enabled);
			$('#loginSpinner').toggleClass('hide', enabled);
		}	
	
		$('#loginForm').submit(function() {
			
			$('#loginAlert').addClass('hide');
		
			enableForm(false);		
			$.ajax({
				url: '/login/ajax',
				type: 'POST', 
				data: {
					email: $('#emailInput').val(),
					password: $('#passwordInput').val(),
					ajax: 'true'
				},
				success: function() {
					if(window.location.pathname != '/') {
						window.location = '/' + window.location.search + window.location.hash;
					} else {
						window.location.reload(true);
					}
				},
				error: function(xhr) {
					$('#loginAlert').toggleClass('hide', false);
				},
				complete: function() {
					enableForm(true);
				}
			});
			return false;
		});
		
		$('#emailInput').focus();
	 	
  </script>
  <#nested>
</#macro>

<#macro footer>

  <div style="border-top: 1px solid #E3E3E3; padding-top: 10px; margin-top: 25px;">
    <footer role="contentinfo" class="container-fluid">
      <div id="inner-footer" class="row-fluid">
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
  
  <div id="loginModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&#215;</button>
          <h3 id="myModalLabel">Login</h3>
      </div>
      <div class="modal-body">
          <div class="alert alert-error hide" id="loginAlert">
          ${label.incorrectLogin}
          </div>
          <form class="form-horizontal" id="loginForm" action="/login" method="POST">
              <div class="control-group" id="emailGroup">
                  <label class="control-label" for="emailInput">${label.emailAddress}</label>
                  <div class="controls">
                      <input type="text" id="emailInput" name="email" placeholder="${label.emailAddress}">
                      <span class="help-inline hide" id="emailHelp">Please enter your email address</span>
                  </div>
              </div>
              <div class="control-group" id="passwordGroup">
                  <label class="control-label" for="passwordInput">${label.password}</label>
                  <div class="controls">
                      <input type="password" name="password" id="passwordInput" placeholder="${label.password}">
                      <span class="help-inline hide" id="passwordHelp">Please enter your password</span>
                  </div>
              </div>
              <div class="control-group">
                  <div class="controls">
                      <button id="loginButton" type="submit" class="btn btn-primary btn-large">${label.login} &raquo;</button>
                      <img src="/img/ajax-loader-spinner.gif" width="16" height="16" class="hide" id="loginSpinner">

                      <div class="login-problem" style="margin-top:25px">
                          <a href="loginProblem">${label.forgottenYourPassword}</a>
                      </div>
                  </div>
              </div>
          </form>
      </div>
  </div>
  
</#macro>