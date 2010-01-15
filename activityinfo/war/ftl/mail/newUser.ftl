<#-- @ftlvariable name="user" type="org.activityinfo.server.domain.User" -->
<#-- @ftlvariable name="invitingUser" type="org.activityinfo.server.domain.User" -->
Hi ${user.name},

${invitingUser.name} (${invitingUser.email}) has invited you to access ActivityInfo. To
complete your user registration, click on the following link:

http://www.activityinfo.org/newuser?${user.changePasswordKey}

Best regards,

L'équipe ActivityInfo