<#import "../assigns.ftlh" as a>
<#macro addBookForm authors>
    <form action="/books/add-book" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="hidden" name="id" value="${a.userId}">
        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" name="title" class="form-control" id="title"
                   placeholder="Title..">
        </div>
        <div class="form-group">
            <label for="genre">Genre</label>
            <input type="text" name="genre" class="form-control" id="genre" placeholder="Genre..">
        </div>
        <div class="form-group">
            <label for="price">Price</label>
            <input type="text" name="price" class="form-control" id="price" placeholder="Price..">
        </div>
        <div class="form-group">
            <label for="date">Date</label>
            <input type="date" name="date" class="form-control"  id="date">
        </div>
        <div class="authors-form-box">
            <div>
                <h5>Choose exists authors</h5>
                <label>
                    <select name="authors" multiple class="form-control form-control-lg">
                        <#list authors as author>
                            <option value="${author.id}"> ${author.firstName} ${author.lastName}</option>
                        </#list>
                    </select>
                </label>
            </div>
            |
            <div class="author-fields-box">
                <div class="author-fields">
                    <div class="add-author-box">
                        <h5>Or add new author</h5>
                        <button type="button" id="addFieldsBtn" class="btn btn-primary">add more fields</button>
                    </div>
                    <div class="authors-fields-group">
                        <div class="form-group">
                            <label for="firstname">First Name</label>
                            <input type="text" name="firstname" class="form-control" id="firstname">
                        </div>
                        <div class="form-group">
                            <label for="lastname">Last Name</label>
                            <input type="text" name="lastname" class="form-control" id="lastname">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>

</#macro>