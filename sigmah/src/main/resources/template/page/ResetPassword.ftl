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
            <div id="cap-body">
                <div id="bar-top"></div>

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
             
           		</div>

          	</div>
            <div id="cap-bottom"></div>
        </div>
    </form>
</body>
</html>