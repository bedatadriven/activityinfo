<#include "Scaffolding.ftl">
<@scaffolding title="${label.chooseNewPassword}">

	<@content>
	<div class="row-fluid">
		<div class="span12">
		
			<h3>${label.chooseNewPassword}</h3>
			
			<form class="form" method="post" id="loginForm" action="changePassword" method="post">
		    	<input type="hidden" name="key" value="${user.changePasswordKey}"></input>
		
				<div class="control-group">
				    <label class="control-label" for="inputEmail">${label.newPassword}</label>
				    <div class="controls">
				      <input type="password" name="password" id="inputPassword">
				    </div>
				  </div>
				  <div class="control-group">
				    <label class="control-label" for="inputPassword">${label.confirmNewPassword}</label>
				    <div class="controls">
				      <input type="password" id="inputPassword">
				    </div>
				  </div>
				</div>
				<div class="control-group">
			    <div class="controls">

			      <button type="submit" class="btn btn-primary btn-large">${label.continue}  &raquo;</button>
			    </div>
			  </div>
			</form>
		</div>
	</div>
	</@content>

</@scaffolding>