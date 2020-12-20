<#import "../parts/template.ftlh" as c>
<#import "../parts/header.ftlh" as h>
<#import "../parts/edit-book-form.ftlh" as bf>
<@c.page>
    <@h.header/>
    <div class="container">
        <@bf.editBookForm authors book/>
    </div>
</@c.page>