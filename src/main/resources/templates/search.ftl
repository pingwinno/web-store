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
    <ul class="nav nav-pills">
        <li class="nav-item">
            <a class="nav-link" aria-current="page" href="/">Product List</a>
        </li>
    </ul>
</header>
<table class="table table-striped table-dark">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">Product Name</th>
        <th scope="col">Product Price</th>
        <th scope="col">Description</th>
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
            <a class="btn btn-primary" href="/edit/${product.id}" role="button">Edit</a>
            <a class="btn btn-danger" href="/delete/${product.id}" role="button">Delete</a>
        </td>
        </#list>
    </tbody>
</table>
</body>

</html>