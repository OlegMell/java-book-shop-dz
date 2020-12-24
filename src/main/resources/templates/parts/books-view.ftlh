<#macro booksView books removable = false editable = false>
    <#list books as book>
        <div class="card book" style="width: 18rem;">
            <img src="https://pngimg.com/uploads/book/book_PNG2105.png" class="card-img-top" alt="book image">
            <div class="card-body">
                <a href="/book/${book.id}">
                    <h5 class="card-title">${book.title}</h5>
                </a>
                <h6 class="card-title">${book.genre.name}</h6>
                <div class="card-title">${book.date.toString()}</div>
                <#if book.price??>
                    <div class="card-title">$ ${book.price}</div>
                </#if>
                <#list book.authors as author>
                    <div>${author.firstName} ${author.lastName}</div>
                </#list>
            </div>
            <div>
                <#if removable>
                    <a href="/books/remove-book/${book.id}" class="btn btn-danger">Remove</a>
                </#if>
                <#if editable>
                    <a href="/books/edit-book/${book.id}" class="btn btn-primary">Edit</a>
                </#if>
            </div>
        </div>
    </#list>
</#macro>