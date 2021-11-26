<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<style type="text/css">
    body {
        background: #343A40 !important;
    }
</style>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <meta charset="UTF-8"/>
    <title>Product list</title>
</head>
<body>
<header class="p-3 bg-dark text-white">
    <form class="form-inline float-right" action="/search" method="GET">
        <input class="form-control mr-sm-2" name="searchInput" type="search" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-success my-2 my-sm-0 mr-3" id="searchInput" type="submit">Search</button>
        <a type="button" class="btn btn-info my-2 my-sm-0 mr-3" href="/basket">Basket</a>
        <a class="btn btn-warning" aria-current="page" href="/logout">Logout</a>
    </form>

</header>
<table class="table table-striped table-dark">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">Product Name</th>
        <th scope="col">Description</th>
        <th scope="col">Product Price</th>
        <th scope="col">Creation Date</th>
        <th scope="col">Actions</th>
    </tr>
    </thead>
    <tbody>
    <#list products as product>
    <tr>
        <td>${product.id}</td>
        <td>${product.name}</td>
        <td>${product.description}</td>
        <td>${product.price}</td>
        <td>${product.creationDate}</td>
        <td>
            <form action="/basket/add" method="post">
                <button name="productId" class="btn btn-success" value=${product.id}>Add to basket</button>
            </form>
        </td>
        </#list>
    </tbody>
</table>
</body>

</html>