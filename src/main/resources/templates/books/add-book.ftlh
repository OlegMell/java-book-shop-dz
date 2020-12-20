<#import "../parts/template.ftlh" as c>
<#import "../parts/header.ftlh" as h>
<#import "../parts/add-book-form.ftlh" as abf>
<@h.header/>
<@c.page>
    <div class="container">
        <div class="form shadow p-3 mb-5 bg-white rounded slide-top">
            <div class="form-header">
                <a href="/">
                    <svg width="3em" height="3em" viewBox="0 0 16 16" class="bi bi-arrow-left-short" fill="currentColor"
                         xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd"
                              d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5z"/>
                    </svg>
                </a>
                <h3>Add Book</h3>
            </div>
            <@abf.addBookForm authors/>
        </div>
    </div>
</@c.page>