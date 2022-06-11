<#--<div class="form-group">-->
<#--    <form class="form-inline" method="get" action=${formAction!} style="display:flex;">-->
<#--        <input class="form-control mb-2" type="text" name="nameFilter" value="${nameFilter!}" placeholder="Поиск по названию" minlength="3">-->
<#--        <button class="btn btn-primary mb-2" type="submit" style="min-width: 120px; margin-left: 5px;">Search</button>-->
<#--    </form>-->
<#--    <form class="form-inline" method="get" action=${formAction!} style="display:flex;">-->
<#--        <input class="form-control mb-2" type="text" name="descFilter" value="${descFilter!}" placeholder="Поиск по описанию" minlength="3">-->
<#--        <button class="btn btn-primary mb-2" type="submit" style="min-width: 120px; margin-left: 5px;">Search</button>-->
<#--    </form>-->
<#--</div>-->


<div class="form-group">
    <form class="form-inline" method="get" action=${formAction!}>
        <input class="form-control mb-2 search_field" type="text" name="nameFilter" value="${nameFilter!}" placeholder="Поиск по названию" minlength="3">
        <input class="form-control mb-2 search_field"  type="text" name="descFilter" value="${descFilter!}" placeholder="Поиск по описанию" minlength="3">

        <button class="btn btn-primary mb-2" type="submit" style="min-width: 120px; margin-left: 5px;">Search</button>
    </form>
</div>