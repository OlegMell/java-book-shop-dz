<#import "./parts/template.ftlh" as c>
<#import "./parts/header.ftlh" as h>
<#import "parts/books-view.ftlh" as b>
<#import "parts/filter.ftlh" as f>
<@c.page>
    <#if isActive?? && !isActive>
        <div class="container">
            <h2>Book Shop</h2>
            <div class="alert alert-warning" role="alert">
                You need activate your account!<br>
                Message with activation link already send to your email!
            </div>
        </div>
    <#elseif isBlocked?? && isBlocked>
        <div class="container">
            <h2>Book Shop</h2>
            <div class="alert alert-danger" role="alert">
                Your account was blocked by admin!
            </div>
        </div>
    <#else>
        <@h.header/>
        <div class="wrap">
            <#if authors?? >
                <@f.filter authors genres/>
            </#if>
            <div class="books-wrapper">
                <#if !books??>
                    <div class="container">
                        <div class="alert alert-info" role="alert">
                            We haven't books for you now((<br>But you can add books after login!
                        </div>
                    </div>
                <#else >
                    <@b.booksView books/>
                </#if>
            </div>
        </div>
    </#if>
</@c.page>