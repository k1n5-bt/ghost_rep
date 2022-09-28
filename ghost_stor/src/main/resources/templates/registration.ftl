<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <div class="mb-1">Регистрация нового пользователя</div>
    ${message!}
    <@l.login "/registration/user" true/>
</@c.page>