<#include "Scaffolding.ftl">
<@scaffolding title="${label.login}">

	<@content>

    <div id="main" class="span12 clearfix" role="main">
      <br>
      <div class="row-fluid">
        <div class="span8">
          <h3>${label.whatIsActivityInfo}</h3>
          <p class="lead">${label.activityInfoIntro}</p>
          
          <div class="row-fluid">
          	<div class="span3"><a href="http://about.activityinfo.org/<#if .lang="fr">fr</#if>" class="btn btn-large btn-success">${label.learnMore} &raquo;</a></div>
          	
          </div>
		  
        </div>
 		<div class="span4">										
			<form class="form-signin" id="loginForm" action="http://www.activityinfo.org/login" method="POST">
				<h3 class="form-signin-heading">${label.login}</h3>
			   	<input type="text" name="email" id="emailInput" class="input-block-level" placeholder="${label.emailAddress}">
               	<input type="password" name="password" id="passwordInput" class="input-block-level" placeholder="${label.password}">

			 	<div class="alert alert-error <#if !loginError>hide</#if>" id="loginAlert">
					${label.incorrectLogin}
			  	</div>
			  	
			  	<button class="btn btn-info btn-primary btn-large" type="submit" id="loginButton">${label.login}</button> 
                <img src="img/ajax-loader-spinner.gif" width="16" height="16" class="hide" id="loginSpinner">
			
				<div class="login-problem"><a href="loginProblem">${label.forgottenYourPassword}</a></div>

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
	</@scripts>
</@scaffolding>