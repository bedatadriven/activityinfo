<#include "Scaffolding.ftl">
<@scaffolding title="AppEngine Configuration">

	<@content>
	<div class="row">
		<div class="span12">
		
			<h2>Domain Configuration: ${customDomain.host}</h2>
			
			<form class="form" method="post" >
				<div>
					<h3>Title</h3>
					<input name="title" class="span12" value="${customDomain.title!''}">
				</div>
				<div>
					<h3>CSS</h3>
					<textarea rows=5 class="span12" name="style">${customDomain.style!''}</textarea>
				</div>
				<div>
					<h3>Header HTML</h3>
					<textarea rows=5 class="span12" name="header">${customDomain.header!''}</textarea>
				</div>
				<div>
					<h3>Home Page Body HTML</h3>
					<textarea rows=5 class="span12" name="homePageBody">${customDomain.homePageBody!''}</textarea>
				</div>
				<div>
					<h3>Footer HTML</h3>
					<textarea rows=5 class="span12" name="footer">${customDomain.footer!''}</textarea>
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