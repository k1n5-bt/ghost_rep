<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
        <form method="post" enctype="multipart/form-data" id="form">
            <table class="table">
                <tr>
                    <th>Поле</th>
                    <th>Текущее значение поля</th>
                </tr>
                <#list fieldNames as key>
                    <tr>
                        <td>${ruFieldNames[key]}</td>
                        <td>
                            <#if key == "levelOfAcceptance">
                                <select name="levelOfAcceptance">
                                    <#list levels?keys as level>
                                        <option value="${level}" <#if lastFields?? && lastFields[key] == level>selected</#if>>
                                            ${levels[level]}
                                        </option>
                                    </#list>
                                </select>
                            <#elseif key == "fileDesc">
                                <input class="form-control" type="text" name=${key} id=${key} required
                                        <#if lastFields??> value="${lastFields[key]}" </#if>
                                >
                            <#elseif key == "adoptionDate" || key == "introductionDate">
                                <input class="form-control" type="date" name=${key} id=${key}
                                        <#if lastFields??> value="${lastFields[key]}" </#if>
                                >
                            <#elseif key == "headContent" || key == "keywords" || key == "keyPhrases">
                                <textarea name=${key} id=${key} form="form"><#if lastFields??>${lastFields[key]}</#if></textarea>
                            <#else>
                                <input class="form-control" type="text" name=${key} id=${key}
                                        <#if lastFields??> value="${lastFields[key]}" </#if>
                                >
                            </#if>
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