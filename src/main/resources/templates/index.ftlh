<#import "./parts/template.ftlh" as c>
<#import "./parts/header.ftlh" as h>
<#import "parts/books-view.ftlh" as b>
<#import "parts/filter.ftlh" as f>
<@c.page>
    <#if isActive?? >
        <#if !isActive>
            <h2>Book Shop</h2>
            <div class="alert alert-warning" role="alert">
                You need activate your account!<br>
                Message with activation link already send to your email!
            </div>
        <#elseif isBlocked?? && isBlocked>
            <h2>Book Shop</h2>
            <div class="alert alert-error" role="alert">
                Your account was blocked by admin!
            </div>
        <#else>
            <@h.header/>
            <div class="wrap">
                <#if books ?? >
                    <@f.filter authors genres/>
                </#if>
                <div class="books-wrapper">
                    <#if !books??>
                        <h2>We haven't books for you now((<br>But you can add your books!!!</h2>
                    </#if>
                    <@b.booksView books/>
                </div>
            </div>
        </#if>
    </#if>
</@c.page>