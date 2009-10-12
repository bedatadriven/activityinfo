
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<link href="static/login.css" rel="stylesheet" type="text/css"/>

    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />

    <meta http-equiv="Cache-Control" content="no-cache" />
    <meta name="description" content="ActivityInfo est la solution ">
    <title>ActivityInfo</title>
</head>
<body class="login" >

    <form name="Form1" method="post" id="Form1" action="auth" method="post">
    <input type="hidden" name="bookmark" value="${bookmark}"></input>

        <div id="login">
            <div id="cap-top"></div>
            <div id="cap-body">
                <div id="bar-top"></div>

                <div id="whatsNew">
                    <div id="noteBg"></div>
                </div>

                <div id="branding"><img id="imgLogo" src="static/activityinfo.png" style="border-width:0;" title="ActivityInfo"/></div>
                <img id="unicef-logo" src="static/unicef-120.png" alt="Un projet de l'UNICEF RDC" title="Un projet de l'UNICEF RDC">

                <div id="panelLogin">
                    <div id="loginPanel">
                        <label>Addresse Email</label>

                        <input name="email" type="text" value="" id="txtLogin" class="textboxX" />
                    </div>
                    <div id="passwordPanel">
                        <label>Mot de passe</label>
                        <input name="password" type="password" id="txtPassword" class="textboxX" />
                    </div>
                    <div id="errorPanel">
                        ${loginError}
                    </div>
                    <div class="rememberBox" id="rememberPanel">
                        <label for="remember"><input name="remember" type="checkbox" value="true" id="remember" class="checkBox" />Rester connecté</label>
                        <div id="loginHelp"><a href="login-problem.html">Vous n'arrivez pas à vous connecter ?</a></div>
                        <span id="loginBox"><input type="image" src="static/login_fr.png" id="loginButton"></span></a></span>
                    </div>

                    <div id="marketTitle" class="marketTitle" align="center">Vous n'utilizer pas encore ActivityInfo?</div>
                    <div id="marketBody" class="marketBody" align="center">ActivityInfo est la solution comprehensive pour la suivi de vos activités d'urgence et developpement.<br />

                        <div id="buttonGroup" align="left">

                            <a href="learnmore_fr.html" title="En savoir plus sur ActivityInfo"><span id="learnmore"></span></a>

                        </div>
                    </div>
                </div>

            </div>
            <div id="cap-bottom"></div>
        </div>

        <div id="copyrightBox" class="copyrightStatement">ActivityInfo, c'est <a href="http://code.google.com/p/activity-info">OpenSource</a>&nbsp;&nbsp;&nbsp;    </div>
    </form>
</body>
</html>