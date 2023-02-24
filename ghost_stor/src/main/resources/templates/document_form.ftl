<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
        <form method="post" enctype="multipart/form-data" <#if parentDocId??> action="/document" </#if> id="form"
              onsubmit="return confirm('Закончить редактирование и сохранить изменения?');">
            <#if parentDocId??>
                <input type="hidden" name="parentDocId" value="${parentDocId}" >
            </#if>
            <table class="table">
                <tr>
                    <th>Поле</th>
                    <th>Текущее значение поля</th>
                </tr>
                <#list fieldNames as key>
                    <tr>
                        <td>${ruFieldNames[key]}</td>
                        <td>
                            <#if key == "levelOfAcceptance">
                                <select name="levelOfAcceptance">
                                    <#list levels?keys as level>
                                        <#if "-" != level>
                                            <option value="${level}" <#if lastFields?? && lastFields[key] == level>selected</#if>>
                                                ${levels[level]}
                                            </option>
                                        </#if>
                                    </#list>
                                </select>
                            <#elseif key == "status">
                                <select name="status">
                                    <#list statuses?keys as status>
                                        <#if "-" != status>
                                            <option value="${status}" <#if lastFields?? && lastFields[key] == status>selected</#if>>
                                                ${statuses[status]}
                                            </option>
                                        </#if>
                                    </#list>
                                </select>
                            <#elseif key == "fileDesc">
                                <input class="form-control" type="text" name=${key} id=${key} required
                                        <#if lastFields??> value="${lastFields[key]}" </#if>
                                >
                            <#elseif key == "adoptionDate" || key == "introductionDate">
                                <input class="form-control" type="date" name=${key} id=${key}
                                        <#if lastFields??> value="${lastFields[key]}" </#if>
                                >
                            <#elseif key == "headContent" || key == "keywords" || key == "keyPhrases">
                                <textarea name=${key} id=${key} form="form" style="width: 500px; height: 100px;"><#if lastFields??>${(lastFields[key] == '-')?string("", lastFields[key])}</#if></textarea>
                            <#elseif key == "normReferences">
                                <div class="autocomplete" style="width:200px;" id="normRefBlock">
                                    <button type="button" onclick="createInput('')" style='margin-bottom: 10px;'>+</button>
                                </div>
                            <#elseif key == "predecessor">
                                <input class="form-control" type="text" name=${key} id=${key}
                                        <#if lastFields??>
                                            value="${(lastFields[key] == '-')?string("", lastFields[key])}"
                                        <#elseif parentDocDesc??>
                                            value="${parentDocDesc}"
                                        </#if>
                                >
                            <#else>
                                <input class="form-control" type="text" name=${key} id=${key}
                                        <#if lastFields??>
                                            value="${(lastFields[key] == '-')?string("", lastFields[key])}"
                                        </#if>
                                >
                            </#if>
                        </td>
                    </tr>
                </#list>
            </table>
           <div class="form-group mb-3">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile"></label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="form-group mb-3">
                <button class="btn btn-primary" type="submit">Добавить</button>
                <a href="/main">
                    <button class="btn btn-primary" type="button" style="margin-left: 10px; background-color: #99DDFF; border-color: #99DDFF">
                        Отменить
                    </button>
                </a><br>
            </div>

        </form>
    <script>
        window.counter = 1;
        let names = [<#list ghostDescs as desc>
            <#if lastFields??>
                <#if desc != lastFields["fileDesc"]>"${desc}",</#if>
            <#else>
                "${desc}",
            </#if>
        </#list>];

        <#if activeLinks??>
            <#list activeLinks?keys as a_link>
            createInput("${a_link}");
            </#list>
        </#if>

        <#if inactiveLinks??>
            <#list inactiveLinks as ina_link>
            createInput("${ina_link}");
            </#list>
        </#if>

        function createInput(str_value) {
            let my_block = null;
            let newDiv = null;
            let name = "normReferences_" + window.counter++;

            newDiv = document.createElement("div");
            newDiv.style = 'display: flex; margin-bottom: 10px;';

            newDiv.innerHTML = `<input id="` + name +
                `" type="text" name="` + name +
                `" placeholder="..." class="form-control" autocomplete="off" value="` + str_value +
                `"><button type="button" onclick='removeInput(this)' style='margin-left: 20px;'>X</button>`;

            my_block = document.getElementById("normRefBlock");
            my_block.insertAdjacentElement('beforeend', newDiv);

            autocomplete(document.getElementById(name), names);
        }

        function removeInput(el) {
            el.parentElement.remove()
        }

        function autocomplete(inp, arr) {
            /*the autocomplete function takes two arguments,
            the text field element and an array of possible autocompleted values:*/
            var currentFocus;
            /*execute a function when someone writes in the text field:*/
            inp.addEventListener("input", function(e) {
                var a, b, i, val = this.value;
                /*close any already open lists of autocompleted values*/
                closeAllLists();
                if (!val) { return false;}
                currentFocus = -1;
                /*create a DIV element that will contain the items (values):*/
                a = document.createElement("DIV");
                a.setAttribute("id", this.id + "autocomplete-list");
                a.setAttribute("class", "autocomplete-items");
                /*append the DIV element as a child of the autocomplete container:*/
                this.parentNode.appendChild(a);
                /*for each item in the array...*/
                for (i = 0; i < arr.length; i++) {
                    /*check if the item starts with the same letters as the text field value:*/
                    if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                        /*create a DIV element for each matching element:*/
                        b = document.createElement("DIV");
                        /*make the matching letters bold:*/
                        b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                        b.innerHTML += arr[i].substr(val.length);
                        /*insert a input field that will hold the current array item's value:*/
                        b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                        /*execute a function when someone clicks on the item value (DIV element):*/
                        b.addEventListener("click", function(e) {
                            /*insert the value for the autocomplete text field:*/
                            inp.value = this.getElementsByTagName("input")[0].value;
                            /*close the list of autocompleted values,
                            (or any other open lists of autocompleted values:*/
                            closeAllLists();
                        });
                        a.appendChild(b);
                    }
                }
            });
            /*execute a function presses a key on the keyboard:*/
            inp.addEventListener("keydown", function(e) {
                var x = document.getElementById(this.id + "autocomplete-list");
                if (x) x = x.getElementsByTagName("div");
                if (e.keyCode == 40) {
                    /*If the arrow DOWN key is pressed,
                    increase the currentFocus variable:*/
                    currentFocus++;
                    /*and and make the current item more visible:*/
                    addActive(x);
                } else if (e.keyCode == 38) { //up
                    /*If the arrow UP key is pressed,
                    decrease the currentFocus variable:*/
                    currentFocus--;
                    /*and and make the current item more visible:*/
                    addActive(x);
                } else if (e.keyCode == 13) {
                    /*If the ENTER key is pressed, prevent the form from being submitted,*/
                    e.preventDefault();
                    if (currentFocus > -1) {
                        /*and simulate a click on the "active" item:*/
                        if (x) x[currentFocus].click();
                    }
                }
            });
            function addActive(x) {
                /*a function to classify an item as "active":*/
                if (!x) return false;
                /*start by removing the "active" class on all items:*/
                removeActive(x);
                if (currentFocus >= x.length) currentFocus = 0;
                if (currentFocus < 0) currentFocus = (x.length - 1);
                /*add class "autocomplete-active":*/
                x[currentFocus].classList.add("autocomplete-active");
            }
            function removeActive(x) {
                /*a function to remove the "active" class from all autocomplete items:*/
                for (var i = 0; i < x.length; i++) {
                    x[i].classList.remove("autocomplete-active");
                }
            }
            function closeAllLists(elmnt) {
                /*close all autocomplete lists in the document,
                except the one passed as an argument:*/
                var x = document.getElementsByClassName("autocomplete-items");
                for (var i = 0; i < x.length; i++) {
                    if (elmnt != x[i] && elmnt != inp) {
                        x[i].parentNode.removeChild(x[i]);
                    }
                }
            }
            /*execute a function when someone clicks in the document:*/
            document.addEventListener("click", function (e) {
                closeAllLists(e.target);
            });
        }
    </script>
</@c.page>