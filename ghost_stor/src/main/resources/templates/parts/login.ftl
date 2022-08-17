<#macro login path isRegisterForm>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <form id="form" class="select-wrapper" style="" action=${path} method="post">
        <div class="form-floating mb-3">
            <input required type="text" name="username" class="form-control" id="floatingInput" placeholder="name@example.com"/>
            <label for="floatingInput">Имя пользователя</label>
        </div>
        <div class="form-floating mb-3">
            <input required type="password" name="password" class="form-control" id="floatingPassword" placeholder="Password"/>
            <label for="floatingPassword">Пароль</label>
        </div>
        <#if isRegisterForm>
            <div class="form-floating mb-3">
                <input required type="email" name="email" class="form-control" id="floatingEmail" placeholder="Email"/>
                <label for="floatingEmail">Электронная почта</label>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm><a class="me-2" href="/registration">Зарегистрироваться</a></#if>
        <button class="btn btn-primary" type="submit">
            <#if isRegisterForm>Создать<#else>Войти</#if>
        </button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Выйти</button>
    </form>
</#macro>