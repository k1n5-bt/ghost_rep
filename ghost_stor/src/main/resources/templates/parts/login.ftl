<#macro login path isRegisterForm>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <form id="form" class="select-wrapper" style="" action=${path} method="post" data-state="user">
        <div class="form-floating mb-3">
            <input required type="email" name="email" class="form-control" id="floatingEmail" placeholder="Email"/>
            <label for="floatingEmail">Электронная почта</label>
        </div>
        <#if isRegisterForm>
            <div class="form-floating mb-3">
                <input required type="text" name="username" class="form-control" id="floatingInput" placeholder="name@example.com"/>
                <label for="floatingInput">Логин</label>
            </div>
            <div class="form-floating mb-3">
                <input required type="text" name="name" class="form-control" id="floatingName" placeholder="Имя"/>
                <label for="floatingName">Имя</label>
            </div>
            <div class="form-floating mb-3">
                <input required type="text" name="surname" class="form-control" id="floatingSurname" placeholder="Фамилия"/>
                <label for="floatingSurname">Фамилия</label>
            </div>
            <div class="form-floating mb-3">
                <input type="text" name="patronymic" class="form-control" id="floatingPatronymic" placeholder="Отчество"/>
                <label for="floatingPatronymic">Отчество</label>
            </div>
            <select class="form-floating mb-3" name="demo-category" id="demo-category">
                <option data-state="user" value="user">Физ. лицо</option>
                <option data-state="company" value="company">Компания</option>
            </select>
            <div class="form-floating mb-3 hide-if-company">
                <input type="text" name="company" class="form-control" id="floatingCompany" placeholder="Urfu"/>
                <label for="floatingCompany">Название компании</label>
            </div>
            <div class="form-floating mb-3 hide-if-company">
                <input type="text" name="division" class="form-control" id="floatingDivision" placeholder=""/>
                <label for="floatingDivision">Подразделение компании</label>
            </div>
            <div class="form-floating mb-3 hide-if-company">
                <input type="text" name="field" class="form-control" id="floatingField" placeholder=""/>
                <label for="floatingField">Деятельность компании</label>
            </div>
        </#if>
        <div class="form-floating mb-3">
            <input required type="password" name="password" class="form-control" id="floatingPassword" placeholder="Password"/>
            <label for="floatingPassword">Пароль</label>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm><a class="me-2" href="/registration">Зарегистрироваться</a></#if>
        <button class="btn btn-primary" type="submit">
            <#if isRegisterForm>Создать<#else>Войти</#if>
        </button>
    </form>
    <script type="text/javascript">
        $('#demo-category').change(function() {
            $('#form').attr('data-state', $(this).find(':selected').data('state'));
            if ($(this).find(':selected').data('state') === "user"){
                $('#floatingCompany').attr('required', false);
                $('#floatingDivision').attr('required', false);
                $('#floatingField').attr('required', false);
            }
            else{
                $('#floatingCompany').attr('required', true)
                $('#floatingDivision').attr('required', true)
                $('#floatingField').attr('required', true)
            }
        });
    </script>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Выйти</button>
    </form>
</#macro>