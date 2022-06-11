<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <#include "parts/search.ftl">

    <a class="btn btn-primary mb-3" data-bs-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Add new content
    </a>

    <div class="collapse" id="collapseExample">
        <div class="form-group mb-3">
            <form method="post" enctype="multipart/form-data">
                <div class="form-group mb-3">
                    <input class="form-control" maxlength="15" type="text" name="name" placeholder="Введите название файла" required>
                </div>
                <div class="form-group mb-3">
                    <input class="form-control" maxlength="25" type="text" name="fileDesc" placeholder="Описание">
                </div>
                <div class="form-group mb-3">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile" required>
                        <label class="custom-file-label" for="customFile"></label>
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <div class="form-group mb-3">
                    <button class="btn btn-primary" type="submit">Добавить</button>
                </div>
            </form>
        </div>
    </div>
    <div class = "card-group">
        <#list messages as message>
            <div class="col-md-6 col-xl-2">
                <div class="card me-3">
                    <#if message.filename??>
                        <img class="card-img-top" src="/static/doc.png">
                    </#if>
                    <div class="m-2">
                        <span><font size="2px"><strong>File name</strong> - ${message.name}</font>
                        </span><br>
                        <a href="/files/${message.filename}" download>Скачать</a><br>
                        <#if message.authorName == name || isAdmin>
                            <a href="/delete/${message.id}">Удалить</a>
                        </#if>
                    </div>
                    <div class="card-footer text-muted">
                        <strong>
                            <font size="1px">
                                Author - ${message.authorName}<br>
                                Desc - ${message.fileDesc}<br>
                            </font>
                        </strong>
                    </div>
                </div>
            </div>
        <#else>
            Не найдено файлов
        </#list>
    </div>
</@c.page>