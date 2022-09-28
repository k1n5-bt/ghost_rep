<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="form-group">
        <form class="form-inline" method="get">
            <#list fields as field>
                <div style="display: flex">
                    <div style="width: 215px; margin-right: 10px" class="form-control mb-2 search_field">
                        ${ruFields[field]}
                    </div>
                    <#if field == "introductionDate" || field == "adoptionDate">
                        <input class="form-control mb-2 search_field" type="text" name="${field}" value="${params[field]}" placeholder="2021-01-01">
                    <#else>
                        <input class="form-control mb-2 search_field" type="text" name="${field}" value="${params[field]}" minlength="2">
                    </#if>
                </div>
            <#else>
                Не найдено файлов
            </#list>

            <button class="btn btn-primary mb-2" type="submit" style="min-width: 120px; margin-left: 5px;">Искать!</button>
        </form>
    </div>

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
                </div>
            </div>
        <#else>
            Не найдено файлов
        </#list>
    </div>
</@c.page>