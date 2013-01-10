<#include "Scaffolding.ftl">
<@scaffolding title="Welcome to ActivityInfo">

	<@content>
	<div class="row-fluid">
		<div class="span12">
		
			<h2>Welcome to ActivityInfo</h2>
			
			<p class="well">Before we get started, let's set up your account. Confirm your name and preferred language,
    			and then choose a password.</p>
			
			<form class="form-horizontal" action="" method="post" id="confirmForm">
			  <input type="hidden" name="key" value="${user.changePasswordKey}"></input>
			
			  <div class="control-group" id="nameGroup">
			    <label class="control-label" for="nameInput">Confirm your name:</label>
			    <div class="controls">
			      <input type="text" name="name" id="nameInput" value="${user.name}" >
			      <span class="help-inline hide" id="nameHelp">Please enter your full name</span>
			    </div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="inputEmail">Confirm your preferred language:</label>
			    <div class="controls">
                <select name="locale">
                    <option value="en">English</option>
                    <option value="fr">Français</option>
                </select>
       			</div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="passwordInput">Choose a password:</label>
			    <div class="controls">
			      <input type="password" name="password" id="passwordInput" placeholder="Password">
			      <span class="help-inline hide" id="passwordHelp">Password must be at least six characters.</span>
			      
			    </div>
			  </div>
			  <div class="control-group" id="confirmPasswordGroup">
			    <label class="control-label" for="confirmPasswordInput">Confirm your password:</label>
			    <div class="controls">
			      <input type="password" name="password2" id="confirmPasswordInput" placeholder="Password">
			      <span class="help-inline hide" id="confirmPasswordHelp">The passwords do not match</span>			      
			    </div>
			  </div>
			  <div class="control-group">
			    <div class="controls">

			      <button type="submit" class="btn btn-primary btn-large">Continue  &raquo;</button>
			    </div>
			  </div>
			</form>
				
		</div>
	</div>
	</@content>
	<@scripts>
	<script type="text/javascript">
	
		var validateName = function() {
			var valid = !!( $('#nameInput').val() );
			$('#nameGroup').toggleClass('error', !valid);
			$('#nameHelp').toggleClass('hide', valid);
			return valid;
		};
		
		var validatePass = function() {
			var pass1 = $('#passwordInput').val();
			var pass2 = $('#confirmPasswordInput').val();
			
			var valid = pass1 && pass1.length >= 6;
			$('#passwordGroup').toggleClass('error', !valid);
			$('#passwordHelp').toggleClass('hide', valid);
			
			var confirmed = pass2 && (pass1 == pass2);
			$('#confirmPasswordGroup').toggleClass('error', !confirmed);
			$('#confirmPasswordHelp').toggleClass('hide', confirmed);
			
			return valid && confirmed;
		};
	
		$("#nameInput").change(validateName);
		$("#passwordInput").change(validatePass);
		$("#confirmPasswordInput").change(validatePass);
		$("#confirmForm").submit(function() {
		  var valid = validateName() && validatePass();
		  return !!valid;
		});
	</script>
	</@scripts>
</@scaffolding>
