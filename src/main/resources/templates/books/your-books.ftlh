<#import "../parts/template.ftlh" as c>
<#import "../parts/header.ftlh" as h>
<#import "../parts/books-view.ftlh" as b>
<@c.page>
    <@h.header/>
    <div class="container">
        <div class="books-wrapper">
            <#if books??>
                <@b.booksView books true true/>
            <#else>
                <h2>You haven't added any books before! </h2>
            </#if>
        </div>
    </div>
</@c.page>
