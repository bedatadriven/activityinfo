<#include "Scaffolding.ftl">
<@scaffolding title="${label.resetYourPassword}">

	<@content>
	<div class="row-fluid">
		<div class="span12">
		
			<h3>${label.resetYourPassword}</h3>
			
			<p>${label.resetDetailMessage}</p>
				
			<form class="form-inline" action="loginProblem" method="POST">
			  <input type="text" name="email" class="input-xlarge" placeholder="my.name@ngo.org">
			  <button type="submit" class="btn btn-primary">${label.reset}</button>
			</form>
		
            <#if loginError == true>
	            <div class="alert alert-error">
	           		<a class="close" data-dismiss="alert" href="#">&times;</a>
	                ${label.loginError} 
				</div>
            </#if>
            <#if emailSent == true>
				<div class="alert alert-success">            	
                	${label.emailSent}
                </div>
            </#if>
            <#if emailError == true>
            	<div class="alert alert-error">
                	${label.emailErrorAlert}
				</div>
            </#if>
		</div>
	</div>
	</@content>

</@scaffolding>
