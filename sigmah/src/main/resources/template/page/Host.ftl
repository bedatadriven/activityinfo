<#-- @ftlvariable name="" type="org.sigmah.server.bootstrap.model.HostPageModel" -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<#if appCacheEnabled>
<html manifest="ActivityInfo/ActivityInfo.appcache">
<#else>
<html>
</#if>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="application-name" content="ActivityInfo"/>
    <meta name="description" content="ActivityInfo"/>
    <meta name="application-url" content="${appUrl}"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9">
 
    <link rel="icon" href="ActivityInfo/desktopicons/16x16.png" sizes="16x16"/>
    <link rel="icon" href="ActivityInfo/desktopicons/32x32.png" sizes="32x32"/>
    <link rel="icon" href="ActivityInfo/desktopicons/48x48.png" sizes="48x48"/>
    <link rel="icon" href="ActivityInfo/desktopicons/64x64.png" sizes="64x64"/>
    <link rel="icon" href="ActivityInfo/desktopicons/128x128.png" sizes="128x128"/>

    <title>ActivityInfo</title>
    <style type="text/css">
        #loading-box {
            position: absolute;
            left: 45%;
            top: 40%;
            padding: 2px;
            margin-left: -45px;
            z-index: 20001;
            border: 1px solid #ccc;
        }

        #loading-box .loading-indicator {
            background: #eef;
            font: bold 13px tahoma, arial, helvetica;
            padding: 10px;
            margin: 0;
            height: auto;
            color: #444;
        }

        #loading-box .loading-indicator img {
            margin-right: 8px;
            float: left;
            vertical-align: top;
        }

        #loading-msg {
            font: normal 10px tahoma, arial, sans-serif;
        }

        #loading-options {
            position: absolute;
            right: 10px;
            bottom: 10px;
            font: normal 13px tahoma, arial, sans-serif;
            text-align: right;
        }
        <#include "Application.css">
    </style>
    <script type="text/javascript">
		if(document.cookie.indexOf('authToken=') == -1) {
			window.location = "/login";
		}
        var UserInfo = {
            userId: ${auth.user.id},
            authToken: "${auth.id}",
            email: "${auth.user.email}"
        };
        var GoogleMapsAPI = {
            key: "ABQIAAAAaKyZGjYsJ9quclBfsnGc-xSULc68XBC8rIKw1gDHypRtutTChRRhuj6VmI9buf-pphk2EHnsnmxwRg"
        };
        var VersionInfo = {
            revision: '$[display.version]'
        };
    </script>

    <script language='javascript' src='ActivityInfo/gxt224/flash/swfobject.js'></script>
    <script type="text/javascript" language="javascript" src="ActivityInfo/ActivityInfo.nocache.js"></script>
	<script type="text/javascript">
	
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-11567120-1']);
	  _gaq.push(['_trackPageview', '/login/success']);
	
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	</script>
</head>
<body>
<div id="loading">
    <div id="loading-box">
        <div class="loading-indicator">
            <img src="ActivityInfo/gxt224/images/default/shared/large-loading.gif" alt=""/>
            ActivityInfo $[display.version]<br/>
            <span id="loading-msg">Chargement en cours...</span>

        </div>
    </div>
</div>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position:absolute;width:0;height:0;border:0"></iframe>
<iframe src="javascript:''" id="_downloadFrame" name="_downloadFrame" tabIndex='-1'
        style="position:absolute;width:0;height:0;border:0"></iframe>
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>

<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-11567120-1");
        pageTracker._trackPageview("/login/success");
    } catch(err) {
    }</script>
    
    <!-- begin olark code --><script type='text/javascript'>/*{literal}<![CDATA[*/window.olark||(function(i){var e=window,h=document,a=e.location.protocol=="https:"?"https:":"http:",g=i.name,b="load";(function(){e[g]=function(){(c.s=c.s||[]).push(arguments)};var c=e[g]._={},f=i.methods.length; while(f--){(function(j){e[g][j]=function(){e[g]("call",j,arguments)}})(i.methods[f])} c.l=i.loader;c.i=arguments.callee;c.f=setTimeout(function(){if(c.f){(new Image).src=a+"//"+c.l.replace(".js",".png")+"&"+escape(e.location.href)}c.f=null},20000);c.p={0:+new Date};c.P=function(j){c.p[j]=new Date-c.p[0]};function d(){c.P(b);e[g](b)}e.addEventListener?e.addEventListener(b,d,false):e.attachEvent("on"+b,d); (function(){function l(j){j="head";return["<",j,"></",j,"><",z,' onl'+'oad="var d=',B,";d.getElementsByTagName('head')[0].",y,"(d.",A,"('script')).",u,"='",a,"//",c.l,"'",'"',"></",z,">"].join("")}var z="body",s=h[z];if(!s){return setTimeout(arguments.callee,100)}c.P(1);var y="appendChild",A="createElement",u="src",r=h[A]("div"),G=r[y](h[A](g)),D=h[A]("iframe"),B="document",C="domain",q;r.style.display="none";s.insertBefore(r,s.firstChild).id=g;D.frameBorder="0";D.id=g+"-loader";if(/MSIE[ ]+6/.test(navigator.userAgent)){D.src="javascript:false"} D.allowTransparency="true";G[y](D);try{D.contentWindow[B].open()}catch(F){i[C]=h[C];q="javascript:var d="+B+".open();d.domain='"+h.domain+"';";D[u]=q+"void(0);"}try{var H=D.contentWindow[B];H.write(l());H.close()}catch(E){D[u]=q+'d.write("'+l().replace(/"/g,String.fromCharCode(92)+'"')+'");d.close();'}c.P(2)})()})()})({loader:(function(a){return "static.olark.com/jsclient/loader0.js?ts="+(a?a[1]:(+new Date))})(document.cookie.match(/olarkld=([0-9]+)/)),name:"olark",methods:["configure","extend","declare","identify"]});
		/* custom configuration goes here (www.olark.com/documentation) */
		olark.identify('4284-445-10-3720');/*]]>{/literal}*/</script>
	<!-- end olark code -->
</body>
</html>