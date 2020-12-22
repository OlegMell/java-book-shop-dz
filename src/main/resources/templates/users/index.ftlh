<#import "../parts/template.ftlh" as c>
<#import "../parts/header.ftlh" as h>

<@c.page>
    <@h.header/>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Username</th>
            <th scope="col">Email</th>
            <th scope="col">Roles</th>
            <th scope="col">Is Active</th>
            <th scope="col">Is Block</th>
            <th scope="col">Unblock Date</th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr data-user-id="${user.id}">
                <th scope="row">${user.id}</th>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>
                    <#list user.roles as role>
                        ${role} <#sep>|</#sep>
                    </#list>
                </td>
                <td>
                    <label>
                        <input type="checkbox" <#if user.active>checked</#if> >
                    </label>
                </td>
                <td>
                    <label>
                        <input id="blockInp" type="checkbox" <#if user.blocked>checked</#if> >
                    </label>
                </td>
                <td>
                    <label>
                        <input id="blockDate" <#if !user.blocked>disabled</#if> type="datetime-local">
                    </label>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>
