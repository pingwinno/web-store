
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8"/>
    <title>Web 1</title>
</head>
<body>
<table border=1>
    <#list products as product>
    <tr><td>${product.name}<td>${product.price}
            </#list>
</table>

</body>
</html>