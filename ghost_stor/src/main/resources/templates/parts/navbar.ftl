<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light" style="background-color: #e3f2fd;">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Ghost Storage</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="/main">Документы</a>
                </li>
                <#if isCompanyAdmin>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/company/requests">Запросы на добавление в компанию</a>
                    </li>
                    <li>
                        <a class="nav-link" aria-current="page" href="/company/allow">Добавить разрешенный для регистрации email</a>
                    </li>
                </#if>
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/user">Панель администратора</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/document">Новый документ</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/archived">Архив</a>
                    </li>
                </#if>
            </ul>
            <div class="navbar-text me-3">${name}</div>
            <#if name != "unknown">
                <@l.logout />
            </#if>
        </div>
    </div>
</nav>