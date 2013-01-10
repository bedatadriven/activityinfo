<#include "Scaffolding.ftl">
<@scaffolding title="Choose a new password">

	<@content>
	<div class="row-fluid">
		<div class="span12">
		
			<div class="page-header"><h1 class="page-title">Choose a new password</h1></div>
			
			<form class="form" method="post" id="loginForm" action="changePassword" method="post">
		    	<input type="hidden" name="key" value="${user.changePasswordKey}"></input>
		
				<div class="control-group">
				    <label class="control-label" for="inputEmail">New password</label>
				    <div class="controls">
				      <input type="password" name="password" id="inputPassword">
				    </div>
				  </div>
				  <div class="control-group">
				    <label class="control-label" for="inputPassword">Confirm your new password</label>
				    <div class="controls">
				      <input type="password" id="inputPassword">
				    </div>
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

</@scaffolding>