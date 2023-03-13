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
                    <#if field == "introductionDate" || field == "adoptionDate" || field == "descUpdateDate">
                        <input class="form-control mb-2 search_field" type="text" name="${field}" value="${params[field]}" placeholder="2021-01-01">
                    <#elseif field == "levelOfAcceptance">
                        <select name="levelOfAcceptance" class="form-control mb-2 search_field" style="appearance: auto !important">
                            <#list levels?keys as level>
                                <#if "-" != level>
                                    <option value="${level}" <#if params?? && params[field] == level>selected</#if>>
                                        ${levels[level]}
                                    </option>
                                </#if>
                            </#list>
                        </select>
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
            <div class="gost_row">
                <div class="column desc_column" title="<#if message.fileDescFirstRedaction??>${message.fileDescFirstRedaction}<#else>${message.fileDesc}</#if>">
                    <a href="/document/${message.id}" class="gost_link">
                        <#if message.fileDescFirstRedaction??>${message.fileDescFirstRedaction}<#else>${message.fileDesc}</#if>
                    </a>
                </div>
                <div class="column name_column" title="<#if message.nameFirstRedaction??>${message.nameFirstRedaction}<#else>${(message.name == '-')?string("", message.name)}</#if>">
                    <#if message.nameFirstRedaction??>${message.nameFirstRedaction}<#else>${(message.name == '-')?string("", message.name)}</#if>
                </div>
            </div>
        <#else>
            Не найдено файлов
        </#list>
    </div>
</@c.page>