<#-- @ftlvariable name="" type="org.activityinfo.server.bootstrap.model.ConfirmInvitePageModel" -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta name="description" content="ActivityInfo est la solution ">
    <title>ActivityInfo - Welcome</title>
</head>
<body>

<h1>Welcome to ActivityInfo</h1>

<p>Before we get started, let's set up your account. Confirm your name and preferred language,
    and then choose a password.</p>

<form name="Form1" method="post" id="Form1" action="auth" method="post">
    <input type="hidden" name="key" value="${user.changePasswordKey}"></input>
    <table>
        <tr>
            <td>Confirm your name:</td>
            <td><input name="name" value="${user.name}"></td>
        </tr>
        <tr>
            <td>Confirm your preferred language:</td>
            <td>
                <select name="locale">
                    <option value="en">English</option>
                    <option value="fr">French / Français</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>Choose a password:</td>
            <td><input name="password" type="password"></td>
        </tr>
        <tr>
            <td>Confirm your password:</td>
            <td><input name="password2" type="password"></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Continue"></td>
        </tr>
    </table>
</form>
</body>
</html>