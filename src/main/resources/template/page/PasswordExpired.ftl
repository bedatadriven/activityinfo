<#include "Scaffolding.ftl">
<@scaffolding title="${label.passwordExpired}">

	<@content>
	<div class="row-fluid">
		<div class="span12">
			<h3>${label.invalidInvitation}</h3>
			
			<p class="well">
			${label.passwordExpiredDetail1} 
			<b>${user.email}</b> ${label.passwordExpiredDetail2}</p>
		 </div>
	</div>
	</@content>

</@scaffolding>
