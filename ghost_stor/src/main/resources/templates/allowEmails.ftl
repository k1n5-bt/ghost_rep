<#import "parts/common.ftl" as c>

<@c.page>
    <div class="form-group mb-3">
        ${message!}
        <form class="form-inline" method="post" action=${formAction!}>
            <div class="form-group mb-3">
                <label for="email">Email</label>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <input class="form-control" type="email" name="email" id="email" placeholder="..." required>
            </div>
            <div class="form-group mb-3">
                <button class="btn btn-primary" type="submit">Добавить</button>
            </div>
        </form>
    </div>
    Разрешенные для регистрации почты
    <table>
        <thead>
        <tr>
            <th>Почта </th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list emails as email>
            <tr>
                <td>${email}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>