<#import "parts/common.ftl" as c>

<@c.page>
    Редактировнаие пользователей

    <form id="form" class="select-wrapper" action="/user" method="post">
        <div class="form-floating mb-3">
            <input type="text" name="username" class="form-control" id="formUsername" value="${user.username}">
            <label for="formUsername">Логин</label>
        </div>
        <div class="form-floating mb-3">
            <input type="text" name="name" class="form-control" id="formName" value="${user.name}">
            <label for="formName">Имя</label>
        </div>
        <div class="form-floating mb-3">
            <input type="text" name="surname" class="form-control" id="formSurname" value="${user.surname}">
            <label for="formSurname">Фамилия</label>
        </div>
        <div class="form-floating mb-3">
            <input type="text" name="patronymic" class="form-control" id="formPatronymic" value="${user.patronymic}">
            <label for="formPatronymic">Отчество</label>
        </div>
        <div class="form-floating mb-3">
            <input type="text" name="field" class="form-control" id="formField" value="${user.field}">
            <label for="formField">Деятельность компании</label>
        </div>
        <div class="form-floating mb-3">
            <input type="text" name="division" class="form-control" id="formDivision" value="${user.division}">
            <label for="formDivision">Подразделение компании</label>
        </div>
        <div class="form-floating mb-3">
            <input type="text" name="company" class="form-control" id="formCompany" value="${user.company}">
            <label for="formCompany">Название компании</label>
        </div>

        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Сохранить</button>
    </form>
</@c.page>