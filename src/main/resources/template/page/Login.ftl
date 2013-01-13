<#include "Scaffolding.ftl">
<@scaffolding title="Login">

	<@content>

    <div id="main" class="span12 clearfix" role="main">
      <br>
      <div class="row-fluid">
        <div class="span8">
          <h3>What is ActivityInfo?</h3>
          <p class="lead">ActivityInfo is an open-source, web-based humanitarian monitoring tool that helps humanitarian organizations to collect, manage, map and analyze indicators.</p>
          
          <div class="row-fluid">
          	<div class="span3"><a href="http://about.activityinfo.org" class="btn">Learn more &raquo;</a></div>
          	
          </div>
		  
        </div>
 		<div class="span4">										
			<form class="form-signin" id="loginForm" action="http://www.activityinfo.org/login" method="POST">
				<h3 class="form-signin-heading">${label.login}</h3>
			   	<input type="text" name="email" id="emailInput" class="input-block-level" placeholder="Email address">
               	<input type="password" name="password" id="passwordInput" class="input-block-level" placeholder="Password">

			 	<div class="alert alert-error <#if !loginError>hide</#if>" id="loginAlert">
					The login is not correct, please double check your email and password.
			  	</div>
               	<label class="checkbox">
                	<input type="checkbox" value="remember-me"> Remember me
                </label>			  			
                <button class="btn btn-info btn-primary btn-large" type="submit" id="loginButton">Log in</button> 
                <img src="img/ajax-loader-spinner.gif" width="16" height="16" class="hide" id="loginSpinner">
			
				<div class="login-problem"><a href="loginProblem">Forgotten your password?</a></div>

			</form>
      	</div>
      </div>	
	</div>
	</@content>
	<@scripts>
	<script type="text/javascript">
	
	
	var enableForm = function(enabled) {
		$('#loginButton').prop('disabled', !enabled);
		$('#loginSpinner').toggleClass('hide', enabled);
	}	

	$('#loginForm').submit(function() {
		
		$('#loginAlert').addClass('hide');
	
		enableForm(false);		
		$.ajax({
			url: '/login',
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
	</@scripts>
</@scaffolding>