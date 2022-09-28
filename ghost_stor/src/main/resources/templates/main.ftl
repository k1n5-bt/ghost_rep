<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <#include "parts/search.ftl">

    <div class = "card-group">
        <#list messages as message>
            <div class="col-md-6 col-xl-2">
                <div class="card me-3">
                    <img class="card-img-top" src="/static/doc.png">
                    <div class="m-2">
                        <span>
                            <a href="/document/${message.id}" class="dock_link">${message.fileDesc}</a>
                        </span><br>
                        <#if message.filename != "">
                            <a href="/files/${message.filename}" download>Скачать</a><br>
                        </#if>
                    </div>
                    <#if message.isUserInFavorite(user) == true>
                        <div class="card-footer text-muted">
                            <strong>
                                <font size="1px">В избранном<br></font>
                            </strong>
                        </div>
                    </#if>
                </div>
            </div>
        <#else>
            Не найдено файлов
        </#list>
    </div>
</@c.page>