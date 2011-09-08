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

    <form name="Form1" method="post" id="Form1" action="loginProblem" method="post">

        <div id="login">
            <div id="cap-top"></div>
            <div id="cap-body" style="height: 600px">
                <div id="bar-top"></div>



                <div id="branding"><img id="imgLogo" src="static/activityinfo.png" style="border-width:0;" title="ActivityInfo"/></div>
                <img id="unicef-logo" src="static/unicef-120.png" alt="Un projet de l'UNICEF RDC" title="Un projet de l'UNICEF RDC">


                <div id="panelLogin">



                <div id="loginPanel">
					<p class="instructions">Saisir votre addresse email et on va vous envoyer un message qui vous permettre de choisir un nouveau mot
			                    de passe</p>

                    <label>Addresse Email</label>

                    <input name="email" type="text" value="" id="txtLogin" class="textboxX"/>
                </div>
               
                <div id="errorPanel">
                <#if loginError == true>
                    On ne peut trouve une compte ActivityInfo avec cette addresse email.
                </#if>
                <#if emailSent == true>
                    Un email a ete bien envoyé
                </#if>
                <#if emailError == true>
                    Il y avait une erreur en envoyant la message. Essayer plus tard.
                </#if>
                </div>
                <div class="rememberBox" id="rememberPanel">

                   <input type="submit" value="Envoyer le lien"></span></a></span>

                <div id="marketTitle" class="marketTitle" align="center">Vous n'utilizer pas encore ActivityInfo?</div>
                <div id="marketBody" class="marketBody" align="center">ActivityInfo est une solution comprehensive pour
                    le suivi de vos activités d'urgence et de développement.<br/>

                    <div id="buttonGroup" align="left">

                        <a href="learnmore_fr.html" title="En savoir plus sur ActivityInfo"><span id="learnmore"></span></a>

                    </div>
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