<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <#if isAdmin>
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
                <td>${fields[key][0]}</td>
                <td>${fields[key][1]}</td>
            </tr>
        </#list>
    </table>
</@c.page>
