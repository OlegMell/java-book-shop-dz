<#import "../parts/template.ftlh" as c>
<#import "../parts/auth-form.ftlh" as f>
<@c.page>
    <div class="auth-form-wrapper">
        <div class="auth-form shadow p-3 mb-5 bg-white rounded slide-top">
            <#if message??>
                <div>
                    <h5>${message}</h5>
                    <a href="/auth/login">Login Page</a>
                </div>
            </#if>
            <@f.form "Registration" true />
        </div>
    </div>
</@c.page>