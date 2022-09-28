<#assign
    known = Session.SPRING_SECURITY_CONTEXT??>
 <#if known>
     <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isAdmin = user.isAdmin()
        isFromCompany = user.isFromCompany()>
 <#else>
     <#assign
        name = ""
        isAdmin = false>
 </#if>