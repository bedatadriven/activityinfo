<#include "Scaffolding.ftl">
<@scaffolding title="Password Expired">

	<@content>
	<div class="row-fluid">
		<div class="span12">
			<h3>Invalid invitation</h3>
			
			<p class="well">
			Pour des raisons securitaire, votre mot de passe a expiré. Nous avons vous envoyer une email à 
			<b>${user.email}</b> avec une lien
			qui permettre de choisir une nouveau mot de passe.</p>
		 </div>
	</div>
	</@content>

</@scaffolding>
