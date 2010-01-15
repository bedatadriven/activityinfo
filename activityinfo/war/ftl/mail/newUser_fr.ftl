<#-- @ftlvariable name="user" type="org.activityinfo.server.domain.User" -->
<#-- @ftlvariable name="invitingUser" type="org.activityinfo.server.domain.User" -->
Bonjour ${user.name},

${invitingUser.name} (${invitingUser.email}) vous invite à acceder au ActivityInfo. Pour confirmer
votre adresse et connecter au système, visitez le lien suivante:

http://www.activityinfo.org/?newUser=${user.changePasswordKey}

Best regards,

L'équipe ActivityInfo