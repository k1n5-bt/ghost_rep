<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="form-group mb-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group mb-3">
                <label for="name">Название документа</label>
                <input class="form-control" type="text" name="name" id="name" placeholder="..." required
                       <#if document??> value="${document.name}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="fileDesc">Описание документа</label>
                <input class="form-control" type="text" name="fileDesc" id="fileDesc" placeholder="..."
                        <#if document??> value="${document.fileDesc}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="OKCcode">Код ОКС</label>
                <input class="form-control" type="text" name="OKCcode" id="OKCcode" placeholder="..."
                        <#if document??> value="${document.OKCcode}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="OKPDcode">Код ОКПД 2</label>
                <input class="form-control" type="text" name="OKPDcode" id="OKPDcode" placeholder="..."
                        <#if document??> value="${document.OKPDcode}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="adoptionDate">Дата принятия</label>
                <input class="form-control" type="date" name="adoptionDate" id="adoptionDate" placeholder="..."
                        <#if document??> value="${document.adoptionDate}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="introductionDate">Дата введения</label>
                <input class="form-control" type="date" name="introductionDate" id="introductionDate" placeholder="..."
                        <#if document??> value="${document.introductionDate}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="developer">Разработчик</label>
                <input class="form-control" type="text" name="developer" id="developer" placeholder="..."
                        <#if document??> value="${document.developer}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="predecessor">Принят взамен</label>
                <input class="form-control" type="text" name="predecessor" id="predecessor" placeholder="..."
                        <#if document??> value="${document.predecessor}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="contents">Текст документа</label>
                <input class="form-control" type="text" name="contents" id="contents" placeholder="..."
                        <#if document??> value="${document.contents}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="levelOfAcceptance">Уровень принятияа</label>
                <input class="form-control" type="text" name="levelOfAcceptance" id="levelOfAcceptance" placeholder="..."
                        <#if document??> value="${document.levelOfAcceptance}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="changes">Изменения</label>
                <input class="form-control" type="text" name="changes" id="changes" placeholder="..."
                        <#if document??> value="${document.changes}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="status">Статус документа</label>
                <input class="form-control" type="text" name="status" id="status" placeholder="..."
                        <#if document??> value="${document.status}" </#if>
                >
            </div>
            <div class="form-group mb-3">
                <label for="referencesAmount">Количество обращений</label>
                <input class="form-control" type="number" name="referencesAmount" id="referencesAmount" placeholder="..."
                        <#if document??> value="${document.referencesAmount}" </#if>
                >
            </div>
           <div class="form-group mb-3">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile"></label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="form-group mb-3">
                <button class="btn btn-primary" type="submit">Добавить</button>
            </div>
        </form>
    </div>
</@c.page>