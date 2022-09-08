<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <#if isAdmin>
        <a href="/document/${document.id}/edit">Изменить</a><br>
        <a href="/document/${document.id}/edit">Заменить</a><br>
        <a href="/document/${document.id}/archive">Отменить</a><br>
        <a href="/delete/${document.id}">Удалить полностью</a><br>
    </#if>
    <#if document.filename != "">
        <a href="/files/${document.filename}" target="_blank">Прикрепленный файл</a><br>
    </#if>

    <table class="table">
        <tr>
            <th>Описание</th>
            <th>Первоначальная актуализация</th>
            <th>Последняя актуализация</th>
        </tr>
        <#list fieldNames as key>
            <tr>
                <td>${ruFieldNames[key]}</td>
                <#if key == "headContent" || key == "keywords" || key == "keyPhrases">
                    <td><pre style="font-family: inherit;">${fields[key][0]}</pre></td>
                    <td><pre style="font-family: inherit;">${fields[key][1]}</pre></td>
                <#elseif key == "levelOfAcceptance">
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${levels[fields[key][0]]}</p></td>
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${levels[fields[key][1]]}</p></td>
                <#else>
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${fields[key][0]}</p></td>
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${fields[key][1]}</p></td>
                </#if>
            </tr>
        </#list>
    </table>
</@c.page>
