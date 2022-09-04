<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <form method="post" enctype="multipart/form-data">
        <table class="table">
            <tr>
                <th>Поле</th>
                <th>Текущее значение поля</th>
            </tr>
            <#list fieldNames as key>
                <tr>
                    <td>${ruFieldNames[key]}</td>
                    <td>
                        <input class="form-control" type="text" name=${key} id=${key} placeholder="..."
                                <#if lastFields??> value="${lastFields[key]}" </#if>
                        >
                    </td>
                </tr>
            </#list>
        </table>
        <div class="form-group mb-3">
            <div class="custom-file">
                <input type="file" name="file" id="customFile">
                <label class="custom-file-label" for="customFile"></label>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="form-group mb-3">
            <button class="btn btn-primary" type="submit">Добавить</button>
        </div>
    </form>
</@c.page>