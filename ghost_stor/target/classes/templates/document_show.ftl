<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <#if isAdmin>
        <a href="/delete/${document.id}">Удалить</a><br>
        <a href="/document/${document.id}/edit">Редактировать</a>
    </#if>

    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Название документа:</div>
        <div class="doc_field_value">${document.name}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Описание документа:</div>
        <div class="doc_field_value">${document.fileDesc}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Код ОКС:</div>
        <div class="doc_field_value">${document.OKCcode}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Код ОКПД 2:</div>
        <div class="doc_field_value">${document.OKPDcode}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Дата принятия:</div>
        <div class="doc_field_value">${document.adoptionDate}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Дата введения:</div>
        <div class="doc_field_value">${document.introductionDate}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Разработчик:</div>
        <div class="doc_field_value">${document.developer}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Принят взамен:</div>
        <div class="doc_field_value">${document.predecessor}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Текст документа:</div>
        <div class="doc_field_value">${document.contents}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Уровень принятия:</div>
        <div class="doc_field_value">${document.levelOfAcceptance}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Изменения:</div>
        <div class="doc_field_value">${document.changes}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Статус документа:</div>
        <div class="doc_field_value">${document.status}</div>
    </div>
    <div class="form-control mb-2 doc_field_block">
        <div class="doc_field_title">Количество обращений:</div>
        <div class="doc_field_value">${document.referencesAmount}</div>
    </div>
</@c.page>
