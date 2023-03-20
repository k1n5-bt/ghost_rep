<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <#include "parts/search.ftl">

    <div class = "card-group">
        <#list messages as message>
            <div class="gost_row">
                <div class="column desc_column" title="<#if message.fileDescFirstRedaction??>${message.fileDescFirstRedaction}<#else>${message.fileDesc}</#if>">
                    <a href="/archived_doc/${message.id}" class="gost_link">
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