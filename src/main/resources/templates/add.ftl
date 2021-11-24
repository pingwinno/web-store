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
<form action="/add" method="POST">
    <div class="form-group my-2 my-lg-0">
        <label for="productName" class="form-label text-white">Product Name</label>
        <input name="productName" type="text" class="form-control" id="productName">
        <label for="price" class="form-label text-white">Price</label>
        <input name="price" type="number" step="0.01" class="form-control" id="price">
        <label for="description" class="form-label text-white">Product Name</label>
        <input name="description" type="text" class="form-control" id="description">
    </div>
    <button type="submit" class="btn btn-primary">Submit</button>
</form>

</body>
</html>