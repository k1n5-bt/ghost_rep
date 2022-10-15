<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div>
        <#if document.isUserInFavorite(user) == true>
            <a href="/favorite/remove/${document.id}">
                <button class="btn btn-primary" style="margin-bottom: 10px;">Удалить из избранного</button>
            </a><br>
        <#else>
            <a href="/favorite/${document.id}">
                <button class="btn btn-primary" style="margin: 0 10px 10px 0;">Добавить в избранное</button>
            </a><br>
        </#if>
    </div>
    <div>
        <#if isAdmin>
            <div style="display: flex">
                <a href="/document/${document.id}/edit">
                    <button class="btn btn-primary" style="margin: 0 10px 10px 0;">Актуализирновать данные</button>
                </a><br>
                <a href="/document/${document.id}/edit">
                    <button class="btn btn-primary" style="margin: 0 10px 10px;">Исправить данные</button>
                </a><br>
            </div>
            <div style="display: flex">
                <a href="/document/${document.id}/replace">
                    <button class="btn btn-primary" style="margin: 0 10px 10px 0;" onclick="showQuestion('replace')">
                        Заменить
                    </button>
                </a><br>
                <a href="/document/${document.id}/archive">
                    <button class="btn btn-primary" style="margin: 0 10px 10px;" onclick="showQuestion('archive')">
                        Отменить
                    </button>
                </a><br>
                <a href="/delete/${document.id}">
                    <button class="btn btn-primary" style="margin: 0 10px 10px;" onclick="showQuestion('delete')">Удалить
                        полностью
                    </button>
                </a><br>
            </div>
        </#if>
    </div>
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
                    <td><pre style="font-family: inherit;">${(fields[key][0] == '-')?string("", fields[key][0])}</pre></td>
                    <td><pre style="font-family: inherit;">${fields[key][1]}</pre></td>
                <#elseif key == "levelOfAcceptance">
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${levels[fields[key][0]]}</p></td>
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${levels[fields[key][1]]}</p></td>
                <#elseif key == "normReferences">
                    <td>
                        <#list activeLinks?keys as a_link>
                            <a href="/document/${activeLinks[a_link]}" class="dock_link">${a_link}</a>
                            <br>
                        </#list>
                        <#list inactiveLinks as ina_link>
                            <p style="margin-bottom: 0;">${ina_link}</p>
                        </#list>
                    </td>
                    <td>
                        <#list activeLinks_f?keys as a_link>
                            <a href="/document/${activeLinks_f[a_link]}" class="dock_link">${a_link}</a>
                            <br>
                        </#list>
                        <#list inactiveLinks_f as ina_link>
                            <p style="margin-bottom: 0;">${ina_link}</p>
                        </#list>
                    </td>
                <#else>
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${(fields[key][0] == '-')?string("", fields[key][0])}</p></td>
                    <td><p style="max-width: 400px; word-break: break-word; margin-bottom: 0px">${fields[key][1]}</p></td>
                </#if>
            </tr>
        </#list>
    </table>

    <script>
        function showQuestion(action) {
            let str;
            switch (action) {
                case "replace":
                    str = 'Необходимо будет заполнить новый документ.';
                    break;
                case "archive":
                    str = 'Документ будет отправлен в архив, а ссылки в других документах станут неактивны.';
                    break;
                case "delete":
                    str = 'Документ будет безвозвратно удален.';
                    break;
            }

            let message = 'Вы уверены?\n';
            let check = confirm(message + str);
            if (check !== true) {
                event.preventDefault();
            }
        }
        function isInFavorite(action) {
            let str;
            switch (action) {
                case "replace":
                    str = 'Необходимо будет заполнить новый документ.';
                    break;
                case "archive":
                    str = 'Документ будет отправлен в архив, а ссылки в других документах станут неактивны.';
                    break;
                case "delete":
                    str = 'Документ будет безвозвратно удален.';
                    break;
            }

            let message = 'Вы уверены?\n';
            let check = confirm(message + str);
            if (check !== true) {
                event.preventDefault();
            }
        }
    </script>
</@c.page>
