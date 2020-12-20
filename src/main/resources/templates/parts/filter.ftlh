<#macro filter authors genres>
    <div class="filter-box">
        <h3>Filters</h3>
        <form id="filterForm">
            <lable>Authors
                <select name="authorId" class="form-select" aria-label="Default select example">
                    <option value="all">All authors</option>
                    <#list authors as author>
                        <option value="${author.id}">${author.firstName} ${author.lastName}</option>
                    </#list>
                </select>
            </lable>
            <lable>Genres
                <select name="genreId" class="form-select" aria-label="Default select example">
                    <option value="all">All genres</option>
                    <#list genres as genre>
                        <option value="${genre.id}">${genre.name}</option>
                    </#list>
                </select>
            </lable>
            <div class="price-box">
                <label>Price
                    <input type="form-control" name="price" id="priceFilter">
                </label>
            </div>
            <button type="submit" class="btn btn-primary" id="filterFormBtn">Filter</button>
        </form>
    </div>
</#macro>