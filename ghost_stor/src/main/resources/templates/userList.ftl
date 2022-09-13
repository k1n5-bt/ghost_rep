<#import "parts/common.ftl" as c>

<@c.page>
Список пользователей сайта
    <div class = "card-group">
        <#list users as user>
            <div class="col-md-6 col-xl-2">
                <div width="50" height="100" class="card me-3">
                    <img class="card-img-top" src="/static/user.png">
                    <div class="m-2">
                        <span>
                            <a>${user.username}</a><br>
                            Роли: <#list user.roles as role>${role}<#sep>, </#list><br>
                            <a href="/user/${user.id}" class="dock_link">изменить</a>
                        </span><br>
                    </div>
                </div>
            </div>
        <#else>
            Не найдено пользователей
        </#list>
    </div>
</@c.page>