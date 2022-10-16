<#import "parts/common.ftl" as c>
<#import "parts/search.ftl" as s>

<#include "parts/security.ftl">

<@c.page>
    <@s.search>
        <div>
            <input type="checkbox" id="favorites" name="qwe">
            <label for="favorites">Показывать только избранные</label>
        </div>
    </@s.search>
    <div class = "card-group">
        <#list messages as message>
            <div class="col-md-6 col-xl-2">
                <div class="card me-3">
                    <img class="card-img-top" src="/static/doc.png">
                    <div class="m-2">
                        <span>
                            <a href="/document/${message.id}" class="dock_link">
                                <#if message.fileDescFirstRedaction??>
                                    ${message.fileDescFirstRedaction}
                                <#else>
                                    ${message.fileDesc}
                                </#if>
                            </a>
                        </span><br>
                        <#if message.filename != "">
                            <a href="/files/${message.filename}" download>Скачать</a><br>
                        </#if>
                    </div>
                    <#if message.isUserInFavorite(user) == true>
                        <div class="card-footer text-muted">
                            <strong>
                                <font size="1px">В избранном<br></font>
                            </strong>
                        </div>
                    </#if>
                </div>
            </div>
        <#else>
            Не найдено файлов
        </#list>
    </div>
    <script type="text/javascript">
        function func() {
        var params = window
                            .location
                            .search
                            .replace('?','')
                            .split('&')
                            .reduce(
                                function(p,e){
                                    var a = e.split('=');
                                    p[ decodeURIComponent(a[0])] = decodeURIComponent(a[1]);
                                    return p;
                                },
                                {}
                            );
          if (params["isFavorites"] == "true"){
              document.location.href = '/main';
          }
          else {
              document.location.href = '/main?isFavorites=true';
          }
        }
        document.getElementById('favorites').addEventListener('click', func);
    </script>
    <script type="text/javascript">
        var params = window
                    .location
                    .search
                    .replace('?','')
                    .split('&')
                    .reduce(
                        function(p,e){
                            var a = e.split('=');
                            p[ decodeURIComponent(a[0])] = decodeURIComponent(a[1]);
                            return p;
                        },
                        {}
                    );
        if (params["isFavorites"] == "true"){
          document.getElementById('favorites').checked = true
        }
        else {
          document.getElementById('favorites').checked = false
        }
    </script>
</@c.page>