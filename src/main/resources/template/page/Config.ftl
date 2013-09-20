<#include "Scaffolding.ftl">
<@scaffolding title="AppEngine Configuration">

	<@content>
	<div class="row">
		<div class="span12">
		
			<h3>Application Configuration</h3>
			
			<form class="form" method="post" >
				<div>
					<textarea rows=5 class="span12" name="config">${currentConfig}</textarea>
				</div>
				<div>
					<button type="submit" class="btn btn-primary">Update</button>
				</div>
			</form>
		</div>
	</div>

	</@content>
	<@footer/>
	<@scripts/>

</@scaffolding>