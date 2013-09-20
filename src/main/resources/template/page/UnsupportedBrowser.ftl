<#include "Scaffolding.ftl">
<@scaffolding title="Unsupported Browser">

	<@content>
	<div class="row">
		<div class="span12">
		
			<h3>${label.unsupportedBrowserHeading}</h3>
			<p class="lead">${label.unsupportedBrowser}</p>
			<ul>
				<li><a href="http://chrome.google.com">Google Chrome</a></li>
				<li><a href="http://www.mozilla.com">FireFox</a> 3.5+</li>
				<li><a href="http://www.microsoft.com/internetexplorer">Internet Explorer</a> 6-9</li>
				<li><a href="http://www.apple.com/safari">Safari</a> (Mac)</li>
				<li><a href="http://www.opera.com">Opera</a></li>
			</ul>
		</div>
	</div>
	</@content>
	<@footer/>
	<@scripts/>

</@scaffolding>