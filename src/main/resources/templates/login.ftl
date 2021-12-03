<!doctype html>
<html lang="en">
<style type="text/css">
    body {
        background: #343A40 !important;
    }
</style>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Sign in</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body class="text-center">
<div class="row">
    <form class="form-signin col-3 mx-auto" action="/login" modelAttribute="credentials" method="POST">
        <h1 class="h3 mb-3 text-white font-weight-normal">Please sign in</h1>
        <label for="inputUser" class="sr-only">User Name</label>
        <input type="text" id="inputUser" name="userName" class="form-control" placeholder="User Name" required
               autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    </form>
</div>
</body>
</html>
