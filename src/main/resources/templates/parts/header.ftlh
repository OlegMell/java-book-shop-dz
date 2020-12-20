<#import "../assigns.ftlh" as auth>
<#macro header>
    <header class="header">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="navbar-brand" href="/">Home</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav navbar-box">
                    <div class="navbar-part">
                        <li class="nav-item active ">
                            <#if auth.user??>
                                <a class="nav-link" href="/books/your-books/${auth.userId}">My
                                    Books<span class="sr-only">(current)</span></a>
                            </#if>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="/books/add-book">Add Book <span
                                        class="sr-only">(current)</span></a>
                        </li>
                    </div>
                    <h1>Book Shop</h1>
                    <#if !auth.user ??>
                        <div class="navbar-part">
                            <li class="nav-item active ">
                                <a class="nav-link btn btn-primary text-white" href="/auth/login">Login <span
                                            class="sr-only">(current)</span></a>
                            </li>
                            <li class="nav-item active">
                                <a class="nav-link btn btn-success text-white" href="/auth/registration">Registration
                                    <span class="sr-only">(current)</span></a>
                            </li>
                        </div>
                    <#else >
                        <div class="navbar-part">
                            <form action="/logout" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <button class="btn-primary text-white">Logout</button>
                            </form>
                        </div>
                    </#if>
                </ul>
            </div>
        </nav>
    </header>
</#macro>