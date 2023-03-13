<#import "parts/common.ftl" as c>
<#import "parts/search.ftl" as s>

<#include "parts/security.ftl">

<@c.page>
    <@s.search>
        <div style="margin-bottom: 5px;">
            <input type="checkbox" id="favorites" name="qwe">
            <label for="favorites">Показывать только избранные</label>
        </div>
    </@s.search>
    <div class = "card-group">
        <#list messages as message>
            <div class="gost_row">
                <div class="column desc_column" title="<#if message.fileDescFirstRedaction??>${message.fileDescFirstRedaction}<#else>${message.fileDesc}</#if>">
                    <a href="/document/${message.id}" class="gost_link">
                        <#if message.fileDescFirstRedaction??>${message.fileDescFirstRedaction}<#else>${message.fileDesc}</#if>
                    </a>
                </div>
                <div class="column name_column" title="<#if message.nameFirstRedaction??>${message.nameFirstRedaction}<#else>${(message.name == '-')?string("", message.name)}</#if>">
                    <#if message.nameFirstRedaction??>${message.nameFirstRedaction}<#else>${(message.name == '-')?string("", message.name)}</#if>
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