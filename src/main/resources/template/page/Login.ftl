<#-- @ftlvariable name="" type="org.activityinfo.server.bootstrap.model.LoginPageModel" -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <link href="static/login.css" rel="stylesheet" type="text/css"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <script language="javascript" src="Login/Login.nocache.js"></script>
    <title>ActivityInfo Login</title>
</head>
<body class="login">

<form method="post" id="loginForm" action="javascript:__do_login()" method="post">
    <input type="hidden" name="urlSuffix" value="${urlSuffix}">

    <div id="login">
        <div id="cap-top"></div>
        <div id="cap-body">
            <div id="bar-top"></div>

              <div id="panelLogin">
                <div id="loginPanel">
                    <label>Addresse Email</label>

                    <input name="email" type="text" value="" id="emailField" class="textboxX"/>
                </div>
                <div id="passwordPanel">
                    <label>Mot de passe</label>
                    <input name="password" type="password" id="passwordField" class="textboxX"/>
                </div>
                <div id="errorPanel">
                <#if loginError == true>
                    L'addresse email ou mot de passe est incorrect.
                </#if>
                </div>
                <div class="rememberBox" id="rememberPanel">
                    <label for="remember"><input name="remember" type="checkbox" value="true" id="remember"
                                                 class="checkBox"/>Rester connecté</label>

                    <span id="loginBox"><input type="image" src="static/login_fr.png"
                                               id="loginButton"></span></a></span>
                    <div style="clear:both"/>
                </div>

 				<div id="loginHelp"><a href="loginProblem">Vous avez oublié votre mot de passe ?</a></div>

            </div>

        </div>
        <div id="cap-bottom"></div>
    </div>

</form>
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-11567120-1");
    <#if loginError == false>
        pageTracker._trackPageview("login");
        <#else>
            pageTracker._trackPageview("login/error");
    </#if>
    } catch(err) {
    }</script>
</body>
</html>
