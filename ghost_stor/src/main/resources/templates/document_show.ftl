<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <#if isAdmin>
        <a href="/delete/${document.id}">Удалить</a><br>
        <a href="/document/${document.id}/edit">Редактировать</a><br>
    </#if>
    <#if document.filename != "">
        <a href="/files/${document.filename}" target="_blank">Прикрепленный файл</a><br>
    </#if>

    <table class="table">
        <tr>
            <th>Поле</th>
            <th>Значение поля</th>
            <th>Первая актуализация</th>
            <th>Вторая актуализация</th>
        </tr>
        <#list fieldNames as key>
            <tr>
                <td>${ruFieldNames[key]}</td>
                <td>${fields[key][0]}</td>
                <td>${fields[key][1]}</td>
                <td>${fields[key][2]}</td>
            </tr>
        </#list>
    </table>
</@c.page>
