<#macro login path isRegisterForm>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <form id="form" class="select-wrapper" style="" action=${path} method="post" data-state="user">
        <#if isRegisterForm>
            <select class="form-floating mb-3" name="demo-category" id="demo-category">
                <option data-state="user" value="user">User registration</option>
                <option data-state="company" value="company">Company registration</option>
            </select>
        </#if>
        <div class="form-floating mb-3">
            <input required type="text" name="username" class="form-control" id="floatingInput" placeholder="name@example.com"/>
            <label for="floatingInput">User Name</label>
        </div>
        <div class="form-floating mb-3">
            <input required type="password" name="password" class="form-control" id="floatingPassword" placeholder="Password"/>
            <label for="floatingPassword">Password</label>
        </div>
        <#if isRegisterForm>
            <div class="form-floating mb-3">
                <input required type="email" name="email" class="form-control" id="floatingEmail" placeholder="Email"/>
                <label for="floatingEmail">Email</label>
            </div>
            <div class="form-floating mb-3 hide-if-company">
                <input type="text" name="createdCompanyName" class="form-control" id="floatingCompany" placeholder="Urfu"/>
                <label for="floatingCompany">Company Name</label>
            </div>
            <select name="companyName" class="form-floating mb-3 hide-if-user" id="companyId">
                <#list companies as company>
                <option data-state=${company.id} value=${company.name}>${company.name}</option>
                </#list>
            </select>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm><a class="me-2" href="/registration">Add new user</a></#if>
        <button class="btn btn-primary" type="submit">
            <#if isRegisterForm>Create<#else>Sign In</#if>
        </button>
    </form>
    <script type="text/javascript">
        $('#demo-category').change(function() {
            $('#form').attr('data-state', $(this).find(':selected').data('state'));
            if ($(this).find(':selected').data('state') === "user")
                $('#floatingCompany').attr('required', false);
            else
                $('#floatingCompany').attr('required', true)
        });
    </script>
    <script type="text/javascript">
        $(document).ready(function(){
            $('#demo-category').change(function() {
                let act = "/registration/" + $('#demo-category').val();
                $('form#form').attr('action',act);
            });
        });
    </script>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Sign Out</button>
    </form>
</#macro>