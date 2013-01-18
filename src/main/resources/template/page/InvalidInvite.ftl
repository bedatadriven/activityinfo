<#include "Scaffolding.ftl">
<@scaffolding title="${label.invalidOrExpiredInvite}">

	<@content>
	<div class="row-fluid">
		<div class="span12">
			<div class="page-header"><h1 class="page-title">${label.invalidInvitation}</h1></div>
			
			${label.invalidInvitationDetail}
			
			<p><a href="login" class="btn btn-primary">${label.loginNow}</a></p>
		 </div>
	</div>
	</@content>

</@scaffolding>