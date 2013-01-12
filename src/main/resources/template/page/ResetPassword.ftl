<#include "Scaffolding.ftl">
<@scaffolding title="Reset your password">

	<@content>
	<div class="row-fluid">
		<div class="span12">
		
			<h3>Reset your password</h3>
			
			<p>Saisir votre addresse email et on va vous envoyer un message qui vous 
				permettre de choisir un nouveau mot de passe</p>
				
			<form class="form-inline" action="loginProblem" method="POST">
			  <input type="text" name="email" class="input-xlarge" placeholder="my.name@ngo.org">
			  <button type="submit" class="btn btn-primary">Reset</button>
			</form>
		
            <#if loginError == true>
	            <div class="alert alert-error">
	           		<a class="close" data-dismiss="alert" href="#">&times;</a>
	                On ne peut trouve une compte ActivityInfo avec cette addresse email. 
				</div>
            </#if>
            <#if emailSent == true>
				<div class="alert alert-success">            	
                	Un email a ete bien envoyé
                </div>
            </#if>
            <#if emailError == true>
            	<div class="alert alert-error">
                	Il y avait une erreur en envoyant la message. Essayer plus tard.
				</div>
            </#if>
		</div>
	</div>
	</@content>

</@scaffolding>
