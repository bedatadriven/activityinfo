<#-- @ftlvariable name="" type="org.activityinfo.server.bootstrap.model.LoginPageModel" -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link href="static/login.css" rel="stylesheet" type="text/css"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <title>ActivityInfo</title>
</head>
<body class="login">

<form name="Form1" method="post" id="loginForm" action="changePassword" method="post">
    <input type="hidden" name="key" value="${user.changePasswordKey}"></input>

    <div id="login">
            <div id="panelLogin">
                <div id="passwordPanel">
                    <label>Noveau Mot de passe</label>
                    <input name="password" type="password" id="txtPassword" class="textboxX"/>
                </div>
                <div id="passwordPanel">
                    <label>Confirmer votre mot de passe</label>
                    <input name="password2" type="password" id="txtPassword" class="textboxX"/>
                </div>
				<div class="rememberBox" id="rememberPanel">

                   <input type="submit" value="Confirmer"></span></a></span>
				</div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-11567120-1");
        pageTracker._trackPageview("changePassword");
    } catch(err) {
    }</script><#-- This is the tracking code for Google Analytics. You can remove this if you do not wish to track
     usage. If you do wish to use Google Analytics to track usage, you will need to replace UA-11567120-1 with your
     own ID. -->
</body>
</html>
