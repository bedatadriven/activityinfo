<#include "Scaffolding.ftl">
<@scaffolding title="Login">

	<@content>
	<div class="row-fluid">
		<div class="span12">
			<div class="page-header"><h1 class="page-title">Login</h1></div>
			<div>
			
				<div class="span8">
						
				<div class="alert alert-error <#if !loginError>hide</#if>" id="loginAlert">
					The login is not correct, please double check your email and password.
				</div>		
						
				 <form class="form-horizontal" id="loginForm" action="http://www.activityinfo.org/login" method="POST">
				  <div class="control-group" id="emailGroup">
					<label class="control-label" for="emailInput">Email</label>
					<div class="controls">
					  <input type="text" id="emailInput" name="email" placeholder="Email">
					  <span class="help-inline hide" id="emailHelp">Please enter your email address</span>
					</div>
				  </div>
				  <div class="control-group" id="passwordGroup">
					<label class="control-label" for="passwordInput">Password</label>
					<div class="controls">
					  <input type="password" name="password" id="passwordInput" placeholder="Password">
					  <span class="help-inline hide" id="passwordHelp">Please enter your password</span>
					</div>
				  </div>
				  <div class="control-group">
					<div class="controls">
					  <label class="checkbox">
						<input type="checkbox"> Remember me
					  </label>
					  <button id="loginButton" type="submit" class="btn btn-primary btn-large">Log in &raquo;</button> 
					  <img src="img/ajax-loader-spinner.gif" width="16" height="16" class="hide" id="loginSpinner">
					</div>
				  </div>
				</form>
	          </div>
	          <div class="span4">
	          	
	          	<div class="well">
	          		<h3>Forgot your password?</h3>
	          
	          		<a href="loginProblem" class="btn">Reset your password</a>
	          	 </div>
	          </div>
			
			</div>
		
	</div>
	</@content>
	<@scripts>
	<script type="text/javascript">
	
	var validateEmail = function() {
		var email = $('#emailInput').val();
		var valid = email && email.length > 3 && email.indexOf('@') != -1;
		$('#emailGroup').toggleClass('error', !valid);
		$('#emailHelp').toggleClass('hide', valid);
		
		return valid;
	};
	
	var validatePassword = function() {
		var valid = !! $('#passwordInput').val();
		$('#passwordGroup').toggleClass('error', !valid);
		$('#passwordHelp').toggleClass('hide', valid);
		return valid;
	};
	$('#emailInput').change(validateEmail);	
	
	var enableForm = function(enabled) {
		$('#loginButton').prop('disabled', !enabled);
		$('#loginSpinner').toggleClass('hide', enabled);
	}	

	$('#loginForm').submit(function() {
		
		$('#loginAlert').addClass('hide');
		
		if(validateEmail() && validatePassword()) {
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
					window.location = '/' + window.location.search;
				},
				error: function(xhr) {
					$('#loginAlert').toggleClass('hide', false);
				},
				complete: function() {
					enableForm(true);
				}
			});
		}
		return false;
	});
	</script>
	</@scripts>
</@scaffolding>