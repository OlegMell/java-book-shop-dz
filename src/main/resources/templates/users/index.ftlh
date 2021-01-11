<#import "../parts/template.ftlh" as c>
<#import "../parts/header.ftlh" as h>

<@c.page>
    <@h.header/>
    <h3 class="p-3">USERS</h3>
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
        <#if users??>
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
                            <input class="block-inp" type="checkbox" <#if user.blocked>checked</#if> >
                        </label>
                    </td>
                    <td>
                        <label>
                            <input class="block-date" min="${now}" value="<#if user.blocked>${user.unblockDate}</#if>"
                                   <#if !user.blocked>disabled</#if> type="date">
                        </label>
                        <button type="button" class="btn btn-success block-submit-btn">Submit</button>
                    </td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</@c.page>
