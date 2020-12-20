<#import "../parts/template.ftlh" as c>
<#import "../parts/auth-form.ftlh" as f>
<@c.page>
    <div class="auth-form-wrapper">
        <div class="auth-form shadow p-3 mb-5 bg-white rounded slide-top">
            <@f.form "Authorize" />
        </div>
    </div>
</@c.page>