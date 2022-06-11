<#import "parts/common.ftl" as c>

<@c.page>
    List of users requests

    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users! as user>
            <tr>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td><a href="/company/requests/${user.id}"> Add</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>