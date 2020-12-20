<#assign hasContext = Session.SPRING_SECURITY_CONTEXT?? >

<#if hasContext >
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    username = user.getUsername()
    userId = user.getId()
    >
<#else>
    <#assign
    username = "uknown"
    >
</#if>